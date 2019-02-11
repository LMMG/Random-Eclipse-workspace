package rip.cobalt.commands.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rip.cobalt.CobaltPlugin;
import rip.cobalt.util.Strings;

public class RulesCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("rules")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			Player player = (Player) commandSender;
			for(String msg : CobaltPlugin.config.getStringList("MESSAGE_RULES")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
		return false;
	}
}
