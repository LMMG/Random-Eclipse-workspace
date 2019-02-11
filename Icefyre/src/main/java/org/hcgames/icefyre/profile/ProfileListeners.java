package org.hcgames.icefyre.profile;

import net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.fight.PartyFight;
import org.hcgames.icefyre.util.packet.ProjectileUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ProfileListeners implements Listener {

    private static Icefyre main = Icefyre.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        new Profile(event.getPlayer());
        event.setJoinMessage(null);
        Icefyre.getInstance().getFightHandler().setupPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Profile profile = Profile.getByPlayer(event.getPlayer());
        if (profile != null) {
            profile.remove();

            Party party = profile.getParty();
            if (party != null) {
                Party.getParties().remove(party);
                party.getMembers().add(party.getLeader());
                party.getLeader().getInvitedPlayers().clear();
                for (Profile member : new ArrayList<>(party.getMembers())) {
                    member.getPlayer().sendMessage(main.getLangFile().getString("PARTY.DELETED"));
                    main.getFightHandler().setupPlayer(member.getPlayer());
                    main.getLobby().setupPlayer(member.getPlayer());
                }
            }
        }

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.getName().equals(event.getPlayer().getName())) continue;
            ((CraftPlayer) other).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) event.getPlayer()).getHandle()));
        }

        for (Profile other : Profile.getProfiles()) {
            if (other.getInvitedPlayers().contains(event.getPlayer().getUniqueId())) {
                other.getInvitedPlayers().remove(event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof ThrownPotion) && projectile.getShooter() instanceof Player) {
            Player player = (Player) projectile.getShooter();
            Profile profile = Profile.getByPlayer(player);
            if (profile != null) {

                PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect("random.bow", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), (float) 0.5, (float) 0.5);
                profile.getPackets().add(packet);

                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

                Profile opponent = null;
                Fight fight = profile.getFight();
                if (fight != null) {
                    opponent = fight.getOpponent(player);

                    PacketPlayOutNamedSoundEffect opponentPacket = new PacketPlayOutNamedSoundEffect("random.bow", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), (float) 0.5, (float) 0.5);

                    opponent.getPackets().add(opponentPacket);

                    ((CraftPlayer)opponent.getPlayer()).getHandle().playerConnection.sendPacket(opponentPacket);
                }

                List<Player> toBlock = new ArrayList<>();
                if (profile.getPartyFight() != null) {
                    for (Profile other : profile.getPartyFight().getAllProfiles()) {
                        toBlock.add(other.getPlayer());

                        PacketPlayOutNamedSoundEffect opponentPacket = new PacketPlayOutNamedSoundEffect("random.bow", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), (float) 0.5, (float) 0.5);

                        other.getPackets().add(opponentPacket);

                        ((CraftPlayer)other.getPlayer()).getHandle().playerConnection.sendPacket(opponentPacket);
                    }
                }

                Profile finalOpponent = opponent;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player other : Bukkit.getOnlinePlayers()) {
                            if (other.getName().equals(player.getName())) continue;
                            if (finalOpponent != null && other.getName().equals(finalOpponent.getName())) continue;
                            if (toBlock.contains(other)) continue;

                            ProjectileUtil.hide(other, event.getEntity());
                        }
                    }
                }.runTaskLater(main, 1L);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.getType() == Material.POTION && itemStack.getDurability() >= 16000) {
            Profile profile = Profile.getByPlayer(player);
            if (profile != null && event.getAction().name().contains("RIGHT")) {
                event.setCancelled(true);

                PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect("random.bow", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), (float) 0.5, (float) 0.5);
                profile.getPackets().add(packet);

                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

                Entity entity = ProjectileUtil.launchPotion(player, ThrownPotion.class, null, itemStack);
                Profile opponent = null;
                Fight fight = profile.getFight();
                if (fight != null) {
                    opponent = fight.getOpponent(player);
                    PacketPlayOutNamedSoundEffect opponentPacket = new PacketPlayOutNamedSoundEffect("random.bow", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), (float) 0.5, (float) 0.5);

                    opponent.getPackets().add(opponentPacket);

                    ((CraftPlayer)opponent.getPlayer()).getHandle().playerConnection.sendPacket(opponentPacket);
                }

                Profile finalOpponent = opponent;

                List<Player> toBlock = new ArrayList<>();
                if (profile.getPartyFight() != null) {
                    for (Profile other : profile.getPartyFight().getAllProfiles()) {
                        toBlock.add(other.getPlayer());

                        PacketPlayOutNamedSoundEffect opponentPacket = new PacketPlayOutNamedSoundEffect("random.bow", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), (float) 0.5, (float) 0.5);

                        other.getPackets().add(opponentPacket);

                        ((CraftPlayer)other.getPlayer()).getHandle().playerConnection.sendPacket(opponentPacket);
                    }
                }

                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other.getName().equals(player.getName())) continue;
                    if (finalOpponent != null && other.getName().equals(finalOpponent.getName())) continue;
                    if (toBlock.contains(other)) continue;

                    ProjectileUtil.hide(other, entity);
                }

                if (itemStack.getAmount() > 1) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                } else {
                    player.setItemInHand(new ItemStack(Material.AIR));
                }

                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event) {
        ThrownPotion potion =  event.getEntity();
        ProjectileSource source = potion.getShooter();
        if (source instanceof Player) {
            Player player = (Player) source;
            Profile profile = Profile.getByPlayer(player);
            if (profile != null) {

                Profile opponent = null;
                Fight fight = profile.getFight();
                if (fight != null) {
                    opponent = fight.getOpponent(player);
                }


                profile.getPotions().add(potion.getLocation());
                if (opponent != null) {
                    opponent.getPotions().add(potion.getLocation());
                }

                List<Player> toBlock = new ArrayList<>();
                if (profile.getPartyFight() != null) {
                    for (Profile other : profile.getPartyFight().getAllProfiles()) {
                        other.getPotions().add(potion.getLocation());
                        toBlock.add(other.getPlayer());
                    }
                }

                Iterator<LivingEntity> iterator = event.getAffectedEntities().iterator();
                while (iterator.hasNext()) {
                    LivingEntity entity = iterator.next();
                    if (entity.equals(player) || (opponent != null && entity.equals(opponent.getPlayer())) || toBlock.contains(entity)) {
                        continue;
                    }
                    event.setIntensity(entity, 0);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        if (profile != null) {
            Profile opponent = null;
            Fight fight = profile.getFight();
            if (fight != null) {
                opponent = fight.getOpponent(player);
            }

            List<Player> toBlock = new ArrayList<>();
            if (profile.getPartyFight() != null) {
                for (Profile other : profile.getPartyFight().getAllProfiles()) {
                    toBlock.add(other.getPlayer());
                }
            }

            Profile finalOpponent = opponent;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (other.getName().equals(player.getName())) continue;
                        if (finalOpponent != null && other.getName().equals(finalOpponent.getName())) continue;
                        if (toBlock.contains(other)) continue;

                        ProjectileUtil.hide(other, event.getItemDrop());
                    }
                }
            }.runTaskLater(main, 1L);
        }
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                Location location = event.getEntity().getLocation();
                Player player = (Player) event.getEntity().getShooter();
                PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect("random.bowhit", location.getBlockX(), location.getBlockY(), location.getBlockZ(), (float) 0.9, (float) 1.2);
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

                Profile profile = Profile.getByPlayer(player);
                if (profile != null) {
                    Fight fight = profile.getFight();
                    if (fight != null) {
                        Profile opponent = fight.getOpponent(player);
                        PacketPlayOutNamedSoundEffect opponentPacket = new PacketPlayOutNamedSoundEffect("random.bowhit", location.getBlockX(), location.getBlockY(), location.getBlockZ(), (float) 0.9, (float) 1.2);
                        ((CraftPlayer)opponent.getPlayer()).getHandle().playerConnection.sendPacket(opponentPacket);
                    }

                    PartyFight partyFight = profile.getPartyFight();
                    if (partyFight != null) {
                        for (Profile opponent : partyFight.getAllProfiles()) {
                            if (opponent.equals(profile)) continue;
                            PacketPlayOutNamedSoundEffect opponentPacket = new PacketPlayOutNamedSoundEffect("random.bowhit", location.getBlockX(), location.getBlockY(), location.getBlockZ(), (float) 0.9, (float) 1.2);
                            ((CraftPlayer)opponent.getPlayer()).getHandle().playerConnection.sendPacket(opponentPacket);
                        }
                    }
                }

            }
        }
    }


    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Profile profile = Profile.getByPlayer(event.getPlayer());
        if (profile != null) {
            if (profile.getState() != ProfileState.IN_STAFF) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Profile profile = Profile.getByPlayer(event.getPlayer());
        if (profile != null) {
            if (profile.getState() != ProfileState.IN_STAFF) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Profile profile = Profile.getByPlayer(event.getPlayer());
        if (profile != null) {
            if (profile.getState() != ProfileState.IN_STAFF) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        Profile profile = Profile.getByPlayer(event.getPlayer());
        if (profile != null) {
            if (profile.getState() != ProfileState.IN_STAFF) {
                event.setCancelled(true);
            }
        }
    }

}
