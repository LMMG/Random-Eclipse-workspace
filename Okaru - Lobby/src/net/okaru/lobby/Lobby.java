package net.okaru.lobby;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.okaru.lobby.listeners.PlayerChangeEvent;
import net.okaru.lobby.listeners.PlayerDamageEvent;
import net.okaru.lobby.listeners.PlayerDropEvent;
import net.okaru.lobby.listeners.PlayerHungerChangeEvent;
import net.okaru.lobby.listeners.PlayerJoinServerEvent;
import net.okaru.lobby.listeners.PlayerLeaveEvent;
import net.okaru.lobby.listeners.PlayerMobEvent;
import net.okaru.lobby.listeners.PlayerMoveBelowEvent;
import net.okaru.lobby.listeners.PlayerTabEvent;
import net.okaru.lobby.listeners.ServerSelectorEvent;
import net.okaru.lobby.listeners.WeatherChangedEvent;
import net.okaru.lobby.scoreboard.ScoreboardProvider;
import net.okaru.lobby.scoreboard.ScoreboardWrapper;
import net.okaru.lobby.scoreboard.provider.LobbyScoreboard;
import net.okaru.lobby.server.Server;
import net.okaru.lobby.tab.TablistManager;
import net.okaru.lobby.utils.FileUtils;

public class Lobby extends JavaPlugin implements Listener,
		PluginMessageListener {

	private static Lobby instance;
	private ServerSelectorEvent serverSelectorListener;
    private PlayerJoinServerEvent playerJoinListener;
    private net.okaru.lobby.listeners.PlayerChangeEvent playerChangeEvent;
    private PlayerDamageEvent playerDamageEvent;
    private PlayerDropEvent playerDropEvent;
    private PlayerHungerChangeEvent playerHungerChangeEvent;
    private PlayerLeaveEvent playerLeaveEvent;
    private PlayerMoveBelowEvent playerMoveBelowEvent;
    private WeatherChangedEvent weatherChangedEvent;
    private PlayerMobEvent playerMobEvent;
	private int plc = 0;
	public static Lobby getInstance() {
		if (instance == null) {
			instance = new Lobby();
		}
		return instance;
	}

	public void onEnable() {
		this.setup();
	}

	public void onDisable() {
		instance = null;

	}

	private void setup() {
		instance = this;
		new ScoreboardWrapper(this, new LobbyScoreboard());
		this.serverSelectorListener = new ServerSelectorEvent();
		this.playerJoinListener = new PlayerJoinServerEvent();
		this.playerChangeEvent = new PlayerChangeEvent();
		this.playerDamageEvent = new PlayerDamageEvent();
		this.playerDropEvent = new PlayerDropEvent();
		this.playerHungerChangeEvent = new PlayerHungerChangeEvent();
		this.playerLeaveEvent = new PlayerLeaveEvent();
		this.playerMoveBelowEvent = new PlayerMoveBelowEvent();
		this.weatherChangedEvent = new WeatherChangedEvent();
		this.playerMobEvent = new PlayerMobEvent();
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerJoinListener, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.serverSelectorListener, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerJoinListener, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerChangeEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerDamageEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerDropEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerHungerChangeEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerLeaveEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerMoveBelowEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.weatherChangedEvent, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this.playerMobEvent, (Plugin)this);
        new TablistManager(this, new PlayerTabEvent(), TimeUnit.SECONDS.toMillis(5L));
		FileUtils.setupFiles();
		getServer().getMessenger().registerOutgoingPluginChannel(this,
				"BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this,
				"BungeeCord", this);
		getServer().getMessenger().registerIncomingPluginChannel(this,
				"RedisBungee", this);
		getServer().getMessenger().registerOutgoingPluginChannel(this,
				"RedisBungee");
		if (this.getConfig().contains("servers")) {
			for (String type : this.getConfig()
					.getConfigurationSection("servers").getKeys(false)) {
				String serverName = "servers." + type + ".serverName";
				new Server(FileUtils.getString(serverName));
			}
		}
		//Server.getByName("Kits").setMaxPlayers(350);
		new BukkitRunnable() {

			public void run() {
				for (Server servers : Server.getServers()) {
					if (servers == null)
						continue;
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						Server.getCount(player, "ALL");
						Server.getCount(player, servers.getName());
					}
				}
			}
		}.runTaskTimer((Plugin) this, 0, 40);

		this.getServer().getPluginManager()
				.registerEvents((Listener) this, (Plugin) this);
	}

	public void onPluginMessageReceived(String channel, Player player,
			byte[] message) {
		ByteArrayDataInput input;
		if (channel.equals("RedisBungee")
				&& ((input = ByteStreams.newDataInput((byte[]) message))
						.readUTF()).equals("PlayerCount")) {
			String serverName = input.readUTF();
			int playerCount = input.readInt();
			Server server = Server.getByName(serverName);
			if (server != null) {
				if (playerCount == -1) {
					server.setOffline(true);
				} else {
					server.setOnlinePlayers(playerCount);
				}
			} else if (serverName.equalsIgnoreCase("ALL")) {
				Server.globalPlayers = playerCount;
			}
		}
	}

	public int getCount(Player player, String server) {
		if (server == null) {
			server = "ALL";
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);
		player.sendPluginMessage(instance, "RedisBungee", out.toByteArray());
		return this.plc;
	}

	public FileConfigurationOptions getFileConfigurationOptions() {
		return this.getConfig().options();
	}
}