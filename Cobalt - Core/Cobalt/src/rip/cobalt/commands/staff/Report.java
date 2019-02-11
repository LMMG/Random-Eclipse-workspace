package rip.cobalt.commands.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Report implements CommandExecutor {
	
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)) {
			return true;
		}
		Player p = (Player) cs;
		if(cmd.getName().equalsIgnoreCase("report")) {
			
		}
		return true;
		
	}

}
