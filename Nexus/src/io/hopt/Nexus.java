package io.hopt;

import com.bizarrealex.aether.Aether;
import io.hopt.listener.event.PlayerEvents;
import io.hopt.scoreboard.NexusAdapter;
import io.hopt.selector.LobbySelector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import io.hopt.selector.ServerSelector;

import java.io.File;

public class Nexus extends JavaPlugin implements Listener {

    private static Nexus instance;
    public static FileConfiguration config;
    public static File conf;
    private Aether aether;

    public void onEnable() {
        instance = this;

        aether = new Aether(this, new NexusAdapter());

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Nexus - PlayerHub Has enabled - Any Bugs report in discord@ https://discord.gg/UMRxzCe");

        config = this.getConfig();
        config.options().copyDefaults(true);
        conf = new File(this.getDataFolder(), "config.yml");
        this.saveConfig();
        this.saveDefaultConfig();

        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new PlayerEvents(), this);
        manager.registerEvents(new ServerSelector(), this);
        manager.registerEvents(new NexusAdapter(), this);
        manager.registerEvents(new LobbySelector(), this);
    }

    public void onDisable() {
    }

    public static Nexus getInstance() {
        return instance;
    }
}
