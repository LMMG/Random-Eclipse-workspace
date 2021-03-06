package net.nersa.kitmap.listener.fixes;

import net.nersa.kitmap.HCF;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listener that prevents {@link Player}s from being trapped in portals.
 */
public class PortalTrapFixListener implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) return;
		if (HCF.getInstance().getFactionManager().getFactionAt(e.getClickedBlock().getLocation()).isSafezone()) return;
		if (e.getClickedBlock().getType() == Material.PORTAL) {
			e.getClickedBlock().setType(Material.AIR);
		}
	}


}
