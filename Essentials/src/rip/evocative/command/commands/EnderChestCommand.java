package rip.evocative.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rip.evocative.command.EssentialsModule;
import rip.evocative.util.Strings;

public class EnderChestCommand implements CommandExecutor
{
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("enderchest")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			if(!EssentialsModule.hasPermission(commandSender, "enderchest")) {
				commandSender.sendMessage(Strings.PERMISSION);
				return true;
			}
			Player player = (Player) commandSender;
			player.openInventory(player.getEnderChest());
		}
		return false;
	}
}
