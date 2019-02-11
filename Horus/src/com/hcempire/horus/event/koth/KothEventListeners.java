package com.hcempire.horus.event.koth;

import com.hcempire.horus.Horus;
import com.hcempire.horus.event.Event;
import com.hcempire.horus.event.EventManager;
import com.hcempire.horus.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class KothEventListeners implements Listener {

    public KothEventListeners() {
        new BukkitRunnable() {
            @Override
            public void run() {

                for (Event possibleEvent : EventManager.getInstance().getEvents()) {
                    if (possibleEvent instanceof KothEvent && possibleEvent.isActive()) {
                        KothEvent koth = (KothEvent) possibleEvent;

                        if (koth.isFinished()) {
                            koth.stop(false);
                            continue;
                        }

                        if (koth.getCappingPlayer() != null) {
                            Player player = koth.getCappingPlayer();

                            if (player.isDead() || !player.isValid() || !player.isOnline() || !koth.getZone().isInside(player.getLocation())) {
                                koth.setCappingPlayer(null);
                            } else {
                                if (koth.getDecisecondsLeft() % 600 == 0 && koth.getDecisecondsLeft() != koth.getCapTime() / 100 && koth.getDecisecondsLeft() != 0) {
                                    Bukkit.broadcastMessage(Horus.getInstance().getLangFile().getString("KOTH.CONTESTED").replace("%KOTH%", koth.getName()).replace("%TIME%", koth.getTimeLeft()).replace("%PLAYER%", player.getName()));
                                }
                            }
                        } else {
                            if (!(koth.isGrace())) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    Profile profile = Profile.getByPlayer(player);

                                    if (player.isDead()) {
                                        continue;
                                    }

                                    if (profile.getProtection() != null) {
                                        continue;
                                    }

                                    if (koth.getZone().isInside(player.getLocation())) {
                                        koth.setCappingPlayer(player);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }.runTaskTimerAsynchronously(Horus.getInstance(), 2L, 2L);
    }

}
