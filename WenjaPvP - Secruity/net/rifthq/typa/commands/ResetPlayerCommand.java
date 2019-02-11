package net.rifthq.typa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.Typa;
import net.rifthq.typa.managers.MySQL;

public class ResetPlayerCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("resetplayer")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only!");
				return true;
			}
			if (!player.hasPermission("typa.auth.admin")) {
				player.sendMessage(ChatColor.RED + "You lack the correct permission(s) to use this command!");
				return true;
			}
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "/resetplayer <player>");
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Player not found!");
				return true;
			}
			if (target == player) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.resetplayer.self")));
				return true;
			}
			if (args.length == 1) {
				MySQL.deletePlayer(target.getUniqueId(), target, player);
			} else {
				player.sendMessage(ChatColor.RED + "/resetplayer <player>");
				return true;
			}
		}
		return false;
	}
}