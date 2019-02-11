package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.Typa;

public class PlayerBuildListeners implements Listener {
	@EventHandler
	public void place(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (!Typa.logged_in.contains(p.getName()) && p.hasPermission("rifthq.login")) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + ("You must login before attempting to placing blocks!"));
		}
	}

	@EventHandler
	public void breakblock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!Typa.logged_in.contains(p.getName()) && p.hasPermission("rifthq.login")) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + ("You must login before attempting to breaking blocks!"));
		}
	}
}
