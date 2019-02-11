package support.evocative;

import com.bizarrealex.aether.Aether;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import support.evocative.events.StaffEvent;
import support.evocative.listener.PlayerListeners;
import support.evocative.listener.PlayerloginKitListener;
import support.evocative.profile.Profile;
import support.evocative.profile.listener.ProfileListener;
import support.evocative.scoreboard.Provider;

import java.io.File;

/**
 * Created by Owner on 01/09/2017.
 */
public class ComboFFA extends JavaPlugin implements Listener {

    /**
     * TODO: PlayerLoginKillEvent - DONE
     * TODO: Fix profile - WORKING ON
     * TODO: Add Commands
     * TODO: Listeners
     */

    public static ComboFFA instance;
    public static FileConfiguration config;
    public static File conf;
    private Aether aether;

    @Override
    public void onEnable() {
        instance = this;
        setup();
        aether = new Aether(this, new Provider());

        config = this.getConfig();
        config.options().copyDefaults(true);
        conf = new File(this.getDataFolder(), "config.yml");
        this.saveConfig();
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        instance = null; // for skids
    }

    public static ComboFFA getInstance() {
        return instance;
    }

    public void setup() {
        //Commands

        //Listeners
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new Profile(), this);
        manager.registerEvents(new ProfileListener(), this);
        manager.registerEvents(new PlayerloginKitListener(), this);
        manager.registerEvents(new PlayerListeners(), this);
        manager.registerEvents(new Provider(), this);
        manager.registerEvents(new StaffEvent(), this);

        Bukkit.getPluginManager().registerEvents(this, this);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setMaximumNoDamageTicks(0);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        event.getPlayer().setMaximumNoDamageTicks(0);
    }
}
