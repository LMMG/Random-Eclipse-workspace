package net.okaru.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropEvent implements Listener {
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event){
		event.setCancelled(true);
	}

}
