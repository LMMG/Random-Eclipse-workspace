package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.rifthq.typa.Typa;

public class PlayerDamageListener implements Listener {
	@EventHandler
	public void damager(EntityDamageByEntityEvent e) {
		Player damager;
		if (e.getDamager() instanceof Player && !Typa.logged_in.contains((damager = (Player) e.getDamager()).getName())
				&& damager.hasPermission("rifthq.login")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void victim(EntityDamageByEntityEvent e) {
		Player player;
		if (e.getEntity() instanceof Player && !Typa.logged_in.contains((player = (Player) e.getEntity()).getName())
				&& player.hasPermission("rifthq.login")) {
			e.setCancelled(true);
		}
	}
}
