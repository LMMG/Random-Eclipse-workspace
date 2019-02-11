package org.hqpots.bolt;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hqpots.bolt.scoreboard.ColorUtils;
import org.hqpots.bolt.scoreboard.ScoreboardHelper;
import org.hqpots.bolt.selector.PlayerEvents;
import org.hqpots.bolt.selector.ServerSelector;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Bolt extends JavaPlugin implements Listener, PluginMessageListener
{
	public static Team team;
	private static Bolt instance;
	public static FileConfiguration config;
	public static File conf;
	public static Object ghost;
	private final Map<Player, ScoreboardHelper> scoreboardHelperMap = new HashMap<>();

	@Override
	public void onEnable()
	{
		instance = this;

		setup();
		setupScoreboard();
		listeners();
	}

	public void setup()
	{
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			onPlayerJoin(player);
		}

		config = getConfig();
		config.options().copyDefaults(true);
		conf = new File(getDataFolder(), "config.yml");
		saveConfig();
		saveDefaultConfig();

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Fuck enabled.");
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	public void listeners()
	{
		PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new PlayerEvents(), this);
		manager.registerEvents(new ServerSelector(), this);
	}

	@Override
	public void onDisable()
	{
		instance = null;
	}

	private void onPlayerJoin(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (player.isOnline())
				{
					Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
					ScoreboardHelper scoreboardHelper = new ScoreboardHelper(scoreboard, new ColorUtils().translateFromString("&2&lMist 2.0 &7(Lobby)"));
					scoreboardHelperMap.put(player, scoreboardHelper);
				}
			}
		}.runTaskLater(this, 20L);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		onPlayerJoin(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		scoreboardHelperMap.remove(player);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player player = event.getPlayer();
		scoreboardHelperMap.remove(player);
	}

	@SuppressWarnings("deprecation")
	public String getRank(final Player player)
	{
		String prefix = new ColorUtils().translateFromString("");
		final PermissionUser permissionUser = PermissionsEx.getUser(player);
		for (final String s : permissionUser.getGroupNames())
		{
			@SuppressWarnings("unused")
			final String ranks = s;
			switch (s)
			{
				case "Owner":
				{
					prefix = "&4Owner";
					break;
				}
				case "Partner":
				{
					prefix = "&ePartner";
					break;
				}
				case "Developer":
				{
					prefix = "&bDeveloper";
					break;
				}
				case "Manager":
				{
					prefix = "&aManager";
					break;
				}
				case "Senior-admin":
				{
					prefix = "&cSenior-admin";
					break;
				}
				case "Admin":
				{
					prefix = "&cAdmin";
					break;
				}
				case "Mod+":
				{
					prefix = "&5Mod+";
					break;
				}
				case "Mod":
				{
					prefix = "&5Mod";
					break;
				}
				case "Trial-mod":
				{
					prefix = "&eTrial-Mod";
					break;
				}
				case "Silver":
				{
					prefix = "&7Silver";
					break;
				}
				case "Gold":
				{
					prefix = "&6Gold";
					break;
				}
				case "Diamond":
				{
					prefix = "&bDiamond";
					break;
				}
				case "Platinum":
				{
					prefix = "&fPlatinum";
					break;
				}
				case "Champion":
				{
					prefix = "&9Champion";
					break;
				}
				case "Mist":
				{
					prefix = "&aMist";
					break;
				}
				default:
				{
					prefix = "&fMember";
					break;
				}
			}
		}
		return prefix;
	}

	public void getCount(String server)
	{
		try
		{
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("PlayerCount");
			out.writeUTF(server);

			Bukkit.getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static int Online = 1;
	public static int practice = 0;
	public static int kitmap = 0;

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if (!channel.equals("BungeeCord")) { return; }
		try
		{
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String command = in.readUTF();

			if (command.equals("PlayerCount"))
			{
				String subchannel = in.readUTF();
				if (subchannel.equals("ALL"))
				{
					int playercount = in.readInt();
					Online = playercount;
				}
				if (subchannel.equals("practice"))
				{
					int playercount = in.readInt();
					practice = playercount;
				}
				if (subchannel.equals("kits"))
				{
					int playercount = in.readInt();
					kitmap = playercount;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setupScoreboard()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for (Map.Entry<Player, ScoreboardHelper> entry : scoreboardHelperMap.entrySet())
				{
					Player player = entry.getKey();
					ScoreboardHelper scoreboardHelper = entry.getValue();

					getCount("kits");
					getCount("ALL");
					getCount("practice");
					getCount("hcf");

					scoreboardHelper.clear();
					scoreboardHelper.add(new ColorUtils().translateFromString("&7&m--*------------------*----"));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&a&lPlayer Count"));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', " &eGlobal: &7" + Online));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', " &ePractice: &7" + practice));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', " &eKitMap: &7" + kitmap));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', " &eHCF: &7" + "0"));
					scoreboardHelper.add(" ");
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&a&lRank:"));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', getRank(player)));
					scoreboardHelper.add(" ");
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&astore.mist.gg"));
					scoreboardHelper.add(new ColorUtils().translateFromString("&7&m--*------------------*----"));
					scoreboardHelper.update(player);
				}
			}
		}.runTaskTimer(this, 0L, 3L);
	}

	public static Bolt getInstance()
	{
		return instance;
	}
}