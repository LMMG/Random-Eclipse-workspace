package gg.vexus.handler;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import gg.vexus.Core;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by Owner on 02/07/2017.
 */
public class DonatorBroadcastHandler implements Listener {
	
	public static void init() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArrayList<String> playerNames = new ArrayList<String>();
				for(Player player : Bukkit.getOnlinePlayers()) {
					PermissionUser u = PermissionsEx.getUser(player);
					PermissionGroup g = u.getGroups()[0];
					if(g.getName().equalsIgnoreCase("Mist")) {
						playerNames.add(player.getName());
					}
				}
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(ChatColor.GREEN + "Mist Users " + ChatColor.GRAY + "» " + ChatColor.WHITE + playerNames.toString());
				Bukkit.broadcastMessage(ChatColor.GRAY + "This can be purchased on our store store.mist.gg!");
				Bukkit.broadcastMessage("");
			}
		}.runTaskTimer(Core.getCore(), 3600L, 3600L);
	}
}
