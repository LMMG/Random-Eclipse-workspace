package rip.cobalt.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mongodb.DBObject;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.database.MongoDB;

public class ProfileCommand implements CommandExecutor {


	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Player player = (Player) cs;
		if (cmd.getName().equalsIgnoreCase("profile")) {
			if (args.length == 0) {
				loadStats(player);
				player.sendMessage(ChatColor.GREEN + "Showing " + player.getName() + "'s profile!");
				return false;
			}
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					target = (Player) Bukkit.getOfflinePlayer(args[0]);
				}
				loadStats(target);
			}
		}
		return false;
	}

	public void loadStats(Player player) {
		MongoDB.getInstance().readPlayer(player);
		MongoDB.getInstance().updatesPlayer(player);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Kills:&6 " + MongoDB.getInstance().kills));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Deaths:&6 " + MongoDB.getInstance().deaths));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Diamond:&6 " + MongoDB.getInstance().diamond));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Emerald:&6 " + MongoDB.getInstance().emerald));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Gold:&6 " + MongoDB.getInstance().gold));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Iron:&6 " + MongoDB.getInstance().iron));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Coal:&6 " + MongoDB.getInstance().coal));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Lapis:&6 " + MongoDB.getInstance().lapis));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Redstone:&6 " + MongoDB.getInstance().redstone));
	}
}
