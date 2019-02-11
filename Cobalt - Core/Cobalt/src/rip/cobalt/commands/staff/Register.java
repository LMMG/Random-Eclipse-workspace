package rip.cobalt.commands.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.database.MongoDB;

public class Register implements CommandExecutor {
	
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)){
			return true;
		}
		Player p = (Player) cs;
		if(cmd.getName().equalsIgnoreCase("register")) {
			if(p.hasPermission("core.register")) {
				if(args.length == 0) {
					p.sendMessage(ChatColor.YELLOW + "Register with /register <pwd>");
					return true;
				}
				if(args.length == 1) {
					if(isInteger(args[0])) {
						if(MongoDB.getInstance().ifStaff(p)) {
							p.sendMessage(ChatColor.RED + "You are already registered!");
							return true;
						}
						MongoDB.getInstance().AddStaffPassword(p, args[0]);
						MongoDB.getInstance().StaffPinRefresh(p);
						p.sendMessage(ChatColor.GREEN + "You have successfully been added to the database!");
						return true;
						
					} else {
						p.sendMessage(ChatColor.RED + "Please enter numbers!");
				  }
				}
			} else {
				p.sendMessage(ChatColor.RED + "Ya dont have permz");
				return true;
			}
		}
		return true;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}
