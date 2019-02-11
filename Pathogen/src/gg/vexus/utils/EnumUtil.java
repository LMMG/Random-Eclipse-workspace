package gg.vexus.utils;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class EnumUtil {

    public static <T> T getRandom(T[] array) {
        return array[new Random().nextInt(array.length)];
    }
}

