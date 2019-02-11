package me.kairos.ipunish;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.util.com.google.common.collect.Iterables;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    public static final String PLAYER_ONLY_COMMAND = ChatColor.RED + "You must be a player to do this!";

    private static final String PLAYER_NOT_FOUND_MESSAGE = ChatColor.RED.toString() + "Player '%1$s' not found";
    private static final String PROFILE_NOT_FOUND_MESSAGE = ChatColor.RED.toString() + "Profile for '%1$s' not found";

    public static String getPlayerNotFoundMessage(String search) {
        return String.format(Locale.ENGLISH, PLAYER_NOT_FOUND_MESSAGE, search);
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        String argument = args[(args.length - 1)];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(80).collect(Collectors.toList());
    }

    public static void kickFromNetwork(String name, String reason, CommandSender sender, JavaPlugin plugin) {
        Player target = Bukkit.getPlayer(name);
        if (target != null) {
            target.kickPlayer(reason);
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(name);
        out.writeUTF(reason);
        Player messenger = sender instanceof Player ? ((Player) sender) : Iterables.getLast(null);
        if (messenger != null) messenger.sendPluginMessage(plugin, IPunish.BUNGEE_CHANNEL_NAME, out.toByteArray());
    }

    public static String getProfileNotFoundMessage(String search) {
        return String.format(Locale.ENGLISH, PROFILE_NOT_FOUND_MESSAGE, search);
    }

    public static void sendStaffMessage(String message) {
        Set<Permissible> permissibles = Bukkit.getPluginManager().getPermissionSubscriptions("ipunish.staff");
        for (Permissible permissible : permissibles) {
            if (permissible instanceof CommandSender) {
                ((CommandSender) permissible).sendMessage(message);
            }
        }
    }

    public static long parseMillis(String string) {
        if (string.isEmpty()) {
            return 0;
        }

        int time = 0;
        if (string.contains("m")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 60;
            }
        } else if (string.contains("h")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 3600;
            }
        } else if (string.contains("s")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr);
            }
        } else if (string.contains("d")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 86400;
            }
        } else if (string.contains("y")) {
            String timeStr = strip(string);
            if (NumberUtils.isNumber(timeStr)) {
                time = NumberUtils.toInt(timeStr) * 31536000;
            }
        }

        return time;
    }

    public static String strip(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }

        return builder.toString();
    }
}
