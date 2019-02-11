package gg.vexus.commands;

import gg.vexus.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("help")) {
			if (!(commandSender instanceof Player)) {
				commandSender.sendMessage(ChatColor.RED + "No!");
				return true;
			}

			Player player = (Player) commandSender;

			for(String msg : Core.config.getStringList("HELP_MESSAGE")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
		return false;
	}
}
