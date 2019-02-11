package net.okaru.lobby.listeners;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerJoinServerEvent implements Listener {
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
        Player player = event.getPlayer();
        player.getInventory().clear();
        player.setWalkSpeed(0.5f);
        player.setHealth(((CraftPlayer)player).getMaxHealth());
        player.setFoodLevel(20);
        PlayerInventory playerInventory = player.getInventory();
		playerInventory.setItem(4, ServerSelectorEvent.getSelector());
        player.updateInventory();
	}

}
