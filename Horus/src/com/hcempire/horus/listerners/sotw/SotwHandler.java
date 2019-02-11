/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.hcempire.horus.listerners.sotw;

import com.hcempire.horus.Horus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SotwHandler {
    private SotwRunnable sotwRunnable;

    public boolean cancel() {
        if (this.sotwRunnable != null) {
            this.sotwRunnable.cancel();
            this.sotwRunnable = null;
            return true;
        }
        return false;
    }

    public void start(long millis) {
        if (this.sotwRunnable == null) {
            this.sotwRunnable = new SotwRunnable(this, millis);
            this.sotwRunnable.runTaskLater((Plugin)Horus.getInstance(), millis / 50);
        }
    }

    public SotwRunnable getSotwRunnable() {
        return this.sotwRunnable;
    }

    public static class SotwRunnable
    extends BukkitRunnable {
        private SotwHandler sotwTimer;
        private long startMillis;
        private long endMillis;

        public SotwRunnable(SotwHandler sotwTimer, long duration) {
            this.sotwTimer = sotwTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
        }

        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }

        public void run() {
            Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m---*------------------------*---"));
            Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&eThe &6&lSOTW &ehas ended. &6&lGOOD LUCK&e."));
            Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m---*------------------------*---"));
            this.cancel();
            this.sotwTimer.sotwRunnable = null;
        }
    }

}

