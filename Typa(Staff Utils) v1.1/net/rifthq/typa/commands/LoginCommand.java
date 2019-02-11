package net.rifthq.typa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.Typa;
import net.rifthq.typa.managers.MySQL;
import net.rifthq.typa.utils.Lists;

public class LoginCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("login")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only!");
				return true;
			}
			if (!player.hasPermission("typa.auth")) {
				player.sendMessage(ChatColor.RED + "You lack the correct permission(s) to use this command!");
				return true;
			}
			if (!(Lists.toLogin.contains(player.getName()))) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.register.already")));
				return true;
			}
			if (Lists.toLogin.contains(player.getName())) {
				if (args.length == 1) {
					MySQL.playerLogin(player.getUniqueId(), player, args[0]);
				} else {
					player.sendMessage(ChatColor.RED + "/login <pin>");
					return true;
				}
			}
		}
		return false;
	}
}