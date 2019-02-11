package com.parapvp.base;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.parapvp.base.command.CommandManager;
import com.parapvp.base.command.SimpleCommandManager;
import com.parapvp.base.command.module.ChatModule;
import com.parapvp.base.command.module.EssentialModule;
import com.parapvp.base.command.module.InventoryModule;
import com.parapvp.base.command.module.TeleportModule;
import com.parapvp.base.command.module.StaffMode.StaffMode;
import com.parapvp.base.command.module.essential.ListCommand;
import com.parapvp.base.command.module.essential.ReportCommand;
import com.parapvp.base.database.MySQL;
import com.parapvp.base.listener.ChatListener;
import com.parapvp.base.listener.ColouredSignListener;
import com.parapvp.base.listener.CommandBlockListener;
import com.parapvp.base.listener.DecreasedLagListener;
import com.parapvp.base.listener.JoinListener;
import com.parapvp.base.listener.NameVerifyListener;
import com.parapvp.base.listener.PlayerLimitListener;
import com.parapvp.base.listener.Rankbroadcast;
import com.parapvp.base.listener.StaffModeListener;
import com.parapvp.base.listener.StaffModeListener2;
import com.parapvp.base.manager.PlayTimeManager;
import com.parapvp.base.manager.ProtocolHook;
import com.parapvp.base.manager.ServerHandler;
import com.parapvp.base.task.AutoRestartHandler;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.ConsoleUser;
import com.parapvp.base.user.NameHistory;
import com.parapvp.base.user.ServerParticipator;
import com.parapvp.base.user.UserManager;
import com.parapvp.util.PersistableLocation;
import com.parapvp.util.RandomUtils;
import com.parapvp.util.SignHandler;
import com.parapvp.util.bossbar.BossBarManager;
import com.parapvp.util.chat.Lang;
import com.parapvp.util.cuboid.Cuboid;
import com.parapvp.util.cuboid.NamedCuboid;
import com.parapvp.util.itemdb.ItemDb;
import com.parapvp.util.itemdb.SimpleItemDb;

public class BasePlugin extends JavaPlugin implements Listener
{
	private static BasePlugin plugin;
	private ItemDb itemDb;
	private Random random = new Random();
	private RandomUtils randomUtils;
	private AutoRestartHandler autoRestartHandler;
	@SuppressWarnings("unused") private BukkitRunnable announcementTask;
	private CommandManager commandManager;
	private PlayTimeManager playTimeManager;
	private ServerHandler serverHandler;
	private SignHandler signHandler;
	private UserManager userManager;
	private StaffModeListener staffModeListener;
	private MySQL mysql;

	public static BasePlugin getPlugin()
	{
		return plugin;
	}

	public void onEnable()
	{
		plugin = this;

		mysql = new MySQL("localhost", "root", "masonc12", "base");
		mysql.createUsers();

		Rankbroadcast.enable();
		staffModeListener = new StaffModeListener();
		Bukkit.getServer().getPluginManager().registerEvents(staffModeListener, this);
		ConfigurationSerialization.registerClass(ServerParticipator.class);
		ConfigurationSerialization.registerClass(BaseUser.class);
		ConfigurationSerialization.registerClass(ConsoleUser.class);
		ConfigurationSerialization.registerClass(NameHistory.class);
		ConfigurationSerialization.registerClass(PersistableLocation.class);
		ConfigurationSerialization.registerClass(Cuboid.class);
		ConfigurationSerialization.registerClass(NamedCuboid.class);

		BossBarManager.hook();
		this.randomUtils = new RandomUtils();
		this.autoRestartHandler = new AutoRestartHandler(this);
		this.serverHandler = new ServerHandler(this);
		this.signHandler = new SignHandler(this);
		this.userManager = new UserManager(this);
		this.itemDb = new SimpleItemDb(this);
		try
		{
			Lang.initialize("en_US");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		PluginManager manager = this.getServer().getPluginManager();
		this.commandManager = new SimpleCommandManager(this);
		this.commandManager.registerAll(new ChatModule(this));
		this.commandManager.registerAll(new EssentialModule(this));
		this.commandManager.registerAll(new InventoryModule(this));
		this.commandManager.registerAll(new TeleportModule(this));
		this.getCommand("list").setExecutor(new ListCommand());
		getCommand("staff").setExecutor(new StaffMode());
		manager.registerEvents(new StaffModeListener2(), this);

		manager.registerEvents(new ChatListener(this), (Plugin) this);
		manager.registerEvents(new CommandBlockListener(), (Plugin) this);
		manager.registerEvents(new ColouredSignListener(), (Plugin) this);
		manager.registerEvents(new DecreasedLagListener(this), (Plugin) this);
		manager.registerEvents(new JoinListener(this), (Plugin) this);
		manager.registerEvents(new ReportCommand(), (Plugin) this);
		manager.registerEvents(new NameVerifyListener(this), (Plugin) this);
		this.playTimeManager = new PlayTimeManager(this);
		manager.registerEvents(new Rankbroadcast(), this);
		manager.registerEvents(this.playTimeManager, (Plugin) this);
		manager.registerEvents(new PlayerLimitListener(), (Plugin) this);
		Bukkit.getPluginManager().registerEvents((Listener) this, this);

		Plugin plugin = this.getServer().getPluginManager().getPlugin("ProtocolLib");
		if (plugin != null && plugin.isEnabled())
		{
			try
			{
				ProtocolHook.hook(this);
			}
			catch (Exception ex)
			{
				this.getLogger().severe("Error hooking into ProtocolLib from Base.");
				ex.printStackTrace();
			}
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				for (Player online : Bukkit.getOnlinePlayers())
				{
					if (mysql.isPlayerBanned(online.getName()))
					{
						String reason = mysql.getBannedReason(online.getName());
						String staff = mysql.getStaff(online.getName());
						String kickreason = "&7\n&cYou have been Blacklisted from the Cobalt Network\n&7This is a manual punishment by a Head Staff Member.\n&7Connect to ts.cobalt.rip if you wish to Appeal.\n&7".replaceAll("\\n", "\n");
						if (reason != null)
						{
							online.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickreason.replaceAll("%reason%", reason).replaceAll("%staff%", staff)));
						}
						else
						{
							online.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickreason.replaceAll("%reaso%", "No Reason Specified").replaceAll("%staff%", staff)));
						}
					}
				}
			}
		}, 50L, 50L);
	}

	public void onDisable()
	{
		super.onDisable();
		BossBarManager.unhook();

		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			if (getStaffModeListener().isStaffModeActive(player))
			{
				getStaffModeListener().setStaffMode(player, false);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour vanish was disabled due a Server Reload."));
			}
		}

		this.playTimeManager.savePlaytimeData();
		this.serverHandler.saveServerData();
		this.signHandler.cancelTasks(null);
		this.userManager.saveParticipatorData();
		plugin = null;
	}

	public RandomUtils getRandomUtils()
	{
		return this.randomUtils;
	}

	public Random getRandom()
	{
		return this.random;
	}

	public AutoRestartHandler getAutoRestartHandler()
	{
		return this.autoRestartHandler;
	}

	public CommandManager getCommandManager()
	{
		return this.commandManager;
	}

	public ItemDb getItemDb()
	{
		return this.itemDb;
	}

	public PlayTimeManager getPlayTimeManager()
	{
		return this.playTimeManager;
	}

	public StaffModeListener getStaffModeListener()
	{
		return staffModeListener;
	}

	public ServerHandler getServerHandler()
	{
		return this.serverHandler;
	}

	public SignHandler getSignHandler()
	{
		return this.signHandler;
	}

	public UserManager getUserManager()
	{
		return this.userManager;
	}

	public String extractNumber(String stripColor)
	{
		return null;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		if (mysql.isPlayerBanned(e.getPlayer().getName()) == true)
		{
			String reason = mysql.getBannedReason(e.getPlayer().getName());
			String staff = mysql.getStaff(e.getPlayer().getName());
			String kickreason = "&7\n&cYou have been Blacklisted from the Cobalt Network\n&7This is a manual punishment by a Head Staff Member.\n&7Connect to ts.hcriots.net if you wish to Appeal.\n&7".replaceAll("\\n", "\n");
			if (reason != null)
			{
				e.disallow(Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', kickreason.replaceAll("%reason%", reason).replaceAll("%staff%", staff)));
			}
			else
			{
				e.disallow(Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', kickreason.replaceAll("%reaso%", "No Reason Specified").replaceAll("%staff%", staff)));
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("blacklist"))
		{
			if (sender.hasPermission("base.blacklist"))
			{
				if (args.length < 2)
				{
					sender.sendMessage(ChatColor.RED + "Usage: /blacklist <player> <reason>");
					return true;
				}
				Player player = Bukkit.getServer().getPlayer(args[0]);

				StringBuilder reasonBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++)
				{
					reasonBuilder.append(args[i]).append(" ");
				}

				String reason = reasonBuilder.toString();

				if (mysql.isPlayerBanned(args[0]))
				{
					sender.sendMessage(ChatColor.RED + "Error: That player is already blacklisted!");
					return true;
				}

				String staff = sender.getName();
				String kickreason = "&7\n&cYou have been Blacklisted from the Cobalt Network\n&7This is a manual punishment by a Head Staff Member.\n&7Connect to ts.cobalt.net if you wish to Appeal.\n&7".replaceAll("\\n", "\n");
				sender.sendMessage(ChatColor.GREEN + "Grabbing profile and IP...");
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
				{
					public void run()
					{
						if (sender instanceof Player)
						{
							Player staff1 = (Player) sender;
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + args[0] + " &7has been successfully blacklisted!"));
							if (!reason.contains("-s"))
							{
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&l" + args[0] + " &cwas permanently blacklisted by &l" + staff + "&c!"));
							}
							else
							{
								reason.replaceAll("-s", "");
								Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&c&l" + args[0] + " &cwas permanently blacklisted by &l" + staff + "&c!"), "riots.staff");
							}
							mysql.banPlayer(args[0], reason, staff1.getName());
						}
						else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + args[0] + " &7has been successfully blacklisted!"));
							if (!reason.contains("-s"))
							{
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&l" + args[0] + " &cwas permanently blacklisted by &lConsole&c!"));
							}
							else
							{
								reason.replaceAll("-s", "");
								Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&c&l" + args[0] + " &cwas permanently blacklisted by &lConsole&c!"), "riots.staff");
							}
							mysql.banPlayer(args[0], reason, "Console");
						}
						if (player != null)
						{
							player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickreason.replaceAll("\\n", "\n")));
						}
					}
				}, 15L);
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "No permission.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("unblacklist"))
		{
			if (sender.hasPermission("riots.manager"))
			{
				if (args.length != 1)
				{
					sender.sendMessage(ChatColor.RED + "Usage: /unblacklist <player>");
					return true;
				}

				Player player = Bukkit.getPlayer(args[0]);

				if (!mysql.isPlayerBanned(args[0]))
				{
					sender.sendMessage(ChatColor.RED + "Error: That player is not blacklisted!");
					return true;
				}

				mysql.unbanPlayer(args[0]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + args[0] + " &7has been successfully unblacklisted!"));
				Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + sender.getName() + ChatColor.RED + " unblacklisted " + ChatColor.BOLD + args[0] + ChatColor.RED + "!");
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "No permission.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("checkblacklist"))
		{
			if (sender.hasPermission("riots.admin"))
			{
				if (args.length != 1)
				{
					sender.sendMessage(ChatColor.RED + "Usage: /checkblacklist <player>");
					return true;
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m--------------------------------"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[0] + "'s &7Blacklist: &f" + mysql.isPlayerBanned(args[0])));
				if (mysql.getBannedReason(args[0]) != null)
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l* &cReason: &f" + mysql.getBannedReason(args[0])));
				}
				else
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l* &cReason: &fNone"));
				}
				if (mysql.getStaff(args[0]) != null)
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l* &cStaff: &f" + mysql.getStaff(args[0])));
				}
				else
				{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l* &cStaff: &fN/A"));
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m--------------------------------"));
			}
		}
		return false;
	}

}
