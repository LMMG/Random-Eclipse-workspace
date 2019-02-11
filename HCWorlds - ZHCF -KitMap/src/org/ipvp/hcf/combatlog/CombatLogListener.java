package org.ipvp.hcf.combatlog;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.ipvp.hcf.HCF;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.doctordark.util.InventoryUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CombatLogListener implements Listener {

    private static final int NEARBY_SPAWN_RADIUS = 64;

    private final Set<UUID> safelyDisconnected = new HashSet<>();
    private final Map<UUID, LoggerEntity> loggers = new HashMap<>();

    private final HCF plugin;

    public CombatLogListener(HCF plugin) {
        this.plugin = plugin;
    }

    /**
     * Disconnects a {@link Player} without a {@link LoggerEntity} spawning.
     *
     * @param player the {@link Player} to disconnect
     * @param reason the reason for disconnecting
     */
    public void safelyDisconnect(Player player, String reason) {
        if (safelyDisconnected.add(player.getUniqueId())) {
            player.kickPlayer(reason);
        }
    }

    public boolean removeCombatLogger(UUID uuid) {
        LoggerEntity entity = loggers.remove(uuid);
        if (entity != null) {
            entity.getBukkitEntity().setHealth(0);
            entity.destroy();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all the {@link LoggerEntity} instances from the server.
     */
    public void removeCombatLoggers() {
        Iterator<LoggerEntity> iterator = loggers.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
            iterator.remove();
        }

        safelyDisconnected.clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLoggerRemoved(LoggerRemovedEvent event) {
        loggers.remove(event.getLoggerEntity().getUniqueID());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        LoggerEntity currentLogger = loggers.remove(event.getPlayer().getUniqueId());
        if (currentLogger != null) {
            currentLogger.destroy();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        boolean result = safelyDisconnected.remove(uuid);
        if (!result && player.getGameMode() != GameMode.CREATIVE && !player.isDead()) {
            // Make sure the player hasn't already spawned a logger.
            if (loggers.containsKey(player.getUniqueId())) {
                return;
            }

            // Make sure the player is not in a safe-zone.
            Location location = player.getLocation();
            if (plugin.getFactionManager().getFactionAt(location).isSafezone()) {
                return;
            }

            // There is no enemies near the player, so don't spawn a logger.
            if (plugin.getSotwTimer().getSotwRunnable() != null || plugin.getTimerManager().getTeleportTimer().getNearbyEnemies(player, NEARBY_SPAWN_RADIUS) <= 0) {
                return;
            }

            LoggerEntity loggerEntity = new LoggerEntityHuman(player, location.getWorld());

            LoggerSpawnEvent calledEvent = new LoggerSpawnEvent(loggerEntity);
            Bukkit.getPluginManager().callEvent(calledEvent);
            if (!calledEvent.isCancelled()) {
                loggers.put(player.getUniqueId(), loggerEntity);

                // Call a tick later allowing for the NBT to save, the reason why
                // it is saved after the PlayerQuitEvent is so the plugins can modify
                // the inventory during this event.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Just double check the player hasn't
                        // logged off during this tick.
                        if (!player.isOnline()) {
                            loggerEntity.postSpawn(plugin);
                        }
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }
}
