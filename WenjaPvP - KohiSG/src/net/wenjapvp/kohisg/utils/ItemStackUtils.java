package net.wenjapvp.kohisg.utils;

import com.google.common.base.Preconditions;

import net.wenjapvp.kohisg.KohiSG;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemStackUtils
{
    public static void updateInventory(Player player)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                player.updateInventory();
            }
        }.runTaskLater(KohiSG.getInstance(), 1L);
    }

    public static ItemStack setItemLore(ItemStack item, List<String> lore)
    {
        Preconditions.checkNotNull(item);
        Preconditions.checkNotNull(lore);
        Preconditions.checkState(item.getType() != Material.AIR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack setItemTitle(ItemStack item, String title)
    {
        Preconditions.checkNotNull(item);
        Preconditions.checkNotNull(title);
        Preconditions.checkState(item.getType() != Material.AIR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(title);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack[] deepCopy(ItemStack[] item)
    {
        ItemStack[] copy = new ItemStack[item.length];
        for (int i = 0; i < item.length; i++)
        {
            ItemStack from = item[i];
            if (from != null) {
                copy[i] = from.clone();
            }
        }
        return copy;
    }

    public static boolean isEmpty(ItemStack item)
    {
        return (item == null) || (item.getType() == Material.AIR);
    }

    public static boolean isArmor(Material material)
    {
        return (isHelmet(material)) || (isChestPlate(material)) || (isLegging(material)) || (isBoot(material));
    }

    public static boolean isHelmet(Material material)
    {
        switch (material)
        {
            case GOLD_HELMET:
            case LEATHER_HELMET:
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case IRON_HELMET:
                return true;
        }
        return false;
    }

    public static boolean isChestPlate(Material material)
    {
        switch (material)
        {
            case GOLD_CHESTPLATE:
            case LEATHER_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case IRON_CHESTPLATE:
                return true;
        }
        return false;
    }

    public static boolean isLegging(Material material)
    {
        switch (material)
        {
            case GOLD_LEGGINGS:
            case LEATHER_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case IRON_LEGGINGS:
                return true;
        }
        return false;
    }

    public static boolean isBoot(Material material)
    {
        switch (material)
        {
            case GOLD_BOOTS:
            case LEATHER_BOOTS:
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case IRON_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean isGoldenArmor(Material material)
    {
        switch (material)
        {
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean isChainArmor(Material material)
    {
        switch (material)
        {
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean isIronArmor(Material material)
    {
        switch (material)
        {
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean isDiamondArmor(Material material)
    {
        switch (material)
        {
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean isLeatherArmor(Material material)
    {
        switch (material)
        {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean canInteract(Material material)
    {
        if ((material == null) || (!material.isBlock())) {
            return false;
        }
        switch (material)
        {
            case DISPENSER:
            case NOTE_BLOCK:
            case BED_BLOCK:
            case CHEST:
            case WORKBENCH:
            case FURNACE:
            case BURNING_FURNACE:
            case WOODEN_DOOR:
            case LEVER:
            case REDSTONE_ORE:
            case STONE_BUTTON:
            case JUKEBOX:
            case CAKE_BLOCK:
            case DIODE_BLOCK_ON:
            case DIODE_BLOCK_OFF:
            case TRAP_DOOR:
            case FENCE_GATE:
            case ENCHANTMENT_TABLE:
            case BREWING_STAND:
            case DRAGON_EGG:
            case ENDER_CHEST:
            case COMMAND:
            case BEACON:
            case WOOD_BUTTON:
            case ANVIL:
            case TRAPPED_CHEST:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
            case HOPPER:
            case DROPPER:
                return true;
        }
        return false;
    }

    public static String getTitle(ItemStack itemStack)
    {
        if (itemStack.hasItemMeta())
        {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName()) {
                return meta.getDisplayName();
            }
        }
        return "";
    }
}
