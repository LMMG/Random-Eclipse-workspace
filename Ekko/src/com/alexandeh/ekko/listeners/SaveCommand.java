package com.alexandeh.ekko.listeners;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.utils.Color;
import com.alexandeh.ekko.utils.player.PlayerUtility;
import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Álvaro Mariano on 17/04/2017.
 */
public class SaveCommand implements CommandExecutor {
    private static Ekko main = Ekko.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("ekko")) {
            if (!commandSender.hasPermission("ekko.command.save")) {
                commandSender.sendMessage(Color.translate("&cYou don't have permission."));
            } else {
                if (strings.length == 0) {
                    commandSender.sendMessage(Color.translate("&cUsage: /ekko reload || save"));
                    return true;
                }
                if (strings.length >= 1) {
                    if (strings[0].equalsIgnoreCase("reload")) {
                        Ekko.getInstance().reloadConfig();
                        commandSender.sendMessage(Color.translate("&eYou have reloaded the configuration file."));
                        return true;
                    }
                    if (strings[0].equalsIgnoreCase("save")) {
                        if (!(Faction.getFactions().isEmpty())) {
                            System.out.println("Preparing to save " + Faction.getFactions().size() + " factions.");
                            MongoCollection sCollection = main.getMongoDatabase().getCollection("systemFactions");
                            MongoCollection pCollection = main.getMongoDatabase().getCollection("playerFactions");
                            for (Faction faction : Faction.getFactions()) {
                                if (faction instanceof PlayerFaction) {
                                    PlayerFaction playerFaction = (PlayerFaction) faction;

                                    Document dbo = new Document();

                                    dbo.put("uuid", playerFaction.getUuid().toString());
                                    dbo.put("leader", playerFaction.getLeader().toString());
                                    dbo.put("name", playerFaction.getName());
                                    dbo.put("name_lower", playerFaction.getName().toLowerCase());
                                    dbo.put("dtr", playerFaction.getDeathsTillRaidable().doubleValue());
                                    dbo.put("balance", playerFaction.getBalance());

                                    if (playerFaction.isFrozen()) {
                                        dbo.put("freezeLength", playerFaction.getFreezeInformation()[0]);
                                        dbo.put("freezeInit", playerFaction.getFreezeInformation()[1]);
                                    }

                                    if (playerFaction.getHome() != null) {
                                        dbo.put("home", playerFaction.getHome());
                                    }

                                    if (playerFaction.getAnnouncement() != null) {
                                        dbo.put("announcement", playerFaction.getAnnouncement());
                                    }

                                    List<String> officers = new ArrayList<>();
                                    List<String> members = new ArrayList<>();
                                    List<String> allies = new ArrayList<>();
                                    List<String> requestedAllies = new ArrayList<>();
                                    Document invitedPlayers = new Document();
                                    List<String> claims = new ArrayList<>();

                                    officers.addAll(PlayerUtility.getConvertedUuidSet(playerFaction.getOfficers()));
                                    members.addAll(PlayerUtility.getConvertedUuidSet(playerFaction.getMembers()));
                                    allies.addAll(PlayerUtility.getConvertedUuidSet(playerFaction.getAllyUuids()));
                                    requestedAllies.addAll(PlayerUtility.getConvertedUuidSet(playerFaction.getRequestedAllies()));

                                    for (UUID invitedPlayer : playerFaction.getInvitedPlayers().keySet()) {
                                        invitedPlayers.put(invitedPlayer.toString(), playerFaction.getInvitedPlayers().get(invitedPlayer));
                                    }

                                    for (Claim claim : playerFaction.getClaims()) {
                                        claims.add(claim.getWorldName() + ";" + claim.getFirstX() + ";" + claim.getFirstZ() + ";" + claim.getSecondX() + ";" + claim.getSecondZ());
                                    }

                                    dbo.put("officers", officers);
                                    dbo.put("members", members);
                                    dbo.put("allies", allies);
                                    dbo.put("requestedAllies", requestedAllies);
                                    dbo.put("invitedPlayers", invitedPlayers);
                                    dbo.put("claims", claims);


                                    pCollection.replaceOne(eq("uuid", playerFaction.getUuid().toString()), dbo, new UpdateOptions().upsert(true));
                                } else {
                                    SystemFaction systemFaction = (SystemFaction) faction;

                                    Document dbo = new Document();

                                    dbo.put("uuid", systemFaction.getUuid().toString());
                                    dbo.put("deathban", systemFaction.isDeathban());
                                    dbo.put("color", systemFaction.getColor().name());
                                    dbo.put("name", systemFaction.getName());
                                    dbo.put("name_lower", systemFaction.getName().toLowerCase());

                                    if (systemFaction.getAnnouncement() != null) {
                                        dbo.put("announcement", systemFaction.getAnnouncement());
                                    }

                                    if (systemFaction.getHome() != null) {
                                        dbo.put("home", systemFaction.getHome());
                                    }

                                    BasicDBList claims = new BasicDBList();
                                    for (Claim claim : systemFaction.getClaims()) {
                                        claims.add(claim.getWorldName() + ";" + claim.getFirstX() + ";" + claim.getFirstZ() + ";" + claim.getSecondX() + ";" + claim.getSecondZ());
                                    }

                                    dbo.put("claims", claims);

                                    sCollection.replaceOne(eq("uuid", systemFaction.getUuid().toString()), dbo, new UpdateOptions().upsert(true));
                                }
                            }
                        }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                            commandSender.sendMessage(Color.translate("&eYou have saved the configuration file and all the worlds."));
                            return true;
                        }
                    }
                }
            }
        return false;
    }
}
