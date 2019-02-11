package net.nersa.lobby.listeners.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.nersa.lobby.Lobby;
import net.nersa.lobby.server.ServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayerListeners implements Listener {
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<String> usingClock = new ArrayList();

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onHiddenPlayerJoin(PlayerJoinEvent e) {
        ItemStack i = new ItemStack(Material.RED_ROSE);
        ItemMeta iMeta = i.getItemMeta();
        i.setItemMeta(iMeta);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (this.usingClock.contains(p.getName())) {
                p.hidePlayer(e.getPlayer());
            } else {
                p.showPlayer(e.getPlayer());
            }
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onHidePlayerInteract(PlayerInteractEvent e) {
        Action a = e.getAction();

        ItemStack is = e.getItem();
        if ((a == Action.PHYSICAL) || (is == null) || (is.getType() == Material.AIR)) {
            return;
        }
        if ((is.getType() == Material.RED_ROSE) && (is.hasItemMeta())) {
            if (this.usingClock.contains(e.getPlayer().getName())) {
                this.usingClock.remove(e.getPlayer().getName());
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    e.getPlayer().showPlayer(p);
                }
            } else {
                this.usingClock.add(e.getPlayer().getName());
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    e.getPlayer().hidePlayer(p);
                }
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketFillEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickItem(PlayerPickupItemEvent e) {
        e.setCancelled(true);
        e.getItem().remove();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
    }

    public static void sendPlayerToServer(Player player, ServerInfo server) {
        sendPlayerToServer(player, server.name);
    }

    public static void sendPlayerToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Lobby.getInstance(), "BungeeCord", out.toByteArray());
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