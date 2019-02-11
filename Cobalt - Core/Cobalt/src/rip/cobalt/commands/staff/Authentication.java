package rip.cobalt.commands.staff;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.database.MongoDB;

public class Authentication implements CommandExecutor, Listener {
	
	public static ArrayList<UUID> AuthenticationPhase = new ArrayList<UUID>();
	
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(!(cs instanceof Player)) {
			return true;
		}
		Player p = (Player) cs;
		if(cmd.getName().equalsIgnoreCase("login")) {
			if(p.hasPermission("core.login")) {
				if(args.length == 0) {
					p.sendMessage(ChatColor.YELLOW + "Login with /login <pwd>");
					return true;
				}
				if(args.length == 1) {
					if(Register.isInteger(args[0])) {
						if(!(AuthenticationPhase.contains(p.getUniqueId()))){
							p.sendMessage(ChatColor.RED + "You have already logged in!");
							return true;
						    }
						if(MongoDB.getInstance().ifStaff(p)) {
							MongoDB.getInstance().StaffPinRefresh(p);
							if(args[0].equalsIgnoreCase((MongoDB.getInstance().pwd))) {
									p.sendMessage(ChatColor.GREEN + "You have logged in successfully!");
									AuthenticationPhase.remove(p.getUniqueId());
									return true;
							} else {
								p.sendMessage(ChatColor.RED + "Your password is incorrect!");
								return true;
							}
						} else {
							p.sendMessage(ChatColor.RED + "Register first with /register <pwd>!");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Enter numbers!");
					}
				}
			} else {
				p.sendMessage(ChatColor.RED + "You don't have sufficient permissions!");
			}
		}
		return true;
	}
	
	@EventHandler
	public void Chat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(AuthenticationPhase.contains(p.getUniqueId())) {
			if(!e.getMessage().startsWith("/login") || !e.getMessage().startsWith("/register")){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Please authenticate!");
			}
		}
	}
}
