package rip.hulk;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
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

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.com.google.common.io.ByteArrayDataInput;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;
import rip.hulk.command.HulkCommand;
import rip.hulk.listeners.PlayerEvents;
import rip.hulk.selector.ServerSelector;
import rip.hulk.utils.ScoreboardHelper;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Hulk extends JavaPlugin implements Listener, PluginMessageListener
{

	@Getter public static Hulk instance;
	private final Map<Player, ScoreboardHelper> scoreboardHelperMap = new HashMap<>();

	public void onEnable()
	{
		instance = this;

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Hulk] This Plugin is being enabled!");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(new ServerSelector(), this);
		manager.registerEvents(new PlayerEvents(), this);

		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			onPlayerJoin(player);
		}
		setupScoreboard();
		loadCommands();
	}

	public void onDisable()
	{
		instance = null;
	}

	public void loadCommands()
	{
		getCommand("hulk").setExecutor(new HulkCommand());
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
					ScoreboardHelper scoreboardHelper = new ScoreboardHelper(scoreboard, ChatColor.translateAlternateColorCodes('&', "&6&lHub"));
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

	@SuppressWarnings("deprecation")
	public String getRank(final Player player)
	{
		String prefix = ChatColor.translateAlternateColorCodes('&', "");
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
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', ("&7&m------------------------")));
					scoreboardHelper.add("§eOnline§7: " + Online);
					scoreboardHelper.add(" ");
					scoreboardHelper.add("§eRank§7: " + getRank(player));
					scoreboardHelper.add(" ");
					scoreboardHelper.add("§7store.cobalt.rip");
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', ("&7&m------------------------")));
					scoreboardHelper.update(player);
				}
			}
		}.runTaskTimer(this, 0L, 3L);
	}
}
