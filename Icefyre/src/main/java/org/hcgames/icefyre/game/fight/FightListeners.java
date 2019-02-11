package org.hcgames.icefyre.game.fight;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.bizarrealex.aether.scoreboard.cooldown.BoardFormat;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.kit.selection.KitSelectionProfile;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.fight.PartyFight;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.packet.ProjectileUtil;

public class FightListeners implements Listener {

    private Icefyre main = Icefyre.getInstance();

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        if (profile != null) {
            KitSelectionProfile selectionProfile = profile.getSelectionProfile();
            if (selectionProfile != null) {
                player.sendMessage(main.getLangFile().getString("KIT_SELECTOR.CANCELLED").replace("%KIT%", selectionProfile.getLadder().getDefaultKit().getName()));
                selectionProfile.getLadder().getDefaultKit().apply(player);
                profile.setSelectionProfile(null);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        ItemStack itemStack = event.getCurrentItem();
        if (profile != null && itemStack != null && itemStack.getType() != Material.AIR) {
            KitSelectionProfile selectionProfile = profile.getSelectionProfile();
            if (selectionProfile != null && event.getInventory().getTitle().equals(selectionProfile.getInventory().getTitle())) {
                event.setCancelled(true);

                Kit kit;
                if (event.getRawSlot() == 8) {
                    kit = selectionProfile.getLadder().getDefaultKit();
                } else {
                    kit = profile.getKits().get(selectionProfile.getLadder())[event.getRawSlot()];
                }

                if (kit != null) {
                    player.sendMessage(main.getLangFile().getString("KIT_SELECTOR.APPLIED").replace("%KIT%", kit.getName()));
                    profile.setSelectionProfile(null);
                    kit.apply(player);
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickEventFightEnd(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        if (profile != null) {
            Fight fight = profile.getFight();
            if (fight != null && fight.getStage() == FightStage.END) {
                if (!event.getClickedInventory().equals(player.getInventory())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = Profile.getByPlayer(player);
            if (profile != null) {
                Fight fight = profile.getFight();
                if (fight != null) {
                    if (fight.getStage() != FightStage.FIGHTING) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        if (profile != null) {
            Fight fight = profile.getFight();
            if (fight != null) {
                fight.end(fight.getOpponent(player));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(main.getLobby().getLocation());
        main.getLobby().setupPlayer(event.getPlayer());
        main.getFightHandler().setupPlayer(event.getPlayer());
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile != null) {
            profile.setFight(null);
            profile.setPartyFight(null);
            profile.setState(ProfileState.IN_LOBBY);

            Party party = Party.getByPlayer(player);
            if (party != null) {
                if (party.isLeader(profile.getPlayer())) {
                    main.getPartyHandler().setupProfile(profile);
                }
            }

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Profile.getByPlayer(player);

        if (profile != null) {
            profile.getPackets().clear();
            profile.getPotions().clear();
            Fight fight = profile.getFight();
            if (fight != null) {

                for (Profile participant : fight.getParticipants()) {
                    participant.getPlayer().sendMessage(event.getDeathMessage());
                }

                fight.end(fight.getOpponent(player));

                event.setDeathMessage(null);

                for (ItemStack itemStack : event.getDrops()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        Item item = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                        for (Player other : Bukkit.getOnlinePlayers()) {
                            if (fight.getOpponent(other) != null) continue;
                            ProjectileUtil.hide(other, item);
                        }
                    }
                }

                event.getDrops().clear();
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(main, 20L);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Board board = Board.getByPlayer(player);
        if (board != null && event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {

            if (player.getGameMode() == GameMode.CREATIVE) return;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) { event.setCancelled(true);  player.updateInventory(); return; }

            if (event.getAction() == Action.RIGHT_CLICK_AIR) {

                Profile profile = Profile.getByPlayer(player);
                if (profile != null) {
                    Fight fight = profile.getFight();
                    if (fight != null && fight.getStage() == FightStage.COUNTDOWN) {
                        event.setCancelled(true);
                        player.updateInventory();
                        return;
                    }

                    PartyFight partyFight = profile.getPartyFight();
                    if (partyFight != null && partyFight.getStage() == FightStage.COUNTDOWN) {
                        event.setCancelled(true);
                        player.updateInventory();
                        return;
                    }
                }

                if (board.getCooldown("enderpearl") != null) {
                    player.sendMessage(main.getLangFile().getString("ENDERPEARL_COOLDOWN").replace("%TIME%", board.getCooldown("enderpearl").getFormattedString(BoardFormat.SECONDS)));
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }
                new BoardCooldown(board, "enderpearl", main.getConfigFile().getInt("ENDERPEARL_COOLDOWN_DURATION"));
                if (profile != null) {
                    profile.setPearlLocation(player.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Profile profile = Profile.getByPlayer(player);
            if (profile != null) {
                if (profile.getFight() == null && profile.getPartyFight() == null) {
                    event.setCancelled(true);
                } else {
                    Location location = event.getTo();
                    location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                    Location pearlLocation = profile.getPearlLocation();

                    if (pearlLocation == null) {
                        return;
                    }

                    if (location.getBlockY() > pearlLocation.getBlockY()) {
                        while (!location.getBlock().isEmpty()) {
                            location = location.add(pearlLocation.getDirection().multiply(-2.5));
                        }
                    }

                    location.setDirection(player.getLocation().getDirection());

                    event.setTo(location);
                }
            }
        }
    }

}
