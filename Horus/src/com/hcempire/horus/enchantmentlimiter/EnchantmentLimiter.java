package com.hcempire.horus.enchantmentlimiter;

import com.hcempire.horus.Horus;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentLimiter {

    private static final EnchantmentLimiter instance = new EnchantmentLimiter();
    private static Horus main = Horus.getInstance();

    public int getEnchantmentLimit(Enchantment enchantment) {
        if (main.getConfigFile().getConfiguration().contains("ENCHANTMENT_LIMITER." + enchantment.getName())) {
            return main.getConfigFile().getInt("ENCHANTMENT_LIMITER." + enchantment.getName());
        }

        return enchantment.getMaxLevel();
    }

    public static EnchantmentLimiter getInstance() {
        return instance;
    }

}
