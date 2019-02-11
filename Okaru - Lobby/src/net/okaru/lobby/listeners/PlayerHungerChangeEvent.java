package net.okaru.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerChangeEvent implements Listener {
	
	@EventHandler
	public void onPlayerHungerChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
}
