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
}
