/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.gnu.trove.map.TObjectLongMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap
 *  net.minecraft.util.gnu.trove.procedure.TObjectLongProcedure
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.MemorySection
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.parapvp.base.manager;

import com.parapvp.util.Config;
import java.util.Set;
import java.util.UUID;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import net.minecraft.util.gnu.trove.procedure.TObjectLongProcedure;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayTimeManager
implements Listener {
    private final TObjectLongMap<UUID> totalPlaytimeMap = new TObjectLongHashMap();
    private final TObjectLongMap<UUID> sessionTimestamps = new TObjectLongHashMap();
    private final Config config;

    public PlayTimeManager(JavaPlugin plugin) {
        this.config = new Config(plugin, "play-times");
        this.reloadPlaytimeData();
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.sessionTimestamps.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        this.totalPlaytimeMap.put(uuid, this.getTotalPlayTime(uuid));
        this.sessionTimestamps.remove(uuid);
    }

    public void reloadPlaytimeData() {
        Object object = this.config.get("playing-times");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection)object;
            for (Object id : section.getKeys(false)) {
                this.totalPlaytimeMap.put(UUID.fromString((String)id), this.config.getLong("playing-times." + (String)id, 0));
            }
        }
        long millis = System.currentTimeMillis();
        for (Player target : Bukkit.getOnlinePlayers()) {
            this.sessionTimestamps.put(target.getUniqueId(), millis);
        }
    }

    public void savePlaytimeData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.totalPlaytimeMap.put(player.getUniqueId(), this.getTotalPlayTime(player.getUniqueId()));
        }
        this.totalPlaytimeMap.forEachEntry((uuid, l) -> {
            this.config.set("playing-times." + uuid.toString(), l);
            return true;
        }
        );
        this.config.save();
    }

    public long getSessionPlayTime(UUID uuid) {
        long session = this.sessionTimestamps.get(uuid);
        return session != this.sessionTimestamps.getNoEntryValue() ? System.currentTimeMillis() - session : 0;
    }

    public long getPreviousPlayTime(UUID uuid) {
        long stamp = this.totalPlaytimeMap.get(uuid);
        return stamp == this.totalPlaytimeMap.getNoEntryValue() ? 0 : stamp;
    }

    public long getTotalPlayTime(UUID uuid) {
        return this.getSessionPlayTime(uuid) + this.getPreviousPlayTime(uuid);
    }
}

