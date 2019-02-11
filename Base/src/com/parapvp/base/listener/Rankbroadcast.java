package com.parapvp.base.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Rankbroadcast implements Listener {
	
	public static void enable() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArrayList<String> playerNames = new ArrayList<String>();
				for(Player player : Bukkit.getOnlinePlayers()) {
					PermissionUser u = PermissionsEx.getUser(player);
					PermissionGroup g = u.getGroups()[0];
					if(g.getName().equalsIgnoreCase("Myth")) {
						playerNames.add(player.getName());
					}
				}
				Bukkit.broadcastMessage(ChatColor.YELLOW + "Elite Users " + ChatColor.GOLD + "» " + ChatColor.WHITE + playerNames.toString());
			}
		};
	}
}
