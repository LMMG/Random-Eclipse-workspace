package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.Typa;

public class PlayerCommandListener implements Listener {
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!Typa.logged_in.contains(p.getName()) && p.hasPermission("rifthq.login")) {
			if (e.getMessage().startsWith("/login")) {
				return;
			}
			if (e.getMessage().startsWith("/register")) {
				return;
			}
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + ("You must login before attempting to execute a command!"));
		}
	}
}
