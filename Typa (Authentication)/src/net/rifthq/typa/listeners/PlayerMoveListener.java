package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.rifthq.typa.Typa;

public class PlayerMoveListener implements Listener
{
	@EventHandler
	public void move(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (!Typa.logged_in.contains(p.getName()) && p.hasPermission("rifthq.login"))
		{
			e.setTo(e.getFrom());
		}
	}
}