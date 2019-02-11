package rip.evocative;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import rip.evocative.command.commands.BroadcastCommand;
import rip.evocative.command.commands.EnderChestCommand;
import rip.evocative.command.commands.InvseeCommand;
import rip.evocative.command.commands.SpawnerCommand;
import rip.evocative.command.commands.SudoCommand;
import rip.evocative.listener.AntiEnderChestListener;
import rip.evocative.listener.FoundDiamondsListener;
import rip.evocative.listener.HeadInfoListener;
import rip.evocative.listener.PlayerCounterListener;
import rip.evocative.listener.SignColorsListener;

public class Essentials extends JavaPlugin implements Listener
{

	@Getter public static Essentials instance;
	public static FileConfiguration config;
	public static File conf;
	public File serverDataFile;
	public FileConfiguration serverData;
	
	public void onEnable()
	{
		instance = this;

		loadListeners();
		loadCommands();
		save();
		Config();
	}

	public void onDisable()
	{
		save();

		instance = null;
	}

	public void Config()
	{
		config = this.getConfig();
		config.options().copyDefaults(true);
		conf = new File(this.getDataFolder(), "config.yml");
		this.saveConfig();
		this.saveDefaultConfig();
		
		serverDataFile = new File(this.getDataFolder(), "data.yml");
		serverData = YamlConfiguration.loadConfiguration(this.serverDataFile);
	}

	public void save()
	{
		new BukkitRunnable()
		{

			@Override
			public void run()
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					Config();
					player.sendMessage(ChatColor.GREEN + "Saved core...");
				}
			}
		}.runTaskTimer(this, 6000L, 6000L);
	}

	public void loadListeners()
	{
		PluginManager pluginManager = getServer().getPluginManager();

		pluginManager.registerEvents(new AntiEnderChestListener(), this);
		pluginManager.registerEvents(new HeadInfoListener(), this);
		pluginManager.registerEvents(new SignColorsListener(), this);
		pluginManager.registerEvents(new FoundDiamondsListener(), this);
		pluginManager.registerEvents(new PlayerCounterListener(), this);
	}

	public void loadCommands()
	{
		getCommand("enderchest").setExecutor(new EnderChestCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		getCommand("sudo").setExecutor(new SudoCommand());
		getCommand("broadcast").setExecutor(new BroadcastCommand());
		getCommand("spawner").setExecutor(new SpawnerCommand());
	}
}
