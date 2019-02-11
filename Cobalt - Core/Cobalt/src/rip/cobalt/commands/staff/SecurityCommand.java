package rip.cobalt.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.database.MongoDB;

public class SecurityCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			return true;
		}
		Player p = (Player) cs;
		if (cmd.getName().equalsIgnoreCase("security")) {
			if (p.hasPermission("core.security")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "/security " + ChatColor.YELLOW + " add <player>");
					p.sendMessage(ChatColor.RED + "/security " + ChatColor.YELLOW + " remove <player>");
					p.sendMessage(ChatColor.RED + "/security " + ChatColor.YELLOW + " check <player>");
					return true;
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("add")) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							target = (Player) Bukkit.getOfflinePlayer(args[0]);
							return true;
						}
						if(MongoDB.getInstance().ifSecurity(target)) {
							p.sendMessage(target.getName() + " is already in the security system!");
							return true;
						}
						MongoDB.getInstance().AddPlayerToSecurity(target);
		                    return true;
					}
					if (args[1].equalsIgnoreCase("remove")) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							target = (Player) Bukkit.getOfflinePlayer(args[0]);
							return true;
						}
						if(MongoDB.getInstance().ifSecurity(target)) {
							MongoDB.getInstance().RemovePlayerFromSecurity(target);
							p.sendMessage("You successfully removed " + target.getName() + " from the security system!");
							return true;
						}
						p.sendMessage("You can't remove a player that isn't in the security system! You must first add him!");
						return true;
					    }
					if(args[1].equalsIgnoreCase("check")) {
						Player target = Bukkit.getPlayer(args[1]);
						if(target == null) {
							target = (Player) Bukkit.getOfflinePlayer(args[0]);
							return true;
						}
						if(MongoDB.getInstance().ifSecurity(target)) {
							p.sendMessage("The player " + target.getName() + " is in the Security system!");
							return true;
						}
						p.sendMessage("The player " + target.getName() + " isn't in the Security system!");
						return true;
					}
				}
			} else {
				p.sendMessage(ChatColor.RED + "No permissions!");
				return true;
			}
		}
		return true;
	}
}
