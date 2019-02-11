package gg.vexus;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import gg.vexus.commands.ClearChatCommand;
import gg.vexus.commands.CopyInventoryCommand;
import gg.vexus.commands.LagCommand;
import gg.vexus.commands.ListCommand;
import gg.vexus.commands.ModModeCommand;
import gg.vexus.commands.RulesCommand;
import gg.vexus.commands.SlowchatCommand;
import gg.vexus.commands.StatsCommand;
import gg.vexus.commands.VanishCommand;
import gg.vexus.handler.DonatorBroadcastHandler;
import gg.vexus.handler.ModModeHandler;
import gg.vexus.handler.ServerHandler;
import gg.vexus.handler.VanishHandler;
import gg.vexus.listeners.BardStrength;
import gg.vexus.listeners.CommandBlocker;
import gg.vexus.listeners.InventoryListener;
import gg.vexus.profile.Profile;
import gg.vexus.utils.C;
import gg.vexus.utils.EnumUtil;

/**
 * Created by Owner on 02/07/2017.
 */

public class Core extends JavaPlugin implements Listener
{

	/**
	 * 
	 * TOOD: StatsCommand
	 * 
	 * Reformat Core
	 * 
	 */

	public static Core core;
	public static SlowchatCommand getSlowChatCommand;
	public static FileConfiguration config;
	public static File conf;

	@Override
	public void onEnable()
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Plugin instances...");
		core = this;

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Auth DB...");

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "");

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Managers...");
		autoBroadcast();
		DonatorBroadcastHandler.init();

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Setting Up Scoreboard...");

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading CorePlayerAPI...");

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Config...");
		config = this.getConfig();
		config.options().copyDefaults(true);
		conf = new File(this.getDataFolder(), "config.yml");
		this.saveConfig();
		this.saveDefaultConfig();

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading StaffMode Utils...");

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Commands...");
		getCommand("lag").setExecutor(new LagCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
		getCommand("mod").setExecutor(new ModModeCommand());
		getCommand("slowchat").setExecutor(new SlowchatCommand(this));
		getCommand("list").setExecutor(new ListCommand());
		getCommand("clearchat").setExecutor(new ClearChatCommand());
		getCommand("copyinv").setExecutor(new CopyInventoryCommand());
		getCommand("help").setExecutor(new gg.vexus.commands.HelpCommand());
		getCommand("rules").setExecutor(new RulesCommand());

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Listeners...");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		PluginManager m = Bukkit.getPluginManager();
		m.registerEvents(new VanishHandler(), this);
		m.registerEvents(new InventoryListener(), this);
		m.registerEvents(new ModModeHandler(), this);
		m.registerEvents(new DonatorBroadcastHandler(), this);
		m.registerEvents(new SlowchatCommand(this), this);
		m.registerEvents(new ServerHandler(), this);
		m.registerEvents(new BardStrength(), this);
		m.registerEvents(new CommandBlocker(), this);
		m.registerEvents(new Profile(), this);

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Core Saving Method...");
		new BukkitRunnable()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (player.hasPermission("common.vanish"))
					{
						player.sendMessage(ChatColor.GREEN + "Saving Core Data...");
						Bukkit.getWorld("world").save();
						Bukkit.dispatchCommand(player, "save-all");
						Bukkit.getConsoleSender().sendMessage("SAVED");
					}
				}
			}
		}.runTaskTimerAsynchronously(getCore(), 3600L, 3600L);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Core has loaded with no errors!...");
	}

	public void onDisable()
	{
		try
		{
			core = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public float convertMillisToMinutes(Long l)
	{
		return (l / 1000) / 60;
	}

	public float convertMillisToHours(Long l)
	{
		return convertMillisToMinutes(l) / 60;
	}

	public float convertMillisToDays(Long l)
	{
		return convertMillisToHours(l) / 24;
	}

	public static SlowchatCommand getGetSlowChatCommand()
	{
		return getSlowChatCommand;
	}

	public static void setGetSlowChatCommand(SlowchatCommand getSlowChatCommand)
	{
		Core.getSlowChatCommand = getSlowChatCommand;
	}

	public static Core getCore()
	{
		return core;
	}

	public void autoBroadcast()
	{
		String one = C.c("&7[&a&oTip&7] &7Support the KitMap and purchase in-game abilities &a@store.mist.gg!");
		String two = C.c("&7[&a&oTip&7] &7Want to see all the updates follow our twitter &a@MistHardcore");
		String three = C.c("&7[&a&oTip&7] &720% Sale during the come up to SOTW&a@ store.mist.gg");

		String[] strings = {
				one,
				two,
				three
		};

		new BukkitRunnable()
		{

			public void run()
			{
				String str = EnumUtil.getRandom(strings);
				Bukkit.broadcastMessage(str);
			}
		}.runTaskTimer(this, 20 * 60, 20 * 60);
	}
}