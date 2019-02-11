package org.ipvp.hcf.listener;

import org.bukkit.event.vehicle.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class ElevatorListener implements Listener
{
    @EventHandler
    public void onMinecart(final VehicleEnterEvent event) {
        if (!(event.getVehicle() instanceof Minecart) || !(event.getEntered() instanceof Player)) {
            return;
        }
        final Player p = (Player)event.getEntered();
        final Location l = event.getVehicle().getLocation();
        final Location loc = new Location(p.getWorld(), (double)l.getBlockX(), (double)l.getBlockY(), (double)l.getBlockZ());
        final Material m = loc.getBlock().getType();
        if (m.equals((Object)Material.FENCE_GATE) || m.equals((Object)Material.SIGN_POST)) {
            event.setCancelled(true);
            p.teleport(this.teleportSpot(loc, loc.getBlockY(), 254));
        }
    }
    
    public Location teleportSpot(final Location loc, final int min, final int max) {
        for (int k = min; k < max; ++k) {
            final Material m1 = new Location(loc.getWorld(), (double)loc.getBlockX(), (double)k, (double)loc.getBlockZ()).getBlock().getType();
            final Material m2 = new Location(loc.getWorld(), (double)loc.getBlockX(), (double)(k + 1), (double)loc.getBlockZ()).getBlock().getType();
            if (m1.equals((Object)Material.AIR) && m2.equals((Object)Material.AIR)) {
                return new Location(loc.getWorld(), (double)loc.getBlockX(), (double)k, (double)loc.getBlockZ());
            }
        }
        return new Location(loc.getWorld(), (double)loc.getBlockX(), (double)loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), (double)loc.getBlockZ());
    }
}
