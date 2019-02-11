package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	@EventHandler
	public void on(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.sendMessage(String.valueOf("\u00a7cIf you are new, please use command /register!"));
		p.sendMessage(String.valueOf("\u00a7cIf you have registered, please use command /login!"));
	}
}
