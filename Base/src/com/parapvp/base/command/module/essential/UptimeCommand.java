/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.apache.commons.lang3.time.FastDateFormat
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.parapvp.base.command.module.essential;

import com.parapvp.base.command.BaseCommand;
import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class UptimeCommand
extends BaseCommand {
    private static final FastDateFormat TIME_FORMATTER = FastDateFormat.getInstance((String)"dd/MM HH:mm:ss", (TimeZone)TimeZone.getTimeZone("GMT+1"), (Locale)Locale.ENGLISH);

    public UptimeCommand() {
        super("uptime", "Check the uptime of the server.");
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        String upTime = DurationFormatUtils.formatDurationWords((long)(System.currentTimeMillis() - startTime), (boolean)true, (boolean)true);
        sender.sendMessage((Object)ChatColor.BLUE + "Server up-time: " + (Object)ChatColor.GOLD + upTime + (Object)ChatColor.BLUE + ", started at " + TIME_FORMATTER.format(startTime) + ".");
        return true;
    }
}

