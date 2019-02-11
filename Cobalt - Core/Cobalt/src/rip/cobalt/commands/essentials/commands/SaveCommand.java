package rip.cobalt.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.commands.essentials.Essentials;
import rip.cobalt.util.Strings;

public class SaveCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("save")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			if(!Essentials.hasPermission(commandSender, "save")) {
				commandSender.sendMessage(Strings.PERMISSION);
				return true;
			}
			Player player = (Player) commandSender;
			Bukkit.getWorld("world").save();
			Bukkit.savePlayers();
			player.sendMessage(ChatColor.GREEN + "You have saved the world!");
		}
		return false;
	}
}
