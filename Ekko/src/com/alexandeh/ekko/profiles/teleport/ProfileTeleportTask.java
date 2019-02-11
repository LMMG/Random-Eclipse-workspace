package com.alexandeh.ekko.profiles.teleport;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.events.player.PlayerInitiateFactionTeleportEvent;
import com.alexandeh.ekko.profiles.Profile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class ProfileTeleportTask extends BukkitRunnable {

    @Getter
    private PlayerInitiateFactionTeleportEvent event;

    public ProfileTeleportTask(PlayerInitiateFactionTeleportEvent event) {
        this.event = event;

        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void run() {
        if (!(event.isCancelled())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(event.getLocation());
                }
            }.runTask(Ekko.getInstance());
            Profile.getByUuid(event.getPlayer().getUniqueId()).setTeleportWarmup(null);
        }
    }
}
