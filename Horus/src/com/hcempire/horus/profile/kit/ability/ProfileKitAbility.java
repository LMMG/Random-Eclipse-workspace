package com.hcempire.horus.profile.kit.ability;

import lombok.Getter;
import org.bukkit.potion.PotionEffectType;

public enum ProfileKitAbility {
    MINING_INVISIBILITY(PotionEffectType.INVISIBILITY),
    SINGLE_SUGAR_SPEED(PotionEffectType.SPEED),
    MULTI_SUGAR_SPEED(PotionEffectType.SPEED),
    MULTI_FEATHER_JUMP(PotionEffectType.JUMP),
    MULTI_IRON_RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
    MULTI_BLAZE_STRENGTH(PotionEffectType.INCREASE_DAMAGE),
    MULTI_SPIDER_WITHER(PotionEffectType.WITHER),
    MULTI_TEAR_REGEN(PotionEffectType.REGENERATION),
    MULTI_CREAM_FIRERES(PotionEffectType.FIRE_RESISTANCE);

    @Getter private PotionEffectType potion;

    ProfileKitAbility(PotionEffectType potion) {
        this.potion = potion;
    }
}
