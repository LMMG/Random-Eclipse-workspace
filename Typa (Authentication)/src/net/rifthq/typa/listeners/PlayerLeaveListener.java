package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.rifthq.typa.Typa;

public class PlayerLeaveListener implements Listener {
	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Typa.logged_in.contains(p.getName()) && p.hasPermission("rifthq.login")) {
			Typa.logged_in.remove(p.getName());
		}
	}
}
