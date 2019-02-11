package rip.cobalt.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.database.MongoDB;

public class ChangePin implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)){
			return true;
		}
		Player p = (Player) cs;
		if(cmd.getName().equalsIgnoreCase("changepin")) {
			if(p.hasPermission("core.changepin")) {
				if(args.length == 0) {
					p.sendMessage(ChatColor.YELLOW + "/changepin <pin>");
					if(p.hasPermission("core.changepin.others")) {
						p.sendMessage(ChatColor.YELLOW + "/changepin <player> <pin>");
						return true;
					}
					return true;
				}
				if(args.length == 1) {
					if(!Register.isInteger(args[0])) {
						p.sendMessage(ChatColor.RED + "You must input numbers!");
						return true;
					}
		            MongoDB.getInstance().ChangeStaffPassword(p, args[0]);
	        		MongoDB.getInstance().StaffPinRefresh(p);
		            p.sendMessage(ChatColor.GREEN + "You changed your password successfully! The new password will take effect when you relog.");
		            return true;
				}
		        if(args.length == 2) { 
		        	Player target = Bukkit.getPlayer(args[0]);
		        	if(target == null) {
		        		target = (Player) Bukkit.getOfflinePlayer(args[0]);
		               return true;
		        	}
		        	if(!Register.isInteger(args[1])) {
		        		p.sendMessage(ChatColor.RED + "You must input numbers!");
						return true;
		        	}
		        	if(MongoDB.getInstance().ifStaff(target)) {
		        		MongoDB.getInstance().ChangeStaffPassword(target, args[1]);
		        		MongoDB.getInstance().StaffPinRefresh(target);
			        	p.sendMessage(ChatColor.GREEN + "You have successfully changed " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s pin to " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + ".");
			        	p.sendMessage(ChatColor.GREEN + "The password will take effect once the user relogs!");
			        	return true;
		        	}
		        }
			}
		}
	    return true;
		
	}

}
