package rip.kohi.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.kohi.commands.StaffModeCommand;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Owner on 31/08/2017.
 */
public class StaffModeEvent implements Listener {

    private static Random random = new Random();
    private static ArrayList<UUID> players = new ArrayList<UUID>();

    @EventHandler
    public void staffModeFight(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (StaffModeCommand.staffMode.contains(((Player) e.getDamager()).getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onModPickup(PlayerPickupItemEvent e) {
        if (e.getPlayer() == null) {
            return;
        }
        if (StaffModeCommand.staffMode.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        } else {
        }
    }

    @EventHandler
    public void modPlaceBlock(BlockPlaceEvent e) {
        if (e.getPlayer() == null) {
            return;
        }
        if (e.getBlock() == null) {
            return;
        }
        if (StaffModeCommand.staffMode.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        } else {
        }
    }

    public static void put(Player player) {
        player.getInventory().clear();
        if ((!player.getGameMode().equals(GameMode.CREATIVE)) && (player.hasPermission("kohiffa.staffmode"))) {
            player.setGameMode(GameMode.CREATIVE);
        }

        //Inventory Inspect
        ItemStack invInspect = new ItemStack(340);
        ItemMeta invInspectM = invInspect.getItemMeta();
        invInspectM.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lInventory Inspect"));
        invInspect.setItemMeta(invInspectM);

        //Vanish feather
        ItemStack vanish = new ItemStack(Material.FEATHER);
        ItemMeta vanishm = vanish.getItemMeta();
        vanishm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lVanish Tool"));
        vanish.setItemMeta(vanishm);

        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));

        player.getInventory().clear();
        player.getInventory().setItem(0, vanish);
        player.getInventory().setItem(4, invInspect);
    }

    public static void remove(Player player) {
        if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        player.getInventory().clear();
        player.getInventory().setLeggings(null);
        player.getInventory().setHelmet(null);
    }

    @EventHandler
    public void onPlayerFeatherClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action a = e.getAction();
        ItemStack is = e.getItem();
        if (StaffModeCommand.staffMode.contains(player.getName())) {
            if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (e.getItem() == null)) {
                return;
            }
            if ((a == Action.RIGHT_CLICK_AIR) || (a == Action.RIGHT_CLICK_BLOCK) || (is.getType() == Material.FEATHER)) {
                VanishEvent.toggleVanish(player);
                return;
            }
        }
    }
}
