package rip.cobalt.commands.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rip.cobalt.commands.essentials.Essentials;
import rip.cobalt.util.Strings;

public class GameModeCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("gamemode")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			if(!Essentials.hasPermission(commandSender, "gamemode")) {
				commandSender.sendMessage(Strings.PERMISSION);
				return true;
			}
			
			Player player = (Player) commandSender;
			
			if(args.length == 0) {
				player.sendMessage(Strings.USAGE);
				return true;
			}
			else if(args[0].equalsIgnoreCase("1")) {
				player.setGameMode(GameMode.CREATIVE);
				player.sendMessage(ChatColor.BLUE + "Your Gamemode Has Been Set to Creative!");
				return true;
			}
			else if(args[0].equalsIgnoreCase("0")) {
				player.setGameMode(GameMode.SURVIVAL);
				player.sendMessage(ChatColor.BLUE + "Your gamemode has been set to survival!");
				player.setHealth(20);
				player.setFlying(false);
				return true;
			}
		}
		return false;
	}
}
