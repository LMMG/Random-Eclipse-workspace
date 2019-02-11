package com.hcempire.horus.misc.listeners;

import com.hcempire.horus.Horus;
import com.hcempire.horus.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GlitchListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            if (event.getPlayer().getLocation().getBlockY() > event.getBlock().getLocation().getBlockY()) {
                event.getPlayer().teleport(event.getPlayer().getLocation());
                event.getPlayer().setVelocity(new Vector());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getPlayer().setVelocity(new Vector(0, -0.25, 0));
                    }
                }.runTaskLaterAsynchronously(Horus.getInstance(), 4L);
            }
        }
    }

    @EventHandler
    public void onVehicleMoveEvent(VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Boat) {
            if (event.getVehicle().getLocation().getBlock().getType() != Material.AIR) {
                if (event.getVehicle().getPassenger() != null) {
                    event.getVehicle().getPassenger().eject();
                    event.getVehicle().getPassenger().teleport(event.getVehicle().getLocation().setDirection(event.getVehicle().getPassenger().getLocation().getDirection()));
                }

                event.getVehicle().remove();
                event.getVehicle().getWorld().dropItemNaturally(event.getVehicle().getLocation(), new ItemStack(Material.BOAT));
            }
        }
    }

    @EventHandler
    public void onVehicleDestroyEvent(VehicleDestroyEvent event) {
        if (event.getVehicle() instanceof Boat) {
            if (event.getVehicle().getPassenger() != null) {
                event.getVehicle().getPassenger().eject();
                event.getVehicle().getPassenger().teleport(event.getVehicle().getLocation().setDirection(event.getVehicle().getPassenger().getLocation().getDirection()));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = event.getPlayer();
            Location newLocation = event.getTo().clone();

            boolean surrounded = true;
            boolean open = true;
            for (BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SELF}) {
                Block block = newLocation.getBlock().getRelative(face);
                if (block.getType() == Material.AIR) {
                    surrounded = false;
                } else {
                    open = false;
                }
            }

            if (surrounded) {
                if (!newLocation.clone().add(0, 1, 0).getBlock().isEmpty()) {
                    newLocation.setY(newLocation.getY() - 1);
                    event.setTo(newLocation);
                } else {
                    newLocation.setY(newLocation.getY() - 2);
                    event.setTo(newLocation);
                }
                return;
            }

            if (open) {
                if (!newLocation.clone().add(0, -1, 0).getBlock().isEmpty() && newLocation.clone().add(0, 2, 0).getBlock().isEmpty()) {
                    event.setTo(newLocation.add(0, 2, 0));
                    return;
                }

                if (!newLocation.clone().add(0, 1, 0).getBlock().isEmpty() && newLocation.clone().add(0, -2, 0).getBlock().isEmpty()) {
                    event.setTo(newLocation.add(0, -2, 0));
                    return;
                }
            }

            newLocation.setX(newLocation.getBlockX());
            newLocation.setY(newLocation.getBlockY());
            newLocation.setZ(newLocation.getBlockZ());

            int attempts = 0;
            while (!newLocation.clone().add(0, 1, 0).getBlock().isEmpty() || !newLocation.getBlock().isEmpty()) {
                attempts++;

                newLocation.setY(event.getTo().getY());
                newLocation.add(newLocation.getDirection().multiply(-1).normalize());

                if (attempts >= 100) {
                    break;
                }
            }

            boolean modified = false;
            if (newLocation.getBlockX() > 0) {
                newLocation.setX(newLocation.getBlockX() + 0.5);
            } else {
                newLocation.setX(newLocation.getBlockX() - 0.5);
                modified = true;
                newLocation.setY(event.getTo().getY());
                newLocation.add(newLocation.getDirection().multiply(-1).normalize());
            }

            if (newLocation.getBlockZ() > 0) {
                newLocation.setZ(newLocation.getBlockZ() + 0.5);
            } else {
                newLocation.setZ(newLocation.getBlockZ() - 0.5);
                if (!(modified)) {
                    newLocation.setY(event.getTo().getY());
                    newLocation.add(newLocation.getDirection().multiply(-1).normalize());
                }
            }

            newLocation.setDirection(player.getLocation().getDirection());

            event.setTo(newLocation);
        }
    }

}
