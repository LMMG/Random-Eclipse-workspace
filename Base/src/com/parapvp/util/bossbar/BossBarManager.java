/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.parapvp.util.bossbar;

import com.google.common.base.Preconditions;
import com.parapvp.base.BasePlugin;
import com.parapvp.util.bossbar.BossBar;
import com.parapvp.util.bossbar.BossBarEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BossBarManager {
    private static final Map<UUID, BossBarEntry> bossBars = new HashMap<UUID, BossBarEntry>();
    private static JavaPlugin plugin;

    public static void hook() {
        Preconditions.checkArgument((boolean)(plugin == null), (Object)"BossBarManager is already hooked");
        plugin = BasePlugin.getPlugin();
    }

    public static void unhook() {
        Preconditions.checkArgument((boolean)(plugin != null), (Object)"BossBarManager is already unhooked");
        for (UUID uuid : bossBars.keySet()) {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player == null) continue;
            BossBarManager.hideBossBar(player);
        }
        plugin = null;
    }

    public static Map<UUID, BossBarEntry> getBossBars() {
        return bossBars;
    }

    public static boolean isShowingBossBar(Player player) {
        return BossBarManager.getShownBossBar(player) != null;
    }

    public static BossBar getShownBossBar(Player player) {
        UUID uuid = player.getUniqueId();
        BossBarEntry bossBarEntry = bossBars.get(uuid);
        return bossBarEntry != null ? bossBarEntry.getBossBar() : null;
    }

    public static BossBar hideBossBar(Player player) {
        BossBarEntry entry = bossBars.get(player.getUniqueId());
        if (entry == null) {
            return null;
        }
        BossBar bossBar = entry.getBossBar();
        BukkitTask bukkitTask = entry.getCancelTask();
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)bossBar.destroyPacket);
        bossBars.remove(player.getUniqueId());
        return bossBar;
    }

    public static void showBossBar(BossBar bossBar, Player player) {
        BossBarManager.showBossBar(bossBar, player, 0);
    }

    public static void showBossBar(BossBar bossBar, final Player player, long ticks) {
        BukkitTask bukkitTask;
        BossBar current = BossBarManager.getShownBossBar(player);
        if (current != null) {
            if (current.equals(bossBar)) {
                return;
            }
            BossBarManager.hideBossBar(player);
        }
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket((Packet)bossBar.spawnPacket);
        if (ticks <= 0) {
            bukkitTask = null;
        } else {
            Preconditions.checkArgument((boolean)(plugin != null), (Object)"Cannot start destroy runnable as plugin wasn't hooked correctly.");
            bukkitTask = new BukkitRunnable(){

                public void run() {
                    BossBarManager.hideBossBar(player);
                }
            }.runTaskLater((Plugin)plugin, ticks);
        }
        bossBars.put(player.getUniqueId(), new BossBarEntry(bossBar, bukkitTask));
        connection.sendPacket((Packet)bossBar.spawnPacket);
    }

}

