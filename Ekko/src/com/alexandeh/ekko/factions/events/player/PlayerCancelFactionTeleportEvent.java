package com.alexandeh.ekko.factions.events.player;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.events.FactionEvent;
import com.alexandeh.ekko.profiles.teleport.ProfileTeleportType;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
public class PlayerCancelFactionTeleportEvent extends FactionEvent{

    private Faction faction;
    private Player player;
    private ProfileTeleportType teleportType;

    public PlayerCancelFactionTeleportEvent(Player player, Faction faction, ProfileTeleportType teleportType) {
        this.player = player;
        this.faction = faction;
        this.teleportType = teleportType;
    }


}
