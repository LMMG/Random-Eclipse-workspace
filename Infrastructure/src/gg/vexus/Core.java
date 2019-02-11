package gg.vexus;

import static gg.vexus.utils.EnumUtil.getRandom;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import gg.vexus.commands.ClearChatCommand;
import gg.vexus.commands.CopyInventoryCommand;
import gg.vexus.commands.LagCommand;
import gg.vexus.commands.ListCommand;
import gg.vexus.commands.LogoutCommand;
import gg.vexus.commands.ModModeCommand;
import gg.vexus.commands.PlayTimeCommand;
import gg.vexus.commands.PvpTimerCommand;
import gg.vexus.commands.RenameCommand;
import gg.vexus.commands.ReportCommand;
import gg.vexus.commands.RulesCommand;
import gg.vexus.commands.SlowchatCommand;
import gg.vexus.commands.VanishCommand;
import gg.vexus.commands.essentials.commands.BroadcastCommand;
import gg.vexus.commands.essentials.commands.ClearCommand;
import gg.vexus.commands.essentials.commands.EnchantCommand;
import gg.vexus.commands.essentials.commands.FlyCommand;
import gg.vexus.commands.essentials.commands.GamemodeCommand;
import gg.vexus.commands.essentials.commands.HelpCommand;
import gg.vexus.commands.essentials.commands.InvseeCommand;
import gg.vexus.commands.essentials.commands.PingCommand;
import gg.vexus.commands.essentials.commands.SkullCommand;
import gg.vexus.commands.essentials.commands.SpawnerCommand;
import gg.vexus.commands.essentials.commands.SudoCommand;
import gg.vexus.commands.essentials.commands.TeleportCommand;
import gg.vexus.commands.essentials.commands.TeleporthereCommand;
import gg.vexus.commands.essentials.commands.TopCommand;
import gg.vexus.commands.essentials.commands.WorldCommand;
import gg.vexus.commands.essentials.commands.pm.MessageCommand;
import gg.vexus.commands.pin.LoginCommand;
import gg.vexus.commands.pin.PinCommand;
import gg.vexus.events.PlayerListeners;
import gg.vexus.handler.DonatorBroadcastHandler;
import gg.vexus.handler.ModModeHandler;
import gg.vexus.handler.VanishHandler;
import gg.vexus.listeners.AntiAbuseListeners;
import gg.vexus.listeners.AutoSmeltListener;
import gg.vexus.listeners.BardStrength;
import gg.vexus.listeners.BookDeenchantListener;
import gg.vexus.listeners.BookExploit;
import gg.vexus.listeners.CommandBlocker;
import gg.vexus.listeners.ElevatorSign;
import gg.vexus.listeners.EntityLimitListener;
import gg.vexus.listeners.FoundDiamondsListener;
import gg.vexus.listeners.HeadInfo;
import gg.vexus.listeners.HitDetectionListener;
import gg.vexus.listeners.InventoryListener;
import gg.vexus.listeners.KitMapSigns;
import gg.vexus.listeners.LootingListener;
import gg.vexus.listeners.PinListener;
import gg.vexus.listeners.StaffJoinListeners;
import gg.vexus.listeners.UnRepairableListener;
import gg.vexus.listeners.faction.BorderListener;
import gg.vexus.listeners.faction.WorldListener;
import gg.vexus.profile.Profile;
import gg.vexus.scoreboard.FixedColor;
import gg.vexus.scoreboard.ScoreboardHandler;
import gg.vexus.scoreboard.TabColors;
import gg.vexus.utils.C;
import gg.vexus.utils.ConfigFile;
import gg.vexus.utils.PlayerTimer;
import lombok.Getter;

/**
 * Created by Owner on 02/07/2017.
 */

public class Core extends JavaPlugin implements Listener
{

	public static Core core;
	public static SlowchatCommand getSlowChatCommand;
	public static FileConfiguration config;
	public static File conf;
	@Getter private ScoreboardHandler scoreboardHandler;
	public static ConfigFile configFile;
	private File reclaimFile;
	private FileConfiguration reclaimConfig;

	@Override
	public void onEnable()
	{
		core = this;

		listeners();
		autoBroadcast();
		DonatorBroadcastHandler.init();
		setupFiles();
		command();
		setup();
		setupTimers();
		essentials();
	}

	public void setupTimers()
	{
		PlayerTimer.createCooldown("report");
	}

	public void setup()
	{
		config = this.getConfig();
		config.options().copyDefaults(true);
		conf = new File(this.getDataFolder(), "config.yml");
		saveConfig();
		saveDefaultConfig();
		this.configFile = new ConfigFile(this, "config");

		this.scoreboardHandler = new ScoreboardHandler(this);

		new BukkitRunnable()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				for (Player player : Bukkit.getServer().getOnlinePlayers())
				{
					if (player.hasPermission("common.vanish"))
					{
						player.sendMessage(ChatColor.GREEN + "Saving Core Data...");
						Bukkit.getWorld("world").save();
					}
				}
			}
		}.runTaskTimerAsynchronously(getCore(), 3600L, 3600L);
	}

	public void essentials()
	{
		getCommand("gamemode").setExecutor(new GamemodeCommand());
		getCommand("broadcast").setExecutor(new BroadcastCommand());
		getCommand("clear").setExecutor(new ClearCommand());
		getCommand("teleporthere").setExecutor(new TeleporthereCommand());
		getCommand("teleport").setExecutor(new TeleportCommand());
		getCommand("top").setExecutor(new TopCommand());
		getCommand("sudo").setExecutor(new SudoCommand());
		getCommand("spawner").setExecutor(new SpawnerCommand());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("world").setExecutor(new WorldCommand());
		getCommand("skull").setExecutor(new SkullCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("enchant").setExecutor(new EnchantCommand());
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("reply").setExecutor(new MessageCommand());
	}

	public void command()
	{
		getCommand("lag").setExecutor(new LagCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
		getCommand("mod").setExecutor(new ModModeCommand());
		getCommand("slowchat").setExecutor(new SlowchatCommand(this));
		getCommand("list").setExecutor(new ListCommand());
		getCommand("clearchat").setExecutor(new ClearChatCommand());
		getCommand("pin").setExecutor(new LoginCommand());
		getCommand("setpin").setExecutor(new PinCommand());
		getCommand("copyinv").setExecutor(new CopyInventoryCommand());
		getCommand("rules").setExecutor(new RulesCommand());
		getCommand("rename").setExecutor(new RenameCommand());
		getCommand("report").setExecutor(new ReportCommand());
		getCommand("logout").setExecutor(new LogoutCommand(this));
		getCommand("pvptimer").setExecutor(new PvpTimerCommand(this));
		getCommand("playtime").setExecutor(new PlayTimeCommand());
	}

	public void listeners()
	{
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
		m.registerEvents(new FixedColor(), this);
		m.registerEvents(new TabColors(), this);
		m.registerEvents(new AntiAbuseListeners(), this);
		m.registerEvents(new BookExploit(), this);
		m.registerEvents(new KitMapSigns(), this);
		m.registerEvents(new StaffJoinListeners(), this);
		m.registerEvents(new WorldListener(this), this);
		m.registerEvents(new BorderListener(), this);
		m.registerEvents(new ElevatorSign(), this);
		m.registerEvents(new AutoSmeltListener(), this);
		m.registerEvents(new FoundDiamondsListener(), this);
		m.registerEvents(new LootingListener(), this);
		m.registerEvents(new HeadInfo(), this);
		m.registerEvents(new UnRepairableListener(), this);
		m.registerEvents(new HitDetectionListener(), this);
		m.registerEvents(new EntityLimitListener(), this);
		m.registerEvents(new BookDeenchantListener(), this);
		m.registerEvents(new PinListener(), this);
	}

	public void onDisable()
	{
		core = null; // for the skids
		this.scoreboardHandler.clearBoards();
	}

	public static Core getCore()
	{
		return core;
	}

	public void autoBroadcast()
	{
		String one = C.c("&7[&a&oTip&7] &7Support the server and purchase in-game items &a@ store.mist.gg!");
		String two = C.c("&7[&2&oTip&7] &7Giveaway on twitter for &AMinecon Cape &7 and &AMsit Rank &7At &ahttps://twitter.com/MistHardcore");
		String three = C.c("&7[&a&oTip&7] &720% Sale during the come up to SOTW &a@ store.mist.gg");

		String[] strings = {
				one,
				two,
				three
		};

		new BukkitRunnable()
		{

			public void run()
			{
				String str = getRandom(strings);
				Bukkit.broadcastMessage(str);
			}
		}.runTaskTimer(this, 20 * 60, 20 * 60);
	}

	public FileConfigurationOptions getFileConfigurationOptions()
	{
		return getConfig().options();
	}

	private void setupFiles()
	{
		try
		{
			if (!getDataFolder().exists())
			{
				getDataFolder().mkdirs();
			}
			File file = new File(getDataFolder().getAbsolutePath(), "config.yml");
			File file2 = new File(getDataFolder().getAbsolutePath(), "pin.yml");
			File file3 = new File(getDataFolder().getAbsolutePath(), "fails.yml");
			File file4 = new File(getDataFolder().getAbsoluteFile(), "notes.yml");
			if (!file.exists())
			{
				getFileConfigurationOptions().copyDefaults(true);
				saveConfig();
			}
			else
			{}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void createPinFile()
	{
		try
		{
			FileConfiguration pin = YamlConfiguration.loadConfiguration(new File(getCore().getDataFolder(), "pin.yml"));
			File pinFile = new File(getCore().getDataFolder(), "pin.yml");
			pin.save(pinFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createFailedFile()
	{
		try
		{
			FileConfiguration fail = YamlConfiguration.loadConfiguration(new File(getCore().getDataFolder(), "fails.yml"));
			File failFile = new File(getCore().getDataFolder(), "fails.yml");
			fail.save(failFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ScoreboardHandler getScoreboardHandler()
	{
		return scoreboardHandler;
	}
}