package org.hcgames.icefyre.party.fight;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.game.fight.FightStage;
import org.hcgames.icefyre.game.fight.duel.DuelRequest;
import org.hcgames.icefyre.game.fight.inventory.FightInventory;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.kit.selection.KitSelectionProfile;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.PartyMemory;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.packet.ProjectileUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class PartyFightListeners implements Listener {

    private Icefyre main = Icefyre.getInstance();

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = Profile.getByPlayer(player);
            if (profile != null) {
                PartyFight fight = profile.getPartyFight();
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
            PartyFight fight = profile.getPartyFight();
            if (fight != null && fight.getStage() == FightStage.FIGHTING) {
                PartyMemory memory = fight.getPartyByProfile(profile);
                if (memory != null) {
                    profile.setFightInventory(new FightInventory(profile, new ArrayList<>(Arrays.asList(player.getInventory().getContents())), new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents())), (int) player.getHealth()));
                    fight.getDeadPlayers().get(memory).add(player);
                    if (fight.getDeadPlayers().get(memory).size() == memory.getSize()) {
                        fight.end(fight.getOpponent(memory));
                    }
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
            PartyFight fight = profile.getPartyFight();
            if (fight != null && fight.getStage() == FightStage.FIGHTING) {

                for (Profile participant : fight.getAllProfiles()) {
                    participant.getPlayer().sendMessage(event.getDeathMessage());
                }

                PartyMemory memory = fight.getPartyByProfile(profile);
                if (memory != null) {
                    profile.setFightInventory(new FightInventory(profile, new ArrayList<>(Arrays.asList(player.getInventory().getContents())), new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents())), (int) player.getHealth()));
                    fight.getDeadPlayers().get(memory).add(player);
                    if (fight.getDeadPlayers().get(memory).size() == memory.getSize()) {
                        fight.end(fight.getOpponent(memory));
                    }
                }

                event.setDeathMessage(null);

                for (ItemStack itemStack : event.getDrops()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        Item item = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                        for (Player other : Bukkit.getOnlinePlayers()) {
                            if (memory != null) {
                                if (memory.contains(Profile.getByPlayer(other)) || fight.getOpponent(memory).contains(Profile.getByPlayer(other))) continue;
                                ProjectileUtil.hide(other, item);
                            }
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

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            Player damager;
            if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            } else if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    damager = (Player) ((Projectile) event.getDamager()).getShooter();
                } else {
                    return;
                }
            } else {
                return;
            }

            if (damager.getUniqueId().equals(player.getUniqueId())) {
                return;
            }

            Party party = Party.getByPlayer(player);
            if (party != null) {
                if (party.contains(Profile.getByPlayer(damager))) {
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        Inventory inventory = event.getInventory();
        ItemStack item = event.getCurrentItem();
        if (profile != null && item != null && item.getType() == Material.SKULL_ITEM && item.getItemMeta().hasDisplayName() && inventory.getTitle().contains(main.getLangFile().getString("PARTY.INVENTORY.TITLE").split(" ")[0])) {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName().split(" ")[0]);
            name = name.substring(0, name.lastIndexOf("'"));
            Player toDuel = Bukkit.getPlayer(name);
            Party party = Party.getByPlayer(player);
            Party toDuelParty = Party.getByPlayer(toDuel);

            if (toDuel != null && party != null && toDuelParty != null) {

                if (!party.getLeader().getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    return;
                }

                if (toDuelParty.getLeader().equals(party.getLeader())) {
                    return;
                }

                player.openInventory(DuelRequest.getLadderInventory());
                profile.getDuelRequests().put(toDuel.getUniqueId(), new DuelRequest(true));
                return;
            }

            player.closeInventory();

        }
    }

}
