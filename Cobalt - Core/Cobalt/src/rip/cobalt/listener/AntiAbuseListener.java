package rip.cobalt.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AntiAbuseListener implements Listener {
	
	@EventHandler
	public void onPlayerjoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if(player.getGameMode() == GameMode.CREATIVE) {
			player.setGameMode(GameMode.SURVIVAL);
			return;
		}
	}
}
