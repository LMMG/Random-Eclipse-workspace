package com.alexandeh.ekko.factions.commands.officer;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.LocationSerialization;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionSetHomeCommand extends FactionCommand {
    @Command(name = "f.sethome", aliases = {"faction.sethome", "factions.sethome", "factions.sethq", "f.sethq", "faction.sethq"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        Faction faction;
        PlayerFaction playerFaction = null;
        if (command.getArgs().length >= 1 && player.hasPermission("ekko.admin")) {
            String name = command.getArgs(0);
            Faction faction1 = PlayerFaction.getAnyByString(name);
            if (faction1 != null) {
                if (faction1 instanceof PlayerFaction) {
                    playerFaction = (PlayerFaction) faction1;
                }
                faction = faction1;
                if (faction instanceof SystemFaction) {
                    faction.setHome(LocationSerialization.serializeLocation(player.getLocation()));
                    player.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_SET_HOME").replace("%PLAYER%", player.getName()));
                    return;
                }
            } else {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                return;
            }
        } else {
            faction = profile.getFaction();
            playerFaction = profile.getFaction();

            if (faction == null) {
                player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                return;
            }

            if (!playerFaction.getLeader().equals(player.getUniqueId()) && !playerFaction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission("ekko.admin")) {
                player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER_OR_LEADER"));
                return;
            }
        }

        for (Claim claim : playerFaction.getClaims()) {
            if (claim.isInside(player.getLocation())) {
                playerFaction.setHome(LocationSerialization.serializeLocation(player.getLocation()));
                playerFaction.sendMessage(langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_SET_HOME").replace("%PLAYER%", player.getName()));
                return;
            }
        }

        player.sendMessage(langConfig.getString("ERROR.MUST_BE_IN_LAND_TO_SET_HOME"));
    }
}
