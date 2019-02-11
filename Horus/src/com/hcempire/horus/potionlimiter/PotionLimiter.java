package com.hcempire.horus.potionlimiter;

import com.hcempire.horus.Horus;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.Potion;

public class PotionLimiter {

    private static final PotionLimiter instance = new PotionLimiter();
    private static Horus main = Horus.getInstance();

    public boolean isBlocked(int data) {
        return main.getConfigFile().getStringList("BLOCKED_POTIONS").contains(data + "");
    }

    public static PotionLimiter getInstance() {
        return instance;
    }

}
