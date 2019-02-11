package net.okaru.queue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.okaru.queue.jedis.QueuePublisher;
import net.okaru.queue.jedis.QueueSubscriber;
import net.okaru.queue.queue.Queue;
import net.okaru.queue.queue.QueueListeners;
import net.okaru.queue.queue.command.LeaveQueueCommand;
import net.okaru.queue.sign.SignListeners;
import net.okaru.queue.sign.SignType;
import net.okaru.queue.util.LocationSerialization;
import net.okaru.queue.util.command.CommandFramework;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import redis.clients.jedis.JedisPool;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class oQueue extends JavaPlugin implements Listener,
		PluginMessageListener {
	public String address;
	private QueueSubscriber subscriber;
	private QueuePublisher publisher;
	private boolean shuttingDown;
	private ConfigFile configFile;
	private Map<Location, SignType> signs;
	private boolean hub;
	private boolean offline;
	@SuppressWarnings("deprecation")
	private int totalPlayers = Bukkit.getOnlinePlayers().length;
	private Map<String, Integer> ranks;
	private CommandFramework commandFramework;
	private boolean connected;
	private JedisPool pool;

	@Override
	public void onEnable() {
		this.configFile = new ConfigFile(this, "config");
		this.address = this.configFile.getString("DATABASE.HOST");
		this.pool = new JedisPool(this.address);
		this.publisher = new QueuePublisher(this);
		if (this.configFile.getBoolean("HUB")) {
			this.subscriber = new QueueSubscriber(this);
			this.hub = true;
			this.signs = new HashMap<Location, SignType>();
			this.getServer().getMessenger()
					.registerOutgoingPluginChannel(this, "RedisBungee");
			this.getServer().getMessenger()
					.registerOutgoingPluginChannel(this, "BungeeCord");
			this.getServer().getMessenger()
					.registerIncomingPluginChannel(this, "RedisBungee", this);
			File file = new File(this.getDataFolder(), "signs.yml");
			if (file.exists()) {
				YamlConfiguration configuration = YamlConfiguration
						.loadConfiguration(file);
				if (configuration.contains("LOCATIONS")) {
					for (String locationString : configuration
							.getConfigurationSection("LOCATIONS")
							.getKeys(false)) {
						this.signs.put(LocationSerialization
								.deserializeLocation(configuration
										.getString("LOCATIONS."
												+ locationString)),
								SignType.JOIN);
					}
				}
				if (configuration.contains("INFO_LOCATIONS")) {
					for (String locationString : configuration
							.getConfigurationSection("INFO_LOCATIONS").getKeys(
									false)) {
						this.signs.put(LocationSerialization
								.deserializeLocation(configuration
										.getString("INFO_LOCATIONS."
												+ locationString)),
								SignType.INFO);
					}
				}
			}
			this.ranks = new HashMap<String, Integer>();
			this.commandFramework = new CommandFramework(this);
			for (final String rank : new ArrayList<String>(this.configFile
					.getConfiguration().getConfigurationSection("RANKS")
					.getKeys(false))) {
				this.ranks.put(rank,
						this.configFile.getInt("RANKS." + rank + ".PRIORITY"));
			}
			for (String key : this.configFile.getConfiguration()
					.getConfigurationSection("QUEUES").getKeys(false)) {
				new Queue(this, this.configFile.getString("QUEUES." + key
						+ ".NAME"), this.configFile.getString("QUEUES." + key
						+ ".SERVER"));
			}
			new LeaveQueueCommand(this);
			new BukkitRunnable() {

				@Override
				public void run() {
					for (Player player : Bukkit.getOnlinePlayers()) {
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("PlayerCount");
						out.writeUTF("ALL");
						player.sendPluginMessage(oQueue.this, "RedisBungee",
								out.toByteArray());
					}
				}
			}.runTaskTimerAsynchronously(this, 40, 40);
			new BukkitRunnable() {

				@Override
				public void run() {
					for (Queue queue : Queue.getQueues()) {
						for (Player player : queue.getPlayerSet()) {
							player.sendMessage(oQueue.this.configFile
									.getString("MESSAGES.POSITION")
									.replace(
											"%POSITION%",
											queue.getPlayers().get(
													player.getUniqueId())
													+ "")
									.replace("%TOTAL%",
											"" + queue.getData()[0] + "")
									.replace("%QUEUE%", queue.getName()));
							if (!oQueue.this.getRank(player).equalsIgnoreCase(
									"Member"))
								continue;
							player.sendMessage(oQueue.this.configFile
									.getString("MESSAGES.NO_RANK"));
						}
					}
				}
			}.runTaskTimerAsynchronously(this, 0, 160);
			new QueueListeners(this);
			new SignListeners(this);
		} else {
			final String server = this.configFile.getString("SERVER_NAME");
			new BukkitRunnable() {

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					oQueue.this.publisher.write(server + ";server_data;"
							+ Bukkit.getOnlinePlayers().length + ";"
							+ Bukkit.getMaxPlayers() + ";"
							+ Bukkit.hasWhitelist());
				}
			}.runTaskTimerAsynchronously(this, 20, 20);
		}
	}

	public int getPriority(Player player) {
		String rank = this.getRank(player);
		if (rank.equalsIgnoreCase("Member")) {
			return 100;
		}
		return this.ranks.get(rank);
	}

	public String getRank(Player player) {
		String currentRank = "Member";
		for (String rank : this.ranks.keySet()) {
			if (!player.hasPermission("rank." + rank))
				continue;
			if (!currentRank.equalsIgnoreCase("Member")
					&& !currentRank.equalsIgnoreCase(rank)) {
				if (this.ranks.get(rank) >= this.ranks.get(currentRank))
					continue;
				currentRank = rank;
				continue;
			}
			currentRank = rank;
		}
		return currentRank;
	}

	private void saveSignLocations() {
		File file = new File(this.getDataFolder(), "signs.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration configuration = YamlConfiguration
				.loadConfiguration(file);
		configuration.set("LOCATIONS", null);
		configuration.set("INFO_LOCATIONS", null);
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		for (Location location : this.getSigns().keySet()) {
			if (this.getSigns().get(location) == SignType.JOIN) {
				configuration.set("LOCATIONS.location" + ++i,
						LocationSerialization.serializeLocation(location));
				continue;
			}
			configuration.set("INFO_LOCATIONS.location" + ++i,
					LocationSerialization.serializeLocation(location));
		}
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		this.shuttingDown = true;
		this.saveSignLocations();
		if (!this.hub) {
			this.publisher.write(this.configFile.getString("SERVER_NAME")
					+ ";bye");
		}
		this.subscriber.getJedisPubSub().unsubscribe();
		this.pool.destroy();
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player,
			byte[] message) {
		String subchannel;
		ByteArrayDataInput in;
		if (channel.equalsIgnoreCase("RedisBungee")
				&& (subchannel = (in = ByteStreams.newDataInput(message))
						.readUTF()).equalsIgnoreCase("PlayerCount")) {
			in.readUTF();
			this.totalPlayers = in.readInt();
		}
	}

	public String getAddress() {
		return this.address;
	}

	public QueueSubscriber getSubscriber() {
		return this.subscriber;
	}

	public QueuePublisher getPublisher() {
		return this.publisher;
	}

	public boolean isShuttingDown() {
		return this.shuttingDown;
	}

	public ConfigFile getConfigFile() {
		return this.configFile;
	}

	public Map<Location, SignType> getSigns() {
		return this.signs;
	}

	public boolean isHub() {
		return this.hub;
	}

	public boolean isOffline() {
		return this.offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public int getTotalPlayers() {
		return this.totalPlayers;
	}

	public Map<String, Integer> getRanks() {
		return this.ranks;
	}

	public CommandFramework getCommandFramework() {
		return this.commandFramework;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public JedisPool getPool() {
		return this.pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

}
