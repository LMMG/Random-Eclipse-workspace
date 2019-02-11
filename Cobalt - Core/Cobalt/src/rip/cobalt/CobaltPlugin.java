package rip.cobalt;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.commands.OresCommand;
import rip.cobalt.commands.ProfileCommand;
import rip.cobalt.commands.essentials.commands.CobaltCommand;
import rip.cobalt.commands.essentials.commands.GameModeCommand;
import rip.cobalt.commands.essentials.commands.HelpCommand;
import rip.cobalt.commands.essentials.commands.RulesCommand;
import rip.cobalt.commands.essentials.commands.SaveCommand;
import rip.cobalt.commands.staff.Authentication;
import rip.cobalt.commands.staff.ChangePin;
import rip.cobalt.commands.staff.DeletePin;
import rip.cobalt.commands.staff.Register;
import rip.cobalt.commands.staff.SecurityCommand;
import rip.cobalt.commands.staff.Staffmode;
import rip.cobalt.database.MongoDB;
import rip.cobalt.listener.AntiAbuseListener;
import rip.cobalt.listener.AuthListener;
import rip.cobalt.listener.PlayerListeners;
import rip.cobalt.listener.StaffModeListener;
import rip.cobalt.profile.Join;

public class CobaltPlugin extends JavaPlugin implements Listener
{

	/**
	 * TODO: Fix Blacklist
	 * TODO: Code Report Command
	 */
	
	public static CobaltPlugin instance;
	public static FileConfiguration config;
	public static File conf;

	public void onEnable()
	{
		MongoDB db = new MongoDB();
		db.connect("127.0.0.1", 27017);
		instance = this;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Ketamine is being enabled 0.1-UNSTABLE");

		save();
		loadCommands();
		loadListeners();
		loadConfig();
	}

	public void onDisable()
	{
		instance = null;// for the skids
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.RED + "Dont Reload!");
		}
	}

	public void loadCommands()
	{
		getCommand("profile").setExecutor(new ProfileCommand());
		getCommand("ores").setExecutor(new OresCommand());
		getCommand("cobalt").setExecutor(new CobaltCommand());
		getCommand("Register").setExecutor(new Register());
		getCommand("login").setExecutor(new Authentication());
		getCommand("changepin").setExecutor(new ChangePin());
		getCommand("deletepin").setExecutor(new DeletePin());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("rules").setExecutor(new RulesCommand());
		getCommand("gamemode").setExecutor(new GameModeCommand());
		getCommand("save").setExecutor(new SaveCommand());
		getCommand("security").setExecutor(new SecurityCommand());
		getCommand("staffmode").setExecutor(new Staffmode());
	}

	public void loadListeners()
	{
		PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new Join(), this);
		manager.registerEvents(new PlayerListeners(), this);
		manager.registerEvents(new Authentication(), this);
		manager.registerEvents(new AuthListener(), this);
		manager.registerEvents(new AntiAbuseListener(), this);
		manager.registerEvents(new 	StaffModeListener(), this);
	}

	public void loadConfig()
	{
	    config = getConfig();
	    config.options().copyDefaults(true);
	    conf = new File(getDataFolder(), "config.yml");
	    saveConfig();
	    saveDefaultConfig();
	}

	public void save()
	{
		new BukkitRunnable()
		{

			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					MongoDB.getInstance().readPlayer(player);
					MongoDB.getInstance().updatesPlayer(player);
				}
			}
		}.runTaskTimerAsynchronously(this, 0, 0L);
	}

	public static CobaltPlugin getInstance()
	{
		return instance;
	}
	
	public void UpdateMethod(Player p) {
		MongoDB.getInstance().readPlayer(p);
		MongoDB.getInstance().updatesPlayer(p);
	}
	
	public void ReloadConfig() {
		this.reloadConfig();
	}
}
