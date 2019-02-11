/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.MinecraftServer
 *  net.minecraft.util.com.google.common.primitives.Ints
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.parapvp.base.task;

import com.parapvp.base.BasePlugin;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.util.com.google.common.primitives.Ints;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AutoRestartHandler {
    private static final int[] ALERT_SECONDS = new int[]{600, 300, 270, 240, 210, 180, 150, 120, 90, 60, 30, 15, 10, 5, 4, 3, 2, 1};
    private static final long TICKS_DAY = TimeUnit.DAYS.toMillis(1);
    private final BasePlugin plugin;
    private long current = Long.MIN_VALUE;
    private String reason;
    private BukkitTask task;

    public AutoRestartHandler(BasePlugin plugin) {
        this.plugin = plugin;
        this.scheduleRestart(TICKS_DAY);
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isPendingRestart() {
        return this.task != null && this.current != Long.MIN_VALUE;
    }

    public void cancelRestart() {
        if (this.isPendingRestart()) {
            this.task.cancel();
            this.task = null;
            this.current = Long.MIN_VALUE;
        }
    }

    public long getRemainingMilliseconds() {
        return this.getRemainingTicks() * 50;
    }

    public long getRemainingTicks() {
        return this.current - (long)MinecraftServer.currentTick;
    }

    public void scheduleRestart(long milliseconds) {
        this.scheduleRestart(milliseconds, null);
    }

    public void scheduleRestart(long millis, final String reason) {
        this.cancelRestart();
        this.reason = reason;
        this.current = (long)(MinecraftServer.currentTick + 20) + millis / 50;
        this.task = new BukkitRunnable(){

            public void run() {
                long remainingTicks;
                if (AutoRestartHandler.this.current == 0) {
                    this.cancel();
                }
                if ((remainingTicks = AutoRestartHandler.this.getRemainingTicks()) <= 0) {
                    this.cancel();
                    Bukkit.shutdown((String)((Object)ChatColor.RED + "Server is restarting, We will be back soon. " + (reason == null ? "" : new StringBuilder().append((Object)ChatColor.AQUA).append("\n\n ").append(reason).toString())));
                    return;
                }
                long remainingMillis = remainingTicks * 50;
                if (Ints.contains((int[])ALERT_SECONDS, (int)((int)(remainingMillis / 1000)))) {
                    Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "Server is restarting in " + (Object)ChatColor.DARK_RED + DurationFormatUtils.formatDurationWords((long)remainingMillis, (boolean)true, (boolean)true) + (Object)ChatColor.RED + (reason == null || reason.isEmpty() ? "." : new StringBuilder().append(" [").append((Object)ChatColor.GRAY).append(reason).append((Object)ChatColor.RED).append("].").toString())));
                }
            }
        }.runTaskTimer((Plugin)this.plugin, 20, 20);
    }

}

