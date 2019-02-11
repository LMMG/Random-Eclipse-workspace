package rip.cobalt.commands.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.CobaltPlugin;
import rip.cobalt.util.Strings;

public class HelpCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("help")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			Player player = (Player) commandSender;
			
			for(String msg : CobaltPlugin.config.getStringList("MESSAGE_HELP")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
		return false;
	}
}
