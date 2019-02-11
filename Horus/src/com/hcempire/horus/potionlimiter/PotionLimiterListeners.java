package com.hcempire.horus.potionlimiter;

import com.hcempire.horus.Horus;
import com.hcempire.horus.enchantmentlimiter.EnchantmentLimiter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class PotionLimiterListeners implements Listener {

    private static Horus main = Horus.getInstance();

    @EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event) {
        ItemStack itemStack = event.getPotion().getItem();
        if (PotionLimiter.getInstance().isBlocked(itemStack.getDurability())) {

            if (event.getPotion().getShooter() instanceof Player) {
                Player player = (Player) event.getPotion().getShooter();
                player.sendMessage(main.getLangFile().getString("POTION_LIMITER.BLOCKED"));
                player.updateInventory();
            }

            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.POTION) {
            if (PotionLimiter.getInstance().isBlocked(event.getItem().getDurability())) {
                player.sendMessage(main.getLangFile().getString("POTION_LIMITER.BLOCKED"));
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }

}
