package net.rifthq.typa.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.Typa;
import net.rifthq.typa.utils.Lists;

public class MySQL {
	static Typa plugin = Typa.getPlugin(Typa.class);

	public static boolean dataExists(UUID uuid, Player player) {
		try {
			Typa.getInstance();
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + Typa.table + " WHERE UUID=?");
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
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.register.already")));
				return;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("INSERT INTO " + Typa.table + " (UUID, PASSWORD, IP) VALUES (?,?,?)");
			statement.setString(1, uuid.toString());
			statement.setString(2, password);
			statement.setString(3, ip.toString());
			statement.executeUpdate();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.register.autologin")));
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			Lists.toLogin.remove(player.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			player.sendMessage(
					ChatColor.RED + "Error whilst atempting to register, please contact a Server Administrator.");

		}
	}
	
	public static void setupTables() {
		try {
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("CREATE TABLE IF NOT EXISTS `" + Typa.table + "` (`ID` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, `UUID` varchar(50), `PASSWORD` varchar(50), `IP` varchar(50))");
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updatePlayer(UUID uuid, Player player, String ip, Player sender) {
		try {
			if (!dataExists(uuid, player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.resetip.notfound").replace("%player%", player.getName())));
				return;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("UPDATE '" + Typa.table + "' SET 'IP'=? WHERE 'UUID'=?");
			statement.setString(1, ip.toString());
			statement.setString(2, uuid.toString());
			statement.executeUpdate();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.resetip.successful").replace("%player%", player.getName())));
		} catch (SQLException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "Error whilst atempting to update player, please contact a Server Administrator.");
		}
	}

	public static String getPlayerPassword(UUID uuid, Player player) {
		try {
			if (!dataExists(uuid, player)) {
				return null;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("SELECT * FROM " + Typa.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());

			ResultSet results = statement.executeQuery();
			results.first();
			return results.getString("PASSWORD");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String deletePlayer(UUID uuid, Player player, Player sender) {
		try {
			if (!dataExists(uuid, player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.resetplayer.successful").replace("%player%", player.getName())));
				return null;
			}
			PreparedStatement statement = plugin.getConnection()
					.prepareStatement("DELETE FROM " + Typa.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			statement.executeUpdate();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.resetplayer.successful").replace("%player%", player.getName())));
			if (Lists.toLogin.contains(player)) {
				Lists.toLogin.remove(player);
				player.removePotionEffect(PotionEffectType.SLOW);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
						"&7&oAuthentication\n&cYou have been removed from our authentication system\n&c therefore you were kicked!"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			sender.sendMessage(
					ChatColor.RED + "Error whilst atempting to remove player, please contact a Server Administrator.");
		}
		return null;
	}

	public static void playerLogin(UUID uuid, Player player, String password) {
		if (!MySQL.dataExists(uuid, player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.login.notfound")));
			return;
		}
		if (password.equals(MySQL.getPlayerPassword(uuid, player))) {
			Lists.toLogin.remove(player.getName());
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			
			//player.sendMessage(ChatColor.GREEN + "Successfully authenticated as " + player.getName() + ".");
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.login.successful").replace("%player%", player.getName())));

		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.login.incorrect")));
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.BLINDNESS);
			
			player.kickPlayer(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.login.kickmessage")));

		}
	}
}
