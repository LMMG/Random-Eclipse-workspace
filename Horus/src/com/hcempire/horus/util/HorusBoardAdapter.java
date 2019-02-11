package com.hcempire.horus.util;

import com.alexandeh.ekko.files.ConfigFile;
import com.alexandeh.ekko.profiles.teleport.ProfileTeleportTask;
import com.alexandeh.ekko.profiles.teleport.ProfileTeleportType;
import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.hcempire.horus.Horus;
import com.hcempire.horus.event.Event;
import com.hcempire.horus.event.EventManager;
import com.hcempire.horus.event.koth.KothEvent;
import com.hcempire.horus.listerners.sotw.SotwHandler;
import com.hcempire.horus.profile.Profile;
import com.hcempire.horus.profile.cooldown.ProfileCooldown;
import com.hcempire.horus.profile.cooldown.ProfileCooldownType;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HorusBoardAdapter implements BoardAdapter {

    public static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    private Horus main;
    private ConfigFile configFile;

    public HorusBoardAdapter(Horus main) {
        this.main = main;
        this.configFile = main.getScoreboardFile();
    }

    @Override
    public String getTitle(Player player) {
        return configFile.getString("TITLE");
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> set) {
        List<String> toReturn = new ArrayList<>();
        Profile profile = Profile.getByPlayer(player);

        for (String line : configFile.getStringList("LINES")) {

            if (line.contains("%SOTW%")) {
                SotwHandler.SotwRunnable sotwRunnable = Horus.getInstance().getSotwTimer().getSotwRunnable();
                if (sotwRunnable != null) {
                    toReturn.add(line.replace("%SOTW%", DurationFormatter.getRemaining(sotwRunnable.getRemaining(), true)));
                }
                continue;
            }

            if (line.contains("%HOME%")) {
                com.alexandeh.ekko.profiles.Profile ekkoProfile = com.alexandeh.ekko.profiles.Profile.getByUuid(player.getUniqueId());

                ProfileTeleportTask teleportTask = ekkoProfile.getTeleportWarmup();
                if (teleportTask != null && teleportTask.getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    toReturn.add(line.replace("%HOME%", SECONDS_FORMATTER.format(((teleportTask.getEvent().getInit() + (teleportTask.getEvent().getTime() * 1000) + 50) - System.currentTimeMillis()) / 1000)));
                }

                continue;
            }

            if (line.contains("%STUCK%")) {
                com.alexandeh.ekko.profiles.Profile ekkoProfile = com.alexandeh.ekko.profiles.Profile.getByUuid(player.getUniqueId());

                ProfileTeleportTask teleportTask = ekkoProfile.getTeleportWarmup();
                if (teleportTask != null && teleportTask.getEvent().getTeleportType() == ProfileTeleportType.STUCK_TELEPORT) {
                    toReturn.add(line.replace("%STUCK%", DurationFormatUtils.formatDuration((long) ((teleportTask.getEvent().getInit() + (teleportTask.getEvent().getTime() * 1000) + 500) - System.currentTimeMillis()), "mm:ss")));
                }

                continue;
            }

            if (line.contains("%KOTH%")) {
                for (Event event : EventManager.getInstance().getEvents()) {
                    if (event instanceof KothEvent && event.isActive()) {
                        toReturn.addAll(event.getScoreboardText());
                    }
                }

                continue;
            }

            if (line.contains("%LOGOUT%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.LOGOUT);

                if (cooldown != null) {
                    toReturn.add(line.replace("%LOGOUT%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%SPAWN_TAG%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.SPAWN_TAG);

                if (cooldown != null) {
                    toReturn.add(line.replace("%SPAWN_TAG%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%ENDER_PEARL%")) {
                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.ENDER_PEARL);

                if (cooldown != null) {
                    toReturn.add(line.replace("%ENDER_PEARL%", cooldown.getTimeLeft()));
                }

                continue;
            }

            if (line.contains("%PVP_PROTECTION%")) {
                if (profile.getProtection() != null && profile.isLeftSpawn()) {
                    toReturn.add(line.replace("%PVP_PROTECTION%", profile.getProtection().getTimeLeft()));
                }
                continue;
            }

            if (line.contains("%CLASS%") || line.contains("%CLASS_WARMUP%")) {
                if (profile.getKitWarmup() != null) {
                    toReturn.add(line.replace("%CLASS%", profile.getKitWarmup().getKit().getName()).replace("%CLASS_WARMUP%", profile.getKitWarmup().getTimeLeft()));
                }
                continue;
            }

            if (line.contains("%BARD%")) {
                if (profile.getEnergy() != null) {
                    for (String string : main.getScoreboardFile().getStringList("PLACE_HOLDER.BARD")) {
                        toReturn.add(string.replace("%ENERGY%", profile.getEnergy().getFormattedString()));
                    }
                }
                continue;
            }

            toReturn.add(line);
        }

        if (toReturn.size() <= 2) {
            return null;
        }

        return toReturn;
    }
}
