package net.nersa.kitmap.listener;

import net.nersa.kitmap.ConfigurationService;
import net.nersa.kitmap.HCF;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class KitMapListener implements Listener {

	HCF plugin;

	public KitMapListener(HCF plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (ConfigurationService.KIT_MAP) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (ConfigurationService.KIT_MAP && plugin.getFactionManager().getFactionAt(event.getItemDrop().getLocation()).isSafezone()) {
			event.getItemDrop().remove();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onItemSpawn(ItemSpawnEvent event) {
		if (ConfigurationService.KIT_MAP && plugin.getFactionManager().getFactionAt(event.getLocation()).isSafezone()) {
			event.getEntity().remove();
		}
	}

}