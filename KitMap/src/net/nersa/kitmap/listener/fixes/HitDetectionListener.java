package net.nersa.kitmap.listener.fixes;

import net.nersa.kitmap.HCF;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HitDetectionListener implements Listener {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, HCF.getInstance());
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setMaximumNoDamageTicks(19);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().setMaximumNoDamageTicks(19);
	}
}
