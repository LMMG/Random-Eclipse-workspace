package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.rifthq.typa.Typa;

public class PlayerIntractListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() != null) {
			Player player = (Player) event.getWhoClicked();
			if (!Typa.logged_in.contains(player.getName()) && player.hasPermission("rifthq.login")) {
				player.closeInventory();
				event.setCancelled(true);
			}
		}
	}
}
