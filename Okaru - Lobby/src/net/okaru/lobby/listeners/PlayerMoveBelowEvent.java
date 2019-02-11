package net.okaru.lobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveBelowEvent implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getPlayer().getLocation().getBlockY() < -25) {
			event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
		}
	}
}
