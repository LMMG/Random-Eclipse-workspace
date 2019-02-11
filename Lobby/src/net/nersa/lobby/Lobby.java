package net.nersa.lobby;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;
import net.nersa.lobby.listeners.ListenerManager;
import net.nersa.lobby.scoreboard.ScoreboardHelper;
import net.nersa.lobby.utils.ColorUtils;
import net.nersa.lobby.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Lobby extends JavaPlugin implements Listener, PluginMessageListener {
    private static Lobby instance;
    public static FileConfiguration config;
    public static File conf;
    private final Map<Player, ScoreboardHelper> scoreboardHelperMap = new HashMap<>();
    @SuppressWarnings("unused")
	private ListenerManager listenerManager;
    public static int Online = 1;
    public static int HCFactions = 0;
    public static int kitmap = 0;
    public static Permission perms = null;

    public static Lobby getInstance() {
        if (instance == null) {
            instance = new Lobby();
        }
        return instance;
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onEnable() {
        instance = this;
        setupPermissions();
        this.listenerManager = new ListenerManager(this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        setupScoreboard();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            onJoinPlayer(player);
        }

        config = getConfig();
        config.options().copyDefaults(true);
        conf = new File(getDataFolder(), "config.yml");
        saveConfig();
        saveDefaultConfig();
    }

    @SuppressWarnings("deprecation")
	public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        	/***
        	 * Make a message for the reload Kick / Server Shutting down 
        	 */
        	
        	player.kickPlayer(" ");
        }
    }

    public void onJoinPlayer(Player player)
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
                    String title = ChatColor.translateAlternateColorCodes('&', "&6&lNersa [Hub-01]");
                    ScoreboardHelper scoreboardHelper = new ScoreboardHelper(scoreboard, title);

                    scoreboardHelperMap.put(player, scoreboardHelper);
                }
            }
        }.runTaskLater(this, 0L);
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
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public void setupScoreboard() {
        new BukkitRunnable() {
            @SuppressWarnings("unused")
			@Override
            public void run() {
                for (Map.Entry<Player, ScoreboardHelper> entry : scoreboardHelperMap.entrySet()) {
                    Player player = entry.getKey();
                    ScoreboardHelper scoreboardHelper = entry.getValue();
                    getCount("ALL");
                    getCount("HCF");
                    getCount("Kitmap");
                    Player p = (Player) Bukkit.getPlayer(getName());

                    scoreboardHelper.clear();
                    scoreboardHelper.clear();
                    scoreboardHelper.add(new ColorUtils().translateFromString("&7&m---*------------------*---"));
                    scoreboardHelper.add(new ColorUtils().translateFromString("&6Online&7:"));
                    scoreboardHelper.add("&c " + Online);
                    scoreboardHelper.add(" ");
                    scoreboardHelper.add(new ColorUtils().translateFromString("&6Player&7:"));
                    scoreboardHelper.add("&c " + player.getName());
                    scoreboardHelper.add(" ");
                    scoreboardHelper.add(new ColorUtils().translateFromString("&6Rank&7:"));
                    scoreboardHelper.add("&c" + StringUtils.capitalization(Lobby.getPermissions().getPrimaryGroup(player)));         
                    scoreboardHelper.add(new ColorUtils().translateFromString("&7&m---*------------------*---"));
                    scoreboardHelper.update(player);
                    scoreboardHelper.update(player);
                }
            }
        }.runTaskTimer(this, 0L, 3L);
    }

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
                if (subchannel.equals("HCFactions")) {
                    int playercount = in.readInt();
                    HCFactions = playercount;
                }
                if (subchannel.equals("KitMap")) {
                    int playercount = in.readInt();
                    kitmap = playercount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCount(String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(server);

            Bukkit.getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Permission getPermissions() {
        return perms;
    }
}