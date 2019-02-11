package net.okaru.lobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerChangeEvent implements Listener {
	
	@EventHandler
	public void onPlayerPlaceEvent(BlockPlaceEvent event){
		if ((event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBreakEvent(BlockBreakEvent event){
		if ((event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))) {
			event.setCancelled(true);
		}
	}
}