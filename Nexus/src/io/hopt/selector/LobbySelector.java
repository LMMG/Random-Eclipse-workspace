package io.hopt.selector;

import io.hopt.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 21/06/2017.
 */
public class LobbySelector implements Listener {
    static List<String> lobby1Lore = new ArrayList<>();

    static {
        lobby1Lore.add(ChatColor.translateAlternateColorCodes('&', "&cLobby-01"));
        lobby1Lore.add(ChatColor.translateAlternateColorCodes('&', "&cPlayers: " + "0"));
    }

    public static ItemStack lobby1 = ItemStackBuilder.get(Material.BOOK, 1, (short) 0, "&b&lLobby-01", lobby1Lore);
    public static ItemStack lobbySelector = ItemStackBuilder.get(Material.NETHER_STAR, 1, (short) 0, "&bLobby Selector", null);
    public static Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&bLobby Selector"));


    public LobbySelector() {
        inv.setItem(0, lobby1);
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) {
            return;
        }
        if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
            e.setCancelled(false);
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
//        if (item.isSimilar(factions)) {
//            Bukkit.dispatchCommand(p, "play HCFactions");
//            p.closeInventory();
//        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(lobbySelector))) {
            Player p = e.getPlayer();
            p.openInventory(inv);
            e.setCancelled(true);
        }
    }
}
