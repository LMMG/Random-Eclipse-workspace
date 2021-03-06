/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.reflect.StructureModifier
 *  com.comphenix.protocol.wrappers.EnumWrappers
 *  com.comphenix.protocol.wrappers.EnumWrappers$ClientCommand
 *  net.minecraft.util.gnu.trove.map.TObjectLongMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.parapvp.util.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.ClientCommand;
import com.parapvp.base.BasePlugin;
import com.parapvp.util.player.PlayerCache;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerUtil {
    private static final Map<Player, Location> frozen = new HashMap<Player, Location>();
    private static final Map<Player, PlayerCache> playerCaches = new HashMap<Player, PlayerCache>();
    private static final TObjectLongMap<Player> lastSent = new TObjectLongHashMap();

    public static void respawn(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Client.CLIENT_COMMAND);
        packet.getClientCommands().writeSafely(0, (ClientCommand)EnumWrappers.ClientCommand.PERFORM_RESPAWN);
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(player, packet);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void wipe(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0.0f);
        player.setLevel(0);
        player.setHealth(((CraftPlayer)player).getMaxHealth());
        player.setFoodLevel(20);
        player.setRemainingAir(player.getMaximumAir());
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        for (PotionEffect pe : player.getActivePotionEffects()) {
            player.removePotionEffect(pe.getType());
        }
    }

    public static void freeze(Player player) {
        frozen.put(player, player.getLocation());
    }

    public static boolean thaw(Player player) {
        return frozen.remove((Object)player) != null;
    }

    public static boolean isFrozen(Player player) {
        return frozen.containsKey((Object)player);
    }

    public static void cache(Player player) {
        playerCaches.put(player, new PlayerCache(player));
    }

    public static void restore(Player player) {
        PlayerCache playerCache = playerCaches.get((Object)player);
        if (playerCache != null) {
            playerCache.apply(player);
        }
    }

    public static PlayerCache getCache(Player player) {
        return playerCaches.get((Object)player);
    }

    static {
        Bukkit.getPluginManager().registerEvents(new Listener(){

            @EventHandler
            public void onMove(PlayerMoveEvent event) {
                Location from = event.getFrom();
                Location to = event.getTo();
                if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                    Player player = event.getPlayer();
                    Location location = (Location)frozen.get((Object)player);
                    if (location != null && (to.getBlockX() != location.getBlockX() || to.getBlockZ() != location.getBlockZ() || Math.abs(to.getBlockY() - location.getBlockY()) >= 2)) {
                        location.setYaw(to.getYaw());
                        location.setPitch(to.getPitch());
                        event.setTo(location);
                        long millis = System.currentTimeMillis();
                        long lastSentMillis = lastSent.get((Object)player);
                        if (lastSentMillis != lastSent.getNoEntryValue() && millis - lastSentMillis <= 3000) {
                            return;
                        }
                        lastSent.put(player, millis);
                        player.sendMessage((Object)ChatColor.YELLOW + "You are currently " + (Object)ChatColor.AQUA + "frozen" + (Object)ChatColor.YELLOW + "!");
                    }
                }
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                Player player = event.getPlayer();
                frozen.remove((Object)player);
                lastSent.remove((Object)player);
                PlayerCache playerCache = (PlayerCache)playerCaches.remove((Object)player);
                if (playerCache != null) {
                    playerCache.apply(player);
                }
            }
        }, (Plugin)BasePlugin.getPlugin());
    }

}

