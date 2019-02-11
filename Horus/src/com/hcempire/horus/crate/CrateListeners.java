package com.hcempire.horus.crate;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrateListeners implements Listener {

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (inventory.getTitle().contains("Items - 1/1") &&  player.hasPermission("crate.admin")) {
            Crate crate = Crate.getByName(ChatColor.stripColor(inventory.getItem(4).getItemMeta().getLore().get(0).replace("Crate: ", "")));
            if (crate != null) {
                List<ItemStack> toAdd = new ArrayList<>();

                for (int i = 9; i < inventory.getSize(); i++) {
                    ItemStack itemStack = inventory.getItem(i);
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        toAdd.add(itemStack);
                    }
                }

                crate.getItems().clear();
                crate.getItems().addAll(toAdd);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (inventory.getTitle().contains("Items - 1/1") &&  player.hasPermission("crate.admin")) {
            if (event.getRawSlot() <= 8) {
                event.setCancelled(true);
            }

            if (event.getClick().name().contains("SHIFT")) {
                ItemStack itemStack = event.getCurrentItem();
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    Inventory clickedInventory = event.getClickedInventory();

                    if (clickedInventory != null && clickedInventory.contains(itemStack) && clickedInventory.equals(inventory)) {
                        return;
                    }

                    event.setCancelled(true);
                    player.getInventory().removeItem(itemStack);

                    int position = 0;
                    for (int i = 0; i < inventory.getSize(); i++) {
                        if (i > 8) {
                            ItemStack slot = inventory.getItem(i);
                            if (slot == null || slot.getType() == Material.AIR) {
                                position = i;
                                break;
                            }
                        }
                    }

                    inventory.setItem(position, itemStack);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            if (block.getType() == Material.ENDER_CHEST) {
                event.setCancelled(true);
                if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
                    Crate crate = Crate.getByKey(event.getItem());

                    if (crate != null) {
                        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.BLUE + crate.getName() + " crate");

                        for (int i = 0; i < 3 + getBonus(player); i++) {
                            inventory.setItem(i, crate.getItems().get(new Random().nextInt(crate.getItems().size())));
                        }

                        ItemStack itemStack = player.getItemInHand();
                        if (itemStack.getAmount() > 1) {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                        } else {
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }

                        player.openInventory(inventory);
                    }
                }
            }
        }
    }

    public int getBonus(Player player) {
        int toReturn = 0;

        for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
            String perm = info.getPermission();
            if (perm.startsWith("crate.bonus.")) {
                int temp = 0;
                try {
                    temp = Integer.parseInt(perm.replace("crate.bonus.", "").replace(" ", ""));
                } catch (NumberFormatException ignored) {
                }

                if (toReturn > 0 && temp < toReturn) {
                    continue;
                }

                toReturn = temp;
            }
        }

        return toReturn;
    }

}
