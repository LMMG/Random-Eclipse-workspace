/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 */
package com.hcempire.horus.listerners.sotw;

import com.google.common.collect.ImmutableList;
import com.hcempire.horus.Horus;
import com.hcempire.horus.listerners.sotw.SotwHandler;
import com.hcempire.horus.util.BukkitUtils;
import com.hcempire.horus.util.JavaUtils;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class SotwCommand
implements CommandExecutor,
TabCompleter {
    private static final List<String> COMPLETIONS = ImmutableList.of("start", "end");
    private final Horus plugin;

    public SotwCommand(Horus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Core.staff.advanced")) {
            sender.sendMessage((Object)ChatColor.RED + "No permission.");
            return true;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length < 2) {
                    sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + " " + args[0].toLowerCase() + " <duration>");
                    return true;
                }
                long duration = JavaUtils.parse(args[1]);
                if (duration == -1) {
                    sender.sendMessage((Object)ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
                    return true;
                }
                if (duration < 1000) {
                    sender.sendMessage((Object)ChatColor.RED + "SOTW protection time must last for at least 20 ticks.");
                    return true;
                }
                SotwHandler.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
                if (sotwRunnable != null) {
                    sender.sendMessage((Object)ChatColor.RED + "SOTW protection is already enabled, use /" + label + " cancel to end it.");
                    return true;
                }
                this.plugin.getSotwTimer().start(duration);
                sender.sendMessage((Object)ChatColor.RED + "Started SOTW protection for " + DurationFormatUtils.formatDurationWords((long)duration, (boolean)true, (boolean)true) + ".");
                return true;
            }
            if (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("cancel")) {
                if (this.plugin.getSotwTimer().cancel()) {
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m---*------------------------*---"));
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&eThe &6&lSOTW &ehas ended. &6&lGOOD LUCK&e."));
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m---*------------------------*---"));
                    return true;
                }
                sender.sendMessage((Object)ChatColor.RED + "SOTW protection is not active.");
                return true;
            }
        }
        sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + " <start|end>");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();
    }
}

