package net.wenjapvp.kohisg.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.wenjapvp.kohisg.KohiSG;

public class PlayerUtils
{
    public static void clearInventory(Player player)
    {
        player.closeInventory();
        PlayerInventory inventory = player.getInventory();
        inventory.setArmorContents(new ItemStack[4]);
        inventory.clear();
        updateInventory(player);
    }

    public static void updateInventory(Player player)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                player.updateInventory();
            }
        }.runTask(KohiSG.getInstance());
    }
}
