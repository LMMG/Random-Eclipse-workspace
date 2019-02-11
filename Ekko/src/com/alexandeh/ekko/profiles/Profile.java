package com.alexandeh.ekko.profiles;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.claims.ClaimPillar;
import com.alexandeh.ekko.factions.claims.ClaimProfile;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.files.ConfigFile;
import com.alexandeh.ekko.profiles.teleport.ProfileTeleportTask;
import com.alexandeh.ekko.utils.player.PlayerUtility;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

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
@Setter
public class Profile {


    private static Map<UUID, Profile> profileMap = new HashMap();

    private PlayerFaction faction;
    private ProfileChatType chatType;
    private UUID uuid;
    private ClaimProfile claimProfile;
    private Claim lastInside;
    private boolean viewingMap;
    private Set<ClaimPillar> mapPillars;
    private ProfileTeleportTask teleportWarmup;
    private boolean inAdminMode;

    public Profile(UUID uuid) {
        this.uuid = uuid;

        mapPillars = new HashSet<>();
        chatType = ProfileChatType.PUBLIC;

        for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
            if (playerFaction.getAllPlayerUuids().contains(uuid)) {
                faction = playerFaction;
            }
        }

        profileMap.put(uuid, this);
    }

    public Profile(Player player) {
        this(player.getUniqueId());
    }

    public void updateTab() {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {

            Scoreboard scoreboard;
            boolean newScoreboard = false;

            if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
                scoreboard = player.getScoreboard();
            } else {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                newScoreboard = true;
            }

            Ekko nebula = Ekko.getInstance();
            ConfigFile mainConfig = nebula.getMainConfig();

            if (mainConfig.getBoolean("TAB_LIST.ENABLED")) {
                Team friendly = getExistingOrCreateNewTeam("friendly", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FRIENDLY_COLOR")));
                Team ally = getExistingOrCreateNewTeam("ally", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ALLY_COLOR")));
                Team enemy = getExistingOrCreateNewTeam("enemy", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ENEMY_COLOR")));


                if (faction != null) {

                    for (Player friendlyPlayer : faction.getOnlinePlayers()) {
                        if (friendlyPlayer.equals(player)) continue;
                        if (!(friendly.hasEntry(friendlyPlayer.getName()))) {
                            friendly.addEntry(friendlyPlayer.getName());
                        }
                    }

                    for (PlayerFaction allyFaction : faction.getAllies()) {
                        for (Player allyPlayer : allyFaction.getOnlinePlayers()) {
                            if (!(ally.hasEntry(allyPlayer.getName()))) {
                                ally.addEntry(allyPlayer.getName());
                            }
                        }
                    }

                }

                for (Player enemyPlayer : PlayerUtility.getOnlinePlayers()) {
                    if (!(enemyPlayer.getName().equals(player.getName()))) {

                        if (friendly.hasEntry(enemyPlayer.getName()) && (faction == null || !faction.getOnlinePlayers().contains(enemyPlayer))) {
                            friendly.removeEntry(enemyPlayer.getName());
                        }

                        if (ally.hasEntry(enemyPlayer.getName())) {
                            Profile enemyProfile = Profile.getByUuid(enemyPlayer.getUniqueId());
                            PlayerFaction enemyFaction = enemyProfile.getFaction();
                            if (enemyFaction == null || faction == null || !faction.getAllies().contains(enemyFaction)) {
                                ally.removeEntry(enemyPlayer.getName());
                            }
                        }

                        if (!(friendly.hasEntry(enemyPlayer.getName())) && (!(ally.hasEntry(enemyPlayer.getName())))) {
                            enemy.addEntry(enemyPlayer.getName());
                        }
                    }
                }

                if (!(friendly.hasEntry(player.getName()))) {
                    friendly.addEntry(player.getName());
                }

                if (newScoreboard) {
                    player.setScoreboard(scoreboard);
                }
            }
        }
    }

    public void updateTab(Player toUpdate) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {

            Scoreboard scoreboard;
            boolean newScoreboard = false;

            if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
                scoreboard = player.getScoreboard();
            } else {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                newScoreboard = true;
            }

            Ekko nebula = Ekko.getInstance();
            ConfigFile mainConfig = nebula.getMainConfig();

            if (mainConfig.getBoolean("TAB_LIST.ENABLED")) {
                Team friendly = getExistingOrCreateNewTeam("friendly", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.FRIENDLY_COLOR")));
                Team ally = getExistingOrCreateNewTeam("ally", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ALLY_COLOR")));
                Team enemy = getExistingOrCreateNewTeam("enemy", scoreboard, ChatColor.valueOf(mainConfig.getString("TAB_LIST.ENEMY_COLOR")));
                Team tagged_players = getExistingOrCreateNewTeam("tagged_players", scoreboard, ChatColor.DARK_RED);

                if (faction != null) {
                    if (faction.getOnlinePlayers().contains(toUpdate) && !(friendly.hasEntry(toUpdate.getName()))) {
                        friendly.addEntry(toUpdate.getName());
                    }

                    for (PlayerFaction allyFaction : faction.getAllies()) {
                        if (allyFaction.getOnlinePlayers().contains(toUpdate) && !(ally.hasEntry(toUpdate.getName()))) {
                            ally.addEntry(toUpdate.getName());
                        }
                    }
                }

                if (friendly.hasEntry(toUpdate.getName()) && (faction == null || !faction.getOnlinePlayers().contains(toUpdate))) {
                    friendly.removeEntry(toUpdate.getName());
                }

                if (ally.hasEntry(toUpdate.getName())) {
                    Profile enemyProfile = Profile.getByUuid(toUpdate.getUniqueId());
                    PlayerFaction enemyFaction = enemyProfile.getFaction();
                    if (enemyFaction == null || faction == null || !faction.getAllies().contains(enemyFaction)) {
                        ally.removeEntry(toUpdate.getName());
                    }
                }

                if (!(friendly.hasEntry(toUpdate.getName())) && (!(ally.hasEntry(toUpdate.getName())))) {
                    enemy.addEntry(toUpdate.getName());
                }

                if (tagged_players.hasEntry(toUpdate.getName()) && enemy.hasEntry(toUpdate.getName())) {
                    enemy.removeEntry(toUpdate.getName());
                }

                if (!(friendly.hasEntry(player.getName()))) {
                    Team vanished = scoreboard.getTeam("hidden");

                    if (vanished == null || !vanished.hasEntry(player.getName())) {
                        friendly.addEntry(player.getName());
                    }
                }

                if (newScoreboard) {
                    player.setScoreboard(scoreboard);
                }
            }
        }

    }

    private Team getExistingOrCreateNewTeam(String string, Scoreboard scoreboard, ChatColor prefix) {
        Team toReturn = scoreboard.getTeam(string);

        if (toReturn == null) {
            toReturn = scoreboard.registerNewTeam(string);
            toReturn.setPrefix(prefix + "");
        }

        return toReturn;
    }

    public static void sendGlobalTabUpdate() {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            getByUuid(player.getUniqueId()).updateTab();
        }
    }

    public static void sendPlayerTabUpdate(Player toUpdate) {
        for (Player player : PlayerUtility.getOnlinePlayers()) {
            getByUuid(player.getUniqueId()).updateTab(toUpdate);
        }
    }

    public static Profile getExistingByUuid(UUID uuid) {
        if (profileMap.containsKey(uuid)) {
            return profileMap.get(uuid);
        }
        return null;
    }

    public static Profile getByUuid(UUID uuid) {
        if (profileMap.containsKey(uuid)) {
            return profileMap.get(uuid);
        }
        return new Profile(uuid);
    }

    public static Map<UUID, Profile> getProfileMap() {
        return profileMap;
    }

    public static Set<Profile> getProfiles() {
        return new HashSet<>(profileMap.values());
    }

    public ClaimProfile getClaimProfile() {
        return claimProfile;
    }
}
