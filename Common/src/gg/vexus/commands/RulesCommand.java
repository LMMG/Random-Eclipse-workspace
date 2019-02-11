package gg.vexus.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.Core;

public class RulesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String arg2, String[] args) {
		if(command.getName().equalsIgnoreCase("rules")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(" ");
				return true;
			}
			
			Player player = (Player) commandSender;
			for(String msg : Core.config.getStringList("RULES_MESSAGE")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
		return false;
 	}
}
