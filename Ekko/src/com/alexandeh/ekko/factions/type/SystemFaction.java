package com.alexandeh.ekko.factions.type;

import com.alexandeh.ekko.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
@Getter
@Setter
public class SystemFaction extends Faction {

    private ChatColor color = ChatColor.WHITE;
    private boolean deathban = true;

    public SystemFaction(String name, UUID uuid) {
        super(name, uuid);
    }

    public static Set<SystemFaction> getSystemFactions() {
        Set<SystemFaction> toReturn = new HashSet<>();
        for (Faction faction : Faction.getFactions()) {
            if (faction instanceof SystemFaction) {
                toReturn.add((SystemFaction) faction);
            }
        }
        return toReturn;
    }

    public static SystemFaction getByName(String name) {
        for (SystemFaction systemFaction : getSystemFactions()) {
            if (systemFaction.getName().replace(" ", "").equalsIgnoreCase(name)) {
                return systemFaction;
            }
        }
        return null;
    }

}
