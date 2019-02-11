package com.alexandeh.ekko.factions;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.files.ConfigFile;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.ItemBuilder;
import com.alexandeh.ekko.utils.player.PlayerUtility;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;

/*
 * Copyright (c) 2016, Alexander Maxwell. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - The name of Alexander Maxwell may not be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@Getter
public class    Faction {

    private static Set<Faction> factions = new HashSet<>();
    private static Ekko main = Ekko.getInstance();

    public ConfigFile mainConfig = main.getMainConfig();

    @Setter
    private String name, home, announcement;

    private UUID uuid;
    private Set<Claim> claims;

    public Faction(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;

        claims = new HashSet<>();

        if (uuid == null) {
            this.uuid = UUID.randomUUID();
        /*
        "Only after generating 1 billion UUIDs every second for the next 100 years, the probability of creating just one duplicate would be about 50%." - Wikipedia
          while (getByUuid(uuid) != null) {
              uuid = UUID.randomUUID();
          }*/
        }
        factions.add(this);
    }


    public static Faction getByName(String name) {
        for (Faction faction : getFactions()) {
            if (faction.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", ""))) {
                return faction;
            }
        }
        return null;
    }

    public static Faction getByUuid(UUID uuid) {
        for (Faction faction : getFactions()) {
            if (faction.getUuid().equals(uuid)) {
                return faction;
            }
        }
        return null;
    }

    public static Set<Faction> getAllByString(String string) {
        Set<Faction> toReturn = new HashSet<>();
        for (Faction faction : factions) {
            if (!(toReturn.contains(faction))) {

                if (faction.getName().replace(" ", "").equalsIgnoreCase(string)) {
                    toReturn.add(faction);
                }

                if (faction instanceof PlayerFaction) {
                    PlayerFaction playerFaction = (PlayerFaction) faction;

                    for (UUID uuid : playerFaction.getAllPlayerUuids()) {
                        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);
                        if (offlinePlayer != null && offlinePlayer.getName().equalsIgnoreCase(string)) {
                            toReturn.add(faction);
                        }
                    }
                }

            }
        }
        return toReturn;
    }

    public boolean isNearBorder(Location l) {
        for (Claim claim : getClaims()) {
            if (claim.getWorldName().equals(l.getWorld().getName())) {
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(0, 0, 1))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(1, 0, 0))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(0, 0, -1))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(-1, 0, 0))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(-1, 0, 1))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(-1, 0, -1))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(1, 0, 1))) {
                    return true;
                }
                if (claim.isInside(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()).add(1, 0, -1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack getWand(Ekko main) {
        return new ItemBuilder(Material.valueOf(main.getMainConfig().getString("FACTION_CLAIMING.WAND.TYPE")))
                .lore(main.getMainConfig().getStringList("FACTION_CLAIMING.WAND.LORE"))
                .name(main.getMainConfig().getString("FACTION_CLAIMING.WAND.NAME"))
                .data(main.getMainConfig().getInt("FACTION_CLAIMING.WAND.DATA")).build();
    }

    public static void load() {
        MongoCollection sCollection = main.getMongoDatabase().getCollection("systemFactions");
        MongoCollection pCollection = main.getMongoDatabase().getCollection("playerFactions");

        sCollection.find().forEach(new Block() {
            @Override
            public void apply(Object obj) {
                Document dbo = (Document) obj;
                UUID uuid = UUID.fromString(dbo.getString("uuid"));
                String name = dbo.getString("name");

                final SystemFaction systemFaction = new SystemFaction(name, uuid);

                boolean deathban = dbo.getBoolean("deathban");
                ChatColor color = ChatColor.valueOf(dbo.getString("color"));

                if (dbo.containsKey("home")) {
                    systemFaction.setHome(dbo.getString("home"));
                }

                if (dbo.containsKey("announcement")) {
                    systemFaction.setAnnouncement(dbo.getString("announcement"));
                }

                systemFaction.setDeathban(deathban);
                systemFaction.setColor(color);

                List<String> claims = (List<String>) dbo.get("claims");
                for (String claim : claims) {
                    if (claim.length() >= 5) {
                        String[] claimParts = (claim).split(";");
                        final String worldName = claimParts[0];
                        final int x1 = Integer.parseInt(claimParts[1]);
                        final int z1 = Integer.parseInt(claimParts[2]);
                        final int x2 = Integer.parseInt(claimParts[3]);
                        final int z2 = Integer.parseInt(claimParts[4]);
                        new Claim(systemFaction, new int[]{x1, x2, z1, z2}, worldName);
                    }
                }
            }
        });

        final Map<PlayerFaction, Set<UUID>> allyCache = new HashMap<>();
        pCollection.find().forEach(new Block() {
           @Override
           public void apply(Object obj) {
               Document dbo = (Document) obj;

               UUID uuid = UUID.fromString(dbo.getString("uuid"));
               UUID leader = UUID.fromString(dbo.getString("leader"));
               String name = dbo.getString("name");

               double dtr = dbo.getDouble("dtr");
               int balance = dbo.getInteger("balance");
               int[] freezeInformation = null;
               String home = null;
               String announcement = null;

               Set<UUID> officers = new HashSet<>();
               Set<UUID> members = new HashSet<>();
               Map<UUID, UUID> invitedPlayers = new HashMap<>();
               if (dbo.containsKey("freezeLength") && dbo.containsKey("freezeInit")) {
                   freezeInformation = new int[]{dbo.getInteger("freezeLength"), dbo.getInteger("freezeInit")};
               }

               if (dbo.containsKey("home")) {
                   home = dbo.getString("home");
               }

               if (dbo.containsKey("announcement")) {
                   announcement = dbo.getString("announcement");
               }

               Document invitedPlayerMap = (Document) dbo.get("invitedPlayers");
               for (String key : invitedPlayerMap.keySet()) {
                   UUID invitedPlayer = UUID.fromString(key);
                   UUID invitedBy = (UUID) invitedPlayerMap.get(key);
                   invitedPlayers.put(invitedPlayer, invitedBy);
               }

               List<String> membersList = (List<String>) dbo.get("members");
               for (String member : membersList) {
                   if (member.length() == uuid.toString().length()) {
                       members.add(UUID.fromString(member));
                   }
               }

               List<String> officerList = (List<String>) dbo.get("officers");
               for (String officer : officerList) {
                   if (officer.length() == uuid.toString().length()) {
                       officers.add(UUID.fromString(officer));
                   }
               }

               final PlayerFaction playerFaction = new PlayerFaction(name, leader, uuid);
               playerFaction.setOfficers(officers);
               playerFaction.setMembers(members);
               playerFaction.setBalance(balance);
               playerFaction.setDeathsTillRaidable(BigDecimal.valueOf(dtr));
               playerFaction.setFreezeInformation(freezeInformation);
               playerFaction.setHome(home);
               playerFaction.setInvitedPlayers(invitedPlayers);
               playerFaction.setAnnouncement(announcement);

               List<String> claims = (List<String>) dbo.get("claims");
               for (String claim : claims) {
                   if (claim.length() >= 5) {
                       String[] claimParts = (claim).split(";");
                       final String worldName = claimParts[0];
                       final int x1 = Integer.parseInt(claimParts[1]);
                       final int z1 = Integer.parseInt(claimParts[2]);
                       final int x2 = Integer.parseInt(claimParts[3]);
                       final int z2 = Integer.parseInt(claimParts[4]);
                       new Claim(playerFaction, new int[]{x1, x2, z1, z2}, worldName);
                   }
               }

               List<String> allies = (List<String>) dbo.get("allies");
               for (String ally : allies) {
                   if (ally.length() == uuid.toString().length()) {
                       if (allyCache.containsKey(playerFaction)) {
                           allyCache.get(playerFaction).add(UUID.fromString(ally));
                       } else {
                           allyCache.put(playerFaction, new HashSet<>(Arrays.asList(UUID.fromString(ally))));
                       }
                   }
               }
           }
       });

        for (PlayerFaction key : allyCache.keySet()) {
            for (UUID allyUuid : allyCache.get(key)) {
                Faction allyFaction = Faction.getByUuid(allyUuid);
                if (allyFaction instanceof PlayerFaction) {
                    key.getAllies().add((PlayerFaction) allyFaction);
                }
            }
        }

        for (Player player : PlayerUtility.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            Claim claim = Claim.getProminentClaimAt(player.getLocation());
            if (claim != null) {
                profile.setLastInside(claim);
            }
            PlayerFaction playerFaction = PlayerFaction.getByPlayerName(player.getName());
            if (profile.getFaction() == null && playerFaction != null) {
                profile.setFaction(playerFaction);
            }
        }

        Profile.sendGlobalTabUpdate();
        main.setLoaded(true);
    }

    public static void save() {
        if (!(getFactions().isEmpty())) {
            System.out.println("Preparing to save " + getFactions().size() + " factions.");
            MongoCollection sCollection = main.getMongoDatabase().getCollection("systemFactions");
            MongoCollection pCollection = main.getMongoDatabase().getCollection("playerFactions");
            for (Faction faction : getFactions()) {
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
    }

    public static Set<Faction> getFactions() {
        return factions;
    }
}