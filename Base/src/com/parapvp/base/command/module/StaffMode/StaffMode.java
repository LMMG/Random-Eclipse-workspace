package com.parapvp.base.command.module.StaffMode;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.listener.StaffModeListener2;

import net.md_5.bungee.api.ChatColor;

public class StaffMode implements CommandExecutor {
	public static ArrayList<Player> staffmode = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("staff")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You can only use this command ingame!");
				return true;
			}
			Player p = (Player) sender;
			if (staffmode.contains(p)) {
				staffmode.remove(p);
				p.sendMessage(ChatColor.YELLOW + "Staff Mode: " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Disabled");
				Bukkit.dispatchCommand(p, "vanish");
				StaffModeListener2.resetPlayerInventory(p);
			} else if (!staffmode.contains(p)) {
				staffmode.add(p);
				p.sendMessage(ChatColor.YELLOW + "Staff Mode: " + ChatColor.GOLD.toString() + ChatColor.BOLD + "Enabled");
				Bukkit.dispatchCommand(p, "vanish");
				StaffModeListener2.setStaffInventory(p);
				}
			}
		return false;
		}
}
