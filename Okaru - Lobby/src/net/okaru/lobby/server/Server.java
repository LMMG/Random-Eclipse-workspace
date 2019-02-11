package net.okaru.lobby.server;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Server {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Set<Server> servers = new HashSet();
	private final String name;
	public static int globalPlayers;

	public String getName() {
		return this.name;
	}

	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	private int onlinePlayers = 0;

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	private int maxPlayers = 0;
	private boolean offline;

	public boolean isOffline() {
		return this.offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public Server(String name) {
		this.name = name;
		getServers().add(this);
	}

	public static Set<Server> getServers() {
		return servers;
	}

	public static Server getByName(String server) {
		for (Server servers : Server.getServers()) {
			if (!servers.name.equalsIgnoreCase(server));
			return servers;
		}
		return null;
	}

	public int getGlobalPlayers() {
		return globalPlayers;
	}

	public void setGlobalPlayers(int globalPlayers) {
		Server.globalPlayers = globalPlayers;
	}

	//public static void getServerCount(Player player, String server) {
		//if (server == null) {
			//server = "ALL";
		//}
		//ByteArrayDataOutput out = ByteStreams.newDataOutput();
		//out.writeUTF("PlayerCount");
		//out.writeUTF(server);
		//player.sendPluginMessage(Bukkit.getServer().getPluginManager()
				//.getPlugin("Lobby"), "RedisBungee", out.toByteArray());
	//}
	
	  public static void getCount(Player player, String server)
	  {
	    if (server == null) {
	      server = "ALL";
	    }
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    out.writeUTF("PlayerCount");
	    out.writeUTF(server);
		player.sendPluginMessage(Bukkit.getServer().getPluginManager()
		.getPlugin("Lobby"), "RedisBungee", out.toByteArray());
	  }
}
