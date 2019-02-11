package net.rifthq.typa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.Typa;
import net.rifthq.typa.managers.MySQL;

public class RegisterCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("register")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only!");
				return true;
			}
			if (!player.hasPermission("typa.auth")) {
				player.sendMessage(ChatColor.RED + "You lack the correct permission(s) to use this command!");
				return true;
			}
			if (MySQL.dataExists(player.getUniqueId(), player)) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.register.already")));
				return true;
			}
			if (args.length == 1) {
				if (args[0].length() > Typa.getInstance().getConfig().getInt("settings.pin.maxcharacters")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.register.toolong").replace("%max_characters%", String.valueOf(Typa.getInstance().getConfig().getInt("settings.pin.maxcharacters")))));
					return true;
				}
				if (args[0].length() < Typa.getInstance().getConfig().getInt("settings.pin.mincharacters")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Typa.getInstance().getConfig().getString("lang.register.tooshort").replace("%min_characters%", String.valueOf(Typa.getInstance().getConfig().getInt("settings.pin.mincharacters")))));
					return true;
				}
				MySQL.registerPlayer(player.getUniqueId(), player, args[0],
						player.getAddress().getAddress().getHostAddress());
			} else {
				player.sendMessage(ChatColor.RED + "/register <pin>");
			}

		}
		return false;
	}
}