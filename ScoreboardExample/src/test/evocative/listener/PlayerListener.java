package test.evocative.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import rip.evocative.PlayerBoard;
import rip.evocative.provider.ScoreboardProvider;
import test.evocative.scoreboard.ScoreboardTimerProvider;

public class PlayerListener implements Listener
{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getServer().getConsoleSender().sendMessage("OnJoin has worked");
		PlayerBoard.get(e.getPlayer()).setProvider(ScoreboardTimerProvider.getInstance());
		PlayerBoard.get(e.getPlayer()).setVisible(true);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Bukkit.getServer().getConsoleSender().sendMessage("OnQuit has worked");
		PlayerBoard.dispose(e.getPlayer());
	}
}
