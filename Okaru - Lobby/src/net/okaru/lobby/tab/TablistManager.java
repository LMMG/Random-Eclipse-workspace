
package net.okaru.lobby.tab;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

//Thank you to squirted for the SRC hookup
//Thank you to nigga that originally coded this, highkey ripped this outta here./

public class TablistManager
implements Listener {
    public static TablistManager INSTANCE;
    private final JavaPlugin plugin;
    private final Map<UUID, Tablist> tablists;
    @NonNull
    private TablistEntrySupplier supplier;
    private int updateTaskId = -1;

    public TablistManager(@NonNull JavaPlugin plugin, @NonNull TablistEntrySupplier supplier, long updateTime) {
        long remainder;
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier");
        }
        boolean startUpdater = true;
        if (INSTANCE != null) {
            int i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
            Bukkit.getLogger().warning("WARNING! AN INSTANCE OF TABLISTMANAGER ALREADY EXISTS!");
            Bukkit.getLogger().warning("IT IS RECOMMENDED TO ONLY USE ONE OTHERWISE IT CAN CAUSE FLICKERING AND OTHER ISSUES!");
            i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
        }
        if ((remainder = updateTime % 50) != 0) {
            updateTime -= remainder;
            Bukkit.getLogger().info("FIXING UPDATE TIME TO VALID TICK-COUNT...");
        }
        if (updateTime < 50) {
            startUpdater = false;
            int i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
            Bukkit.getLogger().warning("WARNING! TABLIST UPDATE TASK NOT STARTED!");
            Bukkit.getLogger().warning("REASON: UPDATE TIME IS TOO SHORT.");
            i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
        }
        INSTANCE = this;
        this.tablists = new ConcurrentHashMap<UUID, Tablist>();
        this.supplier = supplier;
        this.plugin = plugin;
        if (startUpdater) {
            this.updateTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)plugin, (Runnable)new TablistUpdateTask(), updateTime / 50, updateTime / 50).getTaskId();
        }
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.getTablist(player, true);
        }
        );
    }

    @Deprecated
    public Tablist getTablist(Player player) {
        return this.getTablist(player, false);
    }

    @Deprecated
    public Tablist getTablist(Player player, boolean create) {
        UUID uniqueId = player.getUniqueId();
        Tablist tablist = this.tablists.get(uniqueId);
        if (tablist == null && create) {
            tablist = new Tablist(player);
            this.tablists.put(uniqueId, tablist);
        }
        return tablist;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Tablist tablist = this.getTablist(player, true);
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() == this.plugin) {
            this.tablists.forEach((id, tablist) -> {
                tablist.hideFakePlayers().clear();
            }
            );
            this.tablists.clear();
            HandlerList.unregisterAll((Listener)this);
            if (this.updateTaskId != -1) {
                Bukkit.getScheduler().cancelTask(this.updateTaskId);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Tablist tablist = this.tablists.remove(uniqueId);
        if (tablist != null) {
            tablist.hideFakePlayers().clear();
        }
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void setSupplier(@NonNull TablistEntrySupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier");
        }
        this.supplier = supplier;
    }

    @NonNull
    public TablistEntrySupplier getSupplier() {
        return this.supplier;
    }
}

