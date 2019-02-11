package com.alexandeh.ekko.profiles;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.events.player.PlayerCancelFactionTeleportEvent;
import com.alexandeh.ekko.factions.events.player.PlayerDisbandFactionEvent;
import com.alexandeh.ekko.factions.events.player.PlayerLeaveFactionEvent;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.profiles.teleport.ProfileTeleportType;
import com.alexandeh.ekko.utils.LocationSerialization;
import com.alexandeh.ekko.utils.player.PlayerUtility;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;

import java.math.BigDecimal;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class ProfileListeners implements Listener {

    private Ekko main = Ekko.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile.sendPlayerTabUpdate(player);
        Profile profile = Profile.getByUuid(player.getUniqueId());

        profile.setLastInside(Claim.getProminentClaimAt(player.getLocation()));

        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(player.getUniqueId());

        if (!player.hasPlayedBefore()) {
            SystemFaction faction = SystemFaction.getByName("Spawn");
            if (faction != null && faction.getHome() != null) {
                player.teleport(LocationSerialization.deserializeLocation(faction.getHome()));
            }
        }

        if (offlinePlayer == null) {
            new SimpleOfflinePlayer(player);
        } else {
            if (!(offlinePlayer.getName().equals(player.getName()))) {
                offlinePlayer.setName(player.getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

            profile.setLastInside(Claim.getProminentClaimAt(event.getTo()));
        }
    }

    @EventHandler
    public void onPlayerLeaveFactionEvent(PlayerLeaveFactionEvent event) {
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        if (profile.getClaimProfile() != null && profile.getClaimProfile().getFaction() == event.getFaction()) {
            profile.setClaimProfile(null);
        }
    }

    @EventHandler
    public void onFactionDisband(PlayerDisbandFactionEvent event) {
        for (Player player : event.getFaction().getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getClaimProfile() != null && profile.getClaimProfile().getFaction() == event.getFaction()) {
                profile.setClaimProfile(null);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            final Player player = event.getPlayer();
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getTeleportWarmup() != null) {
                if (profile.getTeleportWarmup().getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, null, ProfileTeleportType.HOME_TELEPORT));
                    profile.setTeleportWarmup(null);
                    player.sendMessage(main.getLangConfig().getString("ERROR.TELEPORT_CANCELLED"));
                } else {
                    if (player.getLocation().distance(profile.getTeleportWarmup().getEvent().getInitialLocation()) >= main.getMainConfig().getInt("TELEPORT_COUNTDOWN.STUCK.DISTANCE")) {
                        profile.getTeleportWarmup().getEvent().setCancelled(true);
                        Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, profile.getTeleportWarmup().getEvent().getFaction(), ProfileTeleportType.STUCK_TELEPORT));
                        profile.setTeleportWarmup(null);
                        player.sendMessage(main.getLangConfig().getString("ERROR.TELEPORT_CANCELLED"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getTeleportWarmup() != null) {
                if (profile.getTeleportWarmup().getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, null, ProfileTeleportType.HOME_TELEPORT));
                    profile.setTeleportWarmup(null);
                    player.sendMessage(main.getLangConfig().getString("ERROR.TELEPORT_CANCELLED"));
                } else {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, profile.getTeleportWarmup().getEvent().getFaction(), ProfileTeleportType.STUCK_TELEPORT));
                    profile.setTeleportWarmup(null);
                    player.sendMessage(main.getLangConfig().getString("ERROR.TELEPORT_CANCELLED"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        SystemFaction faction = SystemFaction.getByName("Spawn");
        if (faction != null && faction.getHome() != null) {
            event.setRespawnLocation(LocationSerialization.deserializeLocation(faction.getHome()));
        }

        profile.setLastInside(Claim.getProminentClaimAt(event.getRespawnLocation()));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getInventory().remove(Faction.getWand(main));
        Profile profile = Profile.getExistingByUuid(player.getUniqueId());
        if (profile != null) {
            Profile.getProfiles().remove(profile);
            Profile.getProfileMap().remove(profile.getUuid());
            if (profile.getTeleportWarmup() != null) {
                profile.getTeleportWarmup().cancel();
            }
        }
    }

    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
        event.getSource().remove(Faction.getWand(main));
        event.getDestination().remove(Faction.getWand(main));
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();
            Player damager;

            if (e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            } else if (e.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) e.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    damager = (Player) projectile.getShooter();
                } else {
                    return;
                }
            } else {
                return;
            }

            if (damager == damaged) {
                return;
            }

            PlayerFaction damagedFaction = Profile.getByUuid(damaged.getUniqueId()).getFaction();
            PlayerFaction damagerFaction = Profile.getByUuid(damager.getUniqueId()).getFaction();

            if (damagedFaction == null || damagerFaction == null) {
                return;
            }

            if (damagedFaction == damagerFaction) {
                damager.sendMessage(main.getLangConfig().getString("FACTION_OTHER.CANNOT_DAMAGE_FRIENDLY").replace("%PLAYER%", damaged.getName()));
                e.setCancelled(true);
                return;
            }

            if (damagedFaction.getAllies().contains(damagerFaction) && !main.getMainConfig().getBoolean("ALLIES.DAMAGE_ALLIES")) {
                damager.sendMessage(main.getLangConfig().getString("FACTION_OTHER.CANNOT_DAMAGE_ALLY").replace("%PLAYER%", damaged.getName()));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(player.getUniqueId());
        if (offlinePlayer != null) {
            offlinePlayer.setDeaths(offlinePlayer.getDeaths() + 1);
        }

        if (player.getKiller() != null) {
            SimpleOfflinePlayer killerOfflinePlayer = SimpleOfflinePlayer.getByUuid(player.getKiller().getUniqueId());
            if (killerOfflinePlayer != null) {
                killerOfflinePlayer.setKills(killerOfflinePlayer.getKills() + 1);
            }
        }

        PlayerFaction playerFaction = PlayerFaction.getByPlayer(player);
        if (playerFaction != null) {
            playerFaction.freeze(main.getMainConfig().getInt("FACTION_GENERAL.FREEZE_DURATION"));
            playerFaction.setDeathsTillRaidable(playerFaction.getDeathsTillRaidable().subtract(BigDecimal.ONE));
            playerFaction.sendMessage(main.getLangConfig().getString("ANNOUNCEMENTS.FACTION.PLAYER_DEATH").replace("%PLAYER%", player.getName()).replace("%DTR%", playerFaction.getDeathsTillRaidable() + "").replace("%MAX_DTR%", playerFaction.getMaxDeathsTillRaidable() + ""));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Claim claim = Claim.getProminentClaimAt(event.getEntity().getLocation());
            if (claim != null) {
                if (claim.getFaction() instanceof SystemFaction && !((SystemFaction) claim.getFaction()).isDeathban()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        Claim claim = Claim.getProminentClaimAt(event.getPlayer().getLocation());
        if (claim != null) {
            profile.setLastInside(claim);
        }
    }
}
