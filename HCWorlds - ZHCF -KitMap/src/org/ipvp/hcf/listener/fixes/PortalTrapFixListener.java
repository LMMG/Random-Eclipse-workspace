package org.ipvp.hcf.listener.fixes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.ipvp.hcf.HCF;

/**
 * Listener that prevents {@link Player}s from being trapped in portals.
 */
public class PortalTrapFixListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getClickedBlock() == null) return;
        if(HCF.getPlugin().getFactionManager().getFactionAt(e.getClickedBlock().getLocation()).isSafezone()) return;
        if(e.getClickedBlock().getType() == Material.PORTAL){
            e.getClickedBlock().setType(Material.AIR);
        }
    }


}
