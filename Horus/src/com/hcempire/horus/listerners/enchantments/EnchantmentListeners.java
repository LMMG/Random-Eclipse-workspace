package com.hcempire.horus.listerners.enchantments;

import com.hcempire.horus.Horus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Álvaro Mariano on 19/04/2017.
 */
public class EnchantmentListeners implements Listener {
    private static Horus main = Horus.getInstance();

    public EnchantmentListeners() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getInventory().getBoots().getItemMeta().getLore().contains("Speed II")) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 255, 1));
                    }
                }
            }
        }.runTaskTimerAsynchronously(main, 20L, 2L);
    }
}
