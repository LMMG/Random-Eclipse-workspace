package gg.vexus;

import gg.vexus.commands.*;
import gg.vexus.commands.pin.LoginCommand;
import gg.vexus.commands.pin.PinCommand;
import gg.vexus.events.PlayerListeners;
import gg.vexus.handler.DonatorBroadcastHandler;
import gg.vexus.handler.ModModeHandler;
import gg.vexus.handler.VanishHandler;
import gg.vexus.listeners.BardStrength;
import gg.vexus.listeners.CommandBlocker;
import gg.vexus.listeners.InventoryListener;
import gg.vexus.profile.Profile;
import gg.vexus.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static gg.vexus.utils.EnumUtil.getRandom;

/**
 * Created by Owner on 02/07/2017.
 */

public class Core extends JavaPlugin implements Listener {

	/**
	 * TOOD: StatsCommand
	 * <p>
	 * Reformat Core
	 */

	public static Core core;
	public static SlowchatCommand getSlowChatCommand;
	public static FileConfiguration config;
	public static File conf;


	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Plugin instances...");
		core = this;

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "");

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Managers...");
		autoBroadcast();
		DonatorBroadcastHandler.init();
		setupFiles();

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
		getCommand("pin").setExecutor(new LoginCommand());
		getCommand("setpin").setExecutor(new PinCommand());
		getCommand("copyinv").setExecutor(new CopyInventoryCommand());
		getCommand("help").setExecutor(new gg.vexus.commands.HelpCommand());
		getCommand("rules").setExecutor(new RulesCommand());
		getCommand("rename").setExecutor(new RenameCommand());

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Loading Listeners...");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		PluginManager m = Bukkit.getPluginManager();
		m.registerEvents(new VanishHandler(), this);
		m.registerEvents(new InventoryListener(), this);
		m.registerEvents(new ModModeHandler(), this);
		m.registerEvents(new DonatorBroadcastHandler(), this);
		m.registerEvents(new SlowchatCommand(this), this);
		m.registerEvents(new Profile(), this);
		m.registerEvents(new LoginCommand(), this);
		m.registerEvents(new BardStrength(), this);
		m.registerEvents(new CommandBlocker(), this);
		m.registerEvents(new PlayerListeners(), this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Core has loaded with no errors!...");

		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.hasPermission("common.vanish")) {
						player.sendMessage(ChatColor.GREEN + "Saving Core Data...");
						Bukkit.getWorld("world").save();
					}
				}
			}
		}.runTaskTimerAsynchronously(getCore(), 3600L, 3600L);
	}

	public void onDisable() {
		try {
			core = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SlowchatCommand getGetSlowChatCommand() {
		return getSlowChatCommand;
	}

	public static void setGetSlowChatCommand(SlowchatCommand getSlowChatCommand) {
		Core.getSlowChatCommand = getSlowChatCommand;
	}

	public static Core getCore() {
		return core;
	}

	public void autoBroadcast() {
		String one = C.c("&7[&a&oTip&7] &7Support the server and purchase in-game items &a@ store.mist.gg!");
		String two = C.c("&7[&a&oTip&7] &7Want to see all the updates follow our twitter &a@MistHardcore");
		String three = C.c("&7[&a&oTip&7] &720% Sale during the come up to SOTW &a@ store.mist.gg");

		String[] strings = {one, two, three};

		new BukkitRunnable() {

			public void run() {
				String str = getRandom(strings);
				Bukkit.broadcastMessage(str);
			}
		}.runTaskTimer(this, 20 * 60, 20 * 60);
	}

	public FileConfigurationOptions getFileConfigurationOptions() {
		return getConfig().options();
	}

	private void setupFiles() {
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdirs();
			}
			File file = new File(getDataFolder().getAbsolutePath(), "config.yml");
			File file2 = new File(getDataFolder().getAbsolutePath(), "pin.yml");
			File file3 = new File(getDataFolder().getAbsolutePath(), "fails.yml");
			File file4 = new File(getDataFolder().getAbsoluteFile(), "notes.yml");
			File file5 = new File(getDataFolder(), "reclaim.yml");
			if (!file.exists()) {
				getFileConfigurationOptions().copyDefaults(true);
				saveConfig();
			} else {
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void createPinFile() {
		try {
			FileConfiguration pin = YamlConfiguration.loadConfiguration(new File(getCore().getDataFolder(), "pin.yml"));
			File pinFile = new File(getCore().getDataFolder(), "pin.yml");
			pin.save(pinFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createFailedFile() {
		try {
			FileConfiguration fail = YamlConfiguration
					.loadConfiguration(new File(getCore().getDataFolder(), "fails.yml"));
			File failFile = new File(getCore().getDataFolder(), "fails.yml");
			fail.save(failFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onMoveBeforeLoggedIn(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName())) {
			e.setTo(e.getFrom());
			p.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onChatBeforeLoggedIn(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName())) {
			e.setCancelled(true);
			p.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onCommandBeforeLoggedIn(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if ((!e.getMessage().startsWith("/pin")) && (LoginCommand.loggingin.contains(p.getName()))) {
			e.setCancelled(true);
			p.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}
}
