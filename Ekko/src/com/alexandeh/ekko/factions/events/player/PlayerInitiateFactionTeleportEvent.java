package com.alexandeh.ekko.factions.events.player;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.events.FactionEvent;
import com.alexandeh.ekko.profiles.teleport.ProfileTeleportType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class PlayerInitiateFactionTeleportEvent extends FactionEvent{

    private Faction faction;
    private Player player;
    private ProfileTeleportType teleportType;
    private Location initialLocation;
    private long init;
    @Setter private double time;
    @Setter private Location location;
    @Setter private boolean cancelled;

    public PlayerInitiateFactionTeleportEvent(Player player, Faction faction, ProfileTeleportType teleportType, double time, Location location, Location initialLocation) {
        this.player = player;
        this.faction = faction;
        this.teleportType = teleportType;
        this.time = time;
        this.init = System.currentTimeMillis();
        this.location = location;
        this.initialLocation = initialLocation;
    }


}
