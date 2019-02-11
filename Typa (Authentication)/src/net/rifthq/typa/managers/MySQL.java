package net.rifthq.typa.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.rifthq.typa.Typa;

public class MySQL {
	static Typa plugin = Typa.getPlugin(Typa.class);

	public static boolean dataExists(UUID uuid, Player player) {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + MySQL.plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void registerPlayer(UUID uuid, Player player, String password, String ip) {
		try {
			if (MySQL.dataExists(uuid, player)) {
				
				//Player login data not found, print command to create pin.
				
				return;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("INSERT INTO " + MySQL.plugin.table + " (UUID, PASSWORD, IP) VALUES (?,?,?)");
			statement.setString(1, uuid.toString());
			statement.setString(2, password);
			statement.setString(3, ip.toString());
			statement.executeUpdate(); 
			
			//Set pin susfully.
			
			Typa.logged_in.add(player.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			//Couldn'tt set pin
			
		}
	}

	public static String getPlayerPassword(UUID uuid, Player player) {
		try {
			if (!dataExists(uuid, player)) {
				return null;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());

			ResultSet results = statement.executeQuery();
			results.first();
			return results.getString("PASSWORD");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getPlayerIP(UUID uuid, Player player) {
		try {
			if (!dataExists(uuid, player)) {
				return null;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + plugin.table + " WHERE IP=?");
			statement.setString(1, uuid.toString());

			ResultSet results = statement.executeQuery();
			results.first();
			return results.getString("IP");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void playerLogin(UUID uuid, Player player, String password) {
		if (!MySQL.dataExists(uuid, player)) {
			//not set password, print create message.
			return;
		}
		if (password.equals(MySQL.getPlayerPassword(uuid, player))) {
			Typa.logged_in.add(player.getName());
			
			//correct passwrord message
			
		} else {
			
			//incorrect passwword message.
			
		}
	}
}
