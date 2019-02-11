package io.hopt.listener.event;

import io.hopt.Nexus;
import io.hopt.selector.LobbySelector;
import io.hopt.selector.ServerSelector;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener {

    public static void spawn(World world, Player player) {
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', ""));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.getInventory().clear();
        spawn(player.getWorld(), player);
        player.setWalkSpeed(0.5F);
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().setItem(Nexus.config.getInt("ITEM.SERVERSELECTOR.INV"), ServerSelector.selector);
        player.getInventory().setItem(Nexus.config.getInt("ITEM.LOBBYSELECTOR.INV"), LobbySelector.lobbySelector);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        for(String message : Nexus.config.getStringList("MESSAGE_JOIN")) {
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getName().equals("Hopt")) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cant break blocks in the hub!");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cant place blocks in the hub!");
        }
    }

    @EventHandler
    public void onPlayerPick(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onplayer(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (Nexus.config.getBoolean("FEATURE.TALK")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cant talk in hub!");
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onJoindsa(PlayerJoinEvent e) {
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }
}
