/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.alexandeh.ekko.factions.Faction
 *  com.alexandeh.ekko.factions.type.PlayerFaction
 *  com.alexandeh.ekko.factions.type.SystemFaction
 *  org.bukkit.entity.Player
 */
package com.hcempire.horus.listerners.focus;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import org.bukkit.entity.Player;

public class EkkoUtils {

    public static class MangoFaction {
        public static boolean isPlayerFaction(Faction faction) {
            return faction instanceof PlayerFaction;
        }

        public static boolean isSystemFaction(Faction faction) {
            return faction instanceof SystemFaction;
        }
    }

    public static class MangoPlayer {
        public static boolean isInFaction(Player player) {
            return MangoFaction.isPlayerFaction((Faction)PlayerFaction.getByPlayer((Player)player)) && PlayerFaction.getByPlayer((Player)player) != null;
        }

        public static PlayerFaction getFaction(Player player) {
            if (MangoPlayer.isInFaction(player)) {
                return PlayerFaction.getByPlayer((Player)player);
            }
            return null;
        }

        public static String getFactionName(Player player) {
            if (MangoPlayer.isInFaction(player)) {
                return PlayerFaction.getByPlayer((Player)player).getName();
            }
            return "None";
        }
    }

}

