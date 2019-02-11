package gg.vexus.profile;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import gg.vexus.Core;
import net.md_5.bungee.api.ChatColor;

public class Profile implements Listener {
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		player.sendMessage(ChatColor.GREEN + "Loading in your Profile!");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GREEN + "We have loaded your profile!");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		if(!Core.getCore().getConfig().contains("Players." + uuid.toString())) {
			Core.getCore().getConfig().set("Players." + uuid.toString() + ".kills", 0);
			Core.getCore().saveConfig();
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if(player.getKiller() instanceof Player) {
			Player k = player.getKiller();
			String pUUID = player.getUniqueId().toString();
			String kUUID = k.getUniqueId().toString();
			int kills = Core.getCore().getConfig().getInt("Players." + kUUID + ".kills");
			
			Core.getCore().getConfig().set("Players." + kUUID + ".kills", kills +1);
			Core.getCore().getConfig().set("Players." + pUUID + ".deaths", kills +1);
			Core.getCore().saveConfig();
			
			k.sendMessage(ChatColor.GREEN + "Nice, You got a kill it's now being logged in are database!");
		}
	}
}
