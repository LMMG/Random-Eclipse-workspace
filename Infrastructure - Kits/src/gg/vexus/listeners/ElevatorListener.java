package gg.vexus.listeners;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import gg.vexus.Core;

public class ElevatorListener implements Listener
{

    @EventHandler
    public void onSignUpChangeEvent(final SignChangeEvent event) {
        final List<String> lines = new ArrayList<String>();
        final List<String> toCompare = new ArrayList<String>();
        for (final String line : event.getLines()) {
            lines.add(line.toUpperCase());
        }
        for (final String line2 : Core.getCore().configFile.getStringList("UP_SIGN_CREATE_TEXT")) {
            toCompare.add(line2.toUpperCase());
        }
        if (lines.containsAll(toCompare)) {
            for (int i = 0; i < Core.getCore().configFile.getStringList("UP_SIGN_TEXT").size(); ++i) {
                if (i < 4) {
                    event.setLine(i, (String)Core.getCore().configFile.getStringList("UP_SIGN_TEXT").get(i));
                }
            }
        }
    }
    
    @EventHandler
    public void onSignDownChangeEvent(final SignChangeEvent event) {
        final List<String> lines = new ArrayList<String>();
        final List<String> toCompare = new ArrayList<String>();
        for (final String line : event.getLines()) {
            lines.add(line.toUpperCase());
        }
        for (final String line2 : Core.getCore().configFile.getStringList("DOWN_SIGN_CREATE_TEXT")) {
            toCompare.add(line2.toUpperCase());
        }
        if (lines.containsAll(toCompare)) {
            for (int i = 0; i < Core.getCore().configFile.getStringList("DOWN_SIGN_TEXT").size(); ++i) {
                if (i < 4) {
                    event.setLine(i, (String)Core.getCore().configFile.getStringList("DOWN_SIGN_TEXT").get(i));
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractUpSignEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            for (int i = 0; i < Core.getCore().configFile.getStringList("UP_SIGN_TEXT").size(); ++i) {
                if (i < 4 && !sign.getLine(i).equalsIgnoreCase(Core.getCore().configFile.getStringList("UP_SIGN_TEXT").get(i))) {
                    return;
                }
            }
            new BukkitRunnable() {
                public void run() {
                    final Location location = sign.getBlock().getLocation().clone().add(0.0, 1.0, 0.0);
                    while (location.getBlock().isEmpty()) {
                        location.add(0.0, 1.0, 0.0);
                    }
                    while (!location.getBlock().isEmpty()) {
                        location.add(0.0, 1.0, 0.0);
                    }
                    if (location.getBlockY() < location.getWorld().getMaxHeight()) {
                        player.teleport(location.add(0.5, 0.0, 0.5).setDirection(player.getLocation().getDirection()));
                    }
                }
            }.runTaskAsynchronously(Core.getCore());
        }
    }
    
    @EventHandler
    public void onPlayerInteractDownSignEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            for (int i = 0; i < Core.getCore().configFile.getStringList("DOWN_SIGN_TEXT").size(); ++i) {
                if (i < 4 && !sign.getLine(i).equalsIgnoreCase(Core.getCore().configFile.getStringList("DOWN_SIGN_TEXT").get(i))) {
                    return;
                }
            }
            new BukkitRunnable() {
                public void run() {
                    final Location location = sign.getBlock().getLocation().clone().add(0.0, -1.0, 0.0);
                    if (location.getBlock().isEmpty()) {
                        while (location.getBlock().isEmpty()) {
                            location.add(0.0, -1.0, 0.0);
                        }
                    }
                    while (!location.getBlock().isEmpty()) {
                        location.add(0.0, -1.0, 0.0);
                    }
                    while (location.getBlock().isEmpty()) {
                        location.add(0.0, -1.0, 0.0);
                    }
                    if (location.getBlockY() > 0) {
                        if (!location.getBlock().isEmpty()) {
                            location.add(0.0, 2.0, 0.0);
                        }
                        player.teleport(location.add(0.5, -1.0, 0.5).setDirection(player.getLocation().getDirection()));
                    }
                }
            }.runTaskAsynchronously(Core.getCore());
        }
    }
}
