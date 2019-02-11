package gg.vexus.utils;


import org.bukkit.command.*;
import org.bukkit.*;

import java.util.*;

public class Util {
    private static final Server SERVER;
    private static final ConsoleCommandSender CONSOLE;

    static {
        SERVER = Bukkit.getServer();
        CONSOLE = Util.SERVER.getConsoleSender();
    }

    public static String color(final String o) {
        return ChatColor.translateAlternateColorCodes('&', o);
    }

    public static String strip(final String c) {
        return ChatColor.stripColor(c);
    }

    public static void print(final Object... os) {
        for (final Object o : os) {
            final String s = o.toString();
            if (s != null) {
                final String msg = color(s);
                Util.CONSOLE.sendMessage(msg);
            }
        }
    }

    public static String json(final String o) {
        final String msg = color(o);
        final String json = "{\"text\": \"" + msg + "\"}";
        return json;
    }

    @SafeVarargs
    public static <O> List<O> newList(final O... os) {
        final List<O> list = new ArrayList<O>();
        for (final O o : os) {
            if (o != null) {
                list.add(o);
            }
        }
        return list;
    }
}
