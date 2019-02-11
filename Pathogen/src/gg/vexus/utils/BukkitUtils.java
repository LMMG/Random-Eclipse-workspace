package gg.vexus.utils;

import org.bukkit.ChatColor;

import com.google.common.base.Strings;

public class BukkitUtils {
	
    public static final String STRAIGHT_LINE_DEFAULT;
    private static final String STRAIGHT_LINE_TEMPLATE;

    
    static {
        STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
        STRAIGHT_LINE_DEFAULT = BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
    }
}
