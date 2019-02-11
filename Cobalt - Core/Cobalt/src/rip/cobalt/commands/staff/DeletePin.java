package rip.cobalt.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.database.MongoDB;

public class DeletePin implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)) {
			return true;
		}
		Player p = (Player) cs;
		if(cmd.getName().equalsIgnoreCase("deletepin")) {
			if(cs.hasPermission("core.deletepin")){
				if(args.length == 0) {
					p.sendMessage(ChatColor.YELLOW + " /deletepin <Player>");
					return true;
				}
			    if(args.length == 1) {
			    	Player target = Bukkit.getPlayer(args[0]);
			    	if(target == null) {
			    		target = (Player) Bukkit.getOfflinePlayer(args[0]);
			    		return true;
			    	}
			    	if(!MongoDB.getInstance().ifStaff(target)){
			    			p.sendMessage(ChatColor.RED + "The player " + ChatColor.YELLOW + target +  " isn't staff!");
			    			return true;
			    		}
			    	      MongoDB.getInstance().RemoveStaffPin(target);
			    	   if(target.getName().equalsIgnoreCase(cs.getName())) {
			    		p.sendMessage(ChatColor.GREEN + "You removed your pin!");
			    		return true;
			    	   }
			    	   p.sendMessage(ChatColor.GREEN + "You removed " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s pin!");
			    	}
			    }
			}
		return true;
	}

}
