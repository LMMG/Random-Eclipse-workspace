package rip.kohi.utils;

import org.bukkit.ChatColor;

public class C {

    public static String c(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String complete(String string) {
        String current = "";
        for (char c : string.toCharArray()) {
            String color = ChatColor.getLastColors(current);

            current = ChatColor.translateAlternateColorCodes('&', current.endsWith("&") || current.endsWith(ChatColor.COLOR_CHAR + "") ? current + c : current + color + c);
        }

        return current;
    }

}
