package net.wenjapvp.kohisg.profile;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import net.wenjapvp.kohisg.KohiSG;

public class Profile implements Listener
{
	
	private KohiSG core;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		/**
		 * 
		 * One day it it will be a SQL Profile :D
		 * 
		 */
		
		player.sendMessage(ChatColor.GREEN + "Your Profile Has Loaded!");
	}
	
	@EventHandler
	public void onPlayerJoin2(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		if(!core.getInstance().getConfig().contains("Players." + uuid.toString())) {
			core.getInstance().getConfig().set("Players." + uuid.toString() + ".kills", 0);
			core.getInstance().saveConfig();
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if(player.getKiller() instanceof Player) {
			Player k = player.getKiller();
			String pUUID = player.getUniqueId().toString();
			String kUUID = k.getUniqueId().toString();
			int kills = core.getInstance().getConfig().getInt("Players." + kUUID + ".kills");
			
			core.getInstance().getConfig().set("Players." + kUUID + ".kills", kills +1);
			core.getInstance().getConfig().set("Players." + pUUID + ".deaths", kills +1);
			core.getInstance().saveConfig();
			
			k.sendMessage(ChatColor.GREEN + "Nice, You got a kill it's now being logged in are database!");
		}
	}
}
