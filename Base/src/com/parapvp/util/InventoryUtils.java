/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.Material
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.material.MaterialData
 */
package com.parapvp.util;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

public final class InventoryUtils {
    public static final int DEFAULT_INVENTORY_WIDTH = 9;
    public static final int MINIMUM_INVENTORY_HEIGHT = 1;
    public static final int MINIMUM_INVENTORY_SIZE = 9;
    public static final int MAXIMUM_INVENTORY_HEIGHT = 6;
    public static final int MAXIMUM_INVENTORY_SIZE = 54;
    public static final int MAXIMUM_SINGLE_CHEST_SIZE = 27;
    public static final int MAXIMUM_DOUBLE_CHEST_SIZE = 54;

    public static ItemStack[] deepClone(ItemStack[] origin) {
        Preconditions.checkNotNull((Object)origin, (Object)"Origin cannot be null");
        ItemStack[] cloned = new ItemStack[origin.length];
        for (int i = 0; i < origin.length; ++i) {
            ItemStack next = origin[i];
            cloned[i] = next == null ? null : next.clone();
        }
        return cloned;
    }

    public static int getSafestInventorySize(int initialSize) {
        return (initialSize + 8) / 9 * 9;
    }

    public static void removeItem(Inventory inventory, Material type, short data, int quantity) {
        ItemStack[] contents = inventory.getContents();
        boolean compareDamage = type.getMaxDurability() == 0;
        block0 : for (int i = quantity; i > 0; --i) {
            for (ItemStack content : contents) {
                if (content == null || content.getType() != type || compareDamage && content.getData().getData() != data) continue;
                if (content.getAmount() <= 1) {
                    inventory.removeItem(new ItemStack[]{content});
                    continue block0;
                }
                content.setAmount(content.getAmount() - 1);
                continue block0;
            }
        }
    }

    public static int countAmount(Inventory inventory, Material type, short data) {
        ItemStack[] contents = inventory.getContents();
        boolean compareDamage = type.getMaxDurability() == 0;
        int counter = 0;
        for (ItemStack item : contents) {
            if (item == null || item.getType() != type || compareDamage && item.getData().getData() != data) continue;
            counter += item.getAmount();
        }
        return counter;
    }

    public static boolean isEmpty(Inventory inventory) {
        return InventoryUtils.isEmpty(inventory, true);
    }

    public static boolean isEmpty(Inventory inventory, boolean checkArmour) {
        ItemStack[] contents2;
        boolean result = true;
        ItemStack[] contents = contents2 = inventory.getContents();
        for (ItemStack content : contents2) {
            if (content == null || content.getType() == Material.AIR) continue;
            result = false;
            break;
        }
        if (!result) {
            return false;
        }
        if (checkArmour && inventory instanceof PlayerInventory) {
            ItemStack[] armorContents;
            contents = armorContents = ((PlayerInventory)inventory).getArmorContents();
            for (ItemStack content2 : armorContents) {
                if (content2 == null || content2.getType() == Material.AIR) continue;
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean clickedTopInventory(InventoryDragEvent event)
    {
      InventoryView view = event.getView();
      Inventory topInventory = view.getTopInventory();
      if (topInventory == null) {
        return false;
      }
      boolean result = false;
      Set<Map.Entry<Integer, ItemStack>> entrySet = event.getNewItems().entrySet();
      int size = topInventory.getSize();
      for (Map.Entry<Integer, ItemStack> entry : entrySet) {
        if (((Integer)entry.getKey()).intValue() < size)
        {
          result = true;
          break;
        }
      }
      return result;
    }
  }