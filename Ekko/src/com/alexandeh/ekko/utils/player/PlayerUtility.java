package com.alexandeh.ekko.utils.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerUtility {

    public static Player[] getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static Set<String> getConvertedUuidSet(Set<UUID> uuids) {
        Set<String> toReturn = new HashSet<>();
        for (UUID uuid : uuids) {
            toReturn.add(uuid.toString());
        }
        return toReturn;
    }
}
