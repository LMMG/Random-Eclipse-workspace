package rip.cobalt.commands.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.util.Strings;

public class CobaltCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String arg2, String[] args) {
		if(command.getName().equalsIgnoreCase("cobalt")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			Player player = (Player) commandSender;
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m---------------------------"));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Version: &60.1-STABLE"));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Author: &6TCP, Addons"));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Licenced: &6asd8a7da"));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m---------------------------"));
		}
		return true;
	}
}
