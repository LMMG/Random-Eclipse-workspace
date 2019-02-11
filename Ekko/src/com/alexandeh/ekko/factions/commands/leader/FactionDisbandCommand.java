package com.alexandeh.ekko.factions.commands.leader;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.events.player.PlayerDisbandFactionEvent;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionDisbandCommand extends FactionCommand {
    @Command(name = "f.disband", aliases = {"faction.disband", "factions.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Profile.getByUuid(player.getUniqueId());
        PlayerFaction playerFaction;

        if (command.getArgs().length >= 1 && player.hasPermission("ekko.admin")) {
            String name = command.getArgs(0);
            Faction faction = PlayerFaction.getAnyByString(name);
            if (faction != null) {
                if (faction instanceof PlayerFaction) {
                    playerFaction = (PlayerFaction) faction;
                } else {
                    player.sendMessage(langConfig.getString("SYSTEM_FACTION.DELETED").replace("%NAME%", faction.getName()));

                    Faction.getFactions().remove(faction);

                    Set<Claim> claims = new HashSet<>(faction.getClaims());
                    for (Claim claim : claims) {
                        claim.remove();
                    }

                    main.getMongoDatabase().getCollection("systemFactions").deleteOne(eq("uuid", faction.getUuid().toString()));
                    return;
                }
            } else {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                return;
            }
        } else {
            playerFaction = profile.getFaction();

            if (playerFaction == null) {
                player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                return;
            }

            if (!playerFaction.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(langConfig.getString("ERROR.NOT_LEADER"));
                return;
            }
        }

        for (UUID member : playerFaction.getAllPlayerUuids()) {
            Profile memberProfile = Profile.getByUuid(member);
            if (memberProfile != null && memberProfile.getFaction().equals(playerFaction)) {
                memberProfile.setFaction(null);
            }
        }

        main.getEconomy().depositPlayer(player, playerFaction.getBalance());

        Bukkit.getPluginManager().callEvent(new PlayerDisbandFactionEvent(player, playerFaction));

        Bukkit.broadcastMessage(langConfig.getString("ANNOUNCEMENTS.FACTION_DISBANDED").replace("%PLAYER%", player.getName()).replace("%NAME%", playerFaction.getName()));
        Faction.getFactions().remove(playerFaction);

        main.getMongoDatabase().getCollection("playerFactions").deleteOne(eq("uuid", playerFaction.getUuid().toString()));

        for (PlayerFaction ally : playerFaction.getAllies()) {
            ally.getAllies().remove(playerFaction);
        }

        Set<Claim> claims = new HashSet<>(playerFaction.getClaims());
        for (Claim claim : claims) {
            claim.remove();
        }
    }
}
