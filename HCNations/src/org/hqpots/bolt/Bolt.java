package org.hqpots.bolt;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
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
import org.hqpots.bolt.commands.SetSpawnCommand;
import org.hqpots.bolt.scoreboard.ColorUtils;
import org.hqpots.bolt.scoreboard.ScoreboardHelper;
import org.hqpots.bolt.selector.PlayerEvents;
import org.hqpots.bolt.selector.ServerSelector;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Bolt extends JavaPlugin implements Listener, PluginMessageListener {
	public static Team team;

	private static Bolt instance;

	public static FileConfiguration config;
	public static File conf;

	public static Object ghost;
	PluginManager manager = Bukkit.getServer().getPluginManager();

	private final Map<Player, ScoreboardHelper> scoreboardHelperMap = new HashMap<>();

	public static Bolt getInstance() {
		if (instance == null) {
			instance = new Bolt();
		}
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Fuck enabled.");
		
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		setupScoreboard();

		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			onPlayerJoin(player);
		}

		config = getConfig();
		config.options().copyDefaults(true);
		conf = new File(getDataFolder(), "config.yml");
		saveConfig();
		saveDefaultConfig();

		getCommand("setspawn").setExecutor(new SetSpawnCommand(this));

		manager.registerEvents(new PlayerEvents(), this);
		manager.registerEvents(new ServerSelector(), this);

	}

	@Override
	public void onDisable() {
		instance = null;
	}
	
	public byte[] getOnlinePlayers() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF("ALL");
		return out.toByteArray();
	}

	private void onPlayerJoin(Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
					ScoreboardHelper scoreboardHelper = new ScoreboardHelper(scoreboard,
							new ColorUtils().translateFromString("&6&lHCNATIONS"));

					scoreboardHelperMap.put(player, scoreboardHelper);
				}
			}
		}.runTaskLater(this, 20L);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		onPlayerJoin(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		scoreboardHelperMap.remove(player);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		scoreboardHelperMap.remove(player);
	}

	public String getRank(final Player player) {
	      String prefix = new ColorUtils().translateFromString("");
	      final PermissionUser permissionUser = PermissionsEx.getUser(player);
	      for (final String s : permissionUser.getGroupNames()) {
	          final String ranks = s;
	          switch (s) {
	              case "Owner": {
	                  prefix = "&4Network-Owner";
	                  break;
	              }
	              case "Developer": {
	                  prefix = "&bDeveloper";
	                  break; 
	              }
	              case "Manager": {
	                  prefix = "&aManager";
	                  break; 
	              }
	              case "Senior-admin": {
	                  prefix = "&cSenior-admin";
	                  break; 
	              }
	              case "Admin": {
	                  prefix = "&cAdmin";
	                  break; 
	              }
	              case "Mod+": {
	                  prefix = "&5Mod+";
	                  break; 
	              }
	              case "Mod": {
	                  prefix = "&5Mod";
	                  break; 
	              }
	              case "Trial-mod": {
	                  prefix = "&eTrial-Mod";
	                  break; 
	              }
	              case "Silver": {
	                  prefix = "&7Silver";
	                  break; 
	              }
	              case "Gold": {
	                  prefix = "&6Gold";
	                  break; 
	              }
	              case "Diamond": {
	                  prefix = "&bDiamond";
	                  break; 
	              }
	              case "Platinum": {
	                  prefix = "&fPlatinum";
	                  break; 
	              }
	              case "Champion": {
	                  prefix = "&9Champion";
	                  break; 
	              }
	              case "Mist": {
	                  prefix = "&aMist";
	                  break; 
	              }
	              default: {
	                  prefix = "&7User";
	                  break;
	              }
	          }
	      }
	      return prefix;
	  }
	
    public static int Online = 1;
    public static int Kitmap = 0;
    public static int HCFactions = 0;
    public static int kitmap = 0;

    /**
     * Do not touch :d
     */

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String command = in.readUTF();

            if (command.equals("PlayerCount")) {
                String subchannel = in.readUTF();
                if (subchannel.equals("ALL")) {
                    int playercount = in.readInt();
                    Online = playercount;
                }
                if (subchannel.equals("hcf")) {
                    int playercount = in.readInt();
                    HCFactions = playercount;
                }
                if (subchannel.equals("kits")) {
                    int playercount = in.readInt();
                    kitmap = playercount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void setupScoreboard() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map.Entry<Player, ScoreboardHelper> entry : scoreboardHelperMap.entrySet()) {
					Player player = entry.getKey();
					ScoreboardHelper scoreboardHelper = entry.getValue();
					scoreboardHelper.clear();
					scoreboardHelper.add(new ColorUtils().translateFromString("&7&m------------------------"));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&6&LName:"));
					scoreboardHelper.add("&f" + player.getName());
					scoreboardHelper.add(" ");
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&6&lOnline Players:"));
					scoreboardHelper.add("&f" + Online);
					scoreboardHelper.add(" ");
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&6&lRank:"));
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', getRank(player)));
					scoreboardHelper.add(" ");
					scoreboardHelper.add(ChatColor.translateAlternateColorCodes('&', "&6hcnations.org"));
					scoreboardHelper.add(new ColorUtils().translateFromString("&7&m------------------------"));
					scoreboardHelper.update(player);
				}
			}
		}.runTaskTimer(this, 0L, 3L);
	}
}