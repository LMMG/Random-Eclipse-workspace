package gg.vexus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.utils.Color;

public class CopyInventoryCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		Player player = (Player) commandSender;
		if(command.getName().equals("copyinv")) {
			if(!player.hasPermission("common.copyinv")) {
				player.sendMessage("No");
				return true;
			}
			if(args.length != 1) {
				commandSender.sendMessage("Wrong usage: ");
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
		        player.getInventory().setContents(target.getInventory().getContents());
		        player.getInventory().setArmorContents(target.getInventory().getArmorContents());
		        player.sendMessage(Color.translate("&eYou copied the inventory of &a" + args[0] + "&e."));
			}
		}
		return false;
	}
}
