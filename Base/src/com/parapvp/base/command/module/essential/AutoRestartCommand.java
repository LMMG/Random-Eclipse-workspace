/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.parapvp.base.command.module.essential;

import com.google.common.base.Strings;
import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.task.AutoRestartHandler;
import com.parapvp.util.JavaUtils;
import com.parapvp.util.command.CommandArgument;
import com.parapvp.util.command.CommandWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AutoRestartCommand
extends BaseCommand {
    private final CommandWrapper handler;

    public AutoRestartCommand(BasePlugin plugin) {
        super("autore", "Allows management of server restarts.");
        this.setAliases(new String[]{"autorestart"});
        this.setUsage("/(command) <cancel|time|schedule>");
        ArrayList<CommandArgument> arguments = new ArrayList<CommandArgument>(3);
        arguments.add(new AutoRestartCancelArgument(plugin));
        arguments.add(new AutoRestartScheduleArgument(plugin));
        arguments.add(new AutoRestartTimeArgument(plugin));
        Collections.sort(arguments, new CommandWrapper.ArgumentComparator());
        this.handler = new CommandWrapper(arguments);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return this.handler.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return this.handler.onTabComplete(sender, command, label, args);
    }

    private static class AutoRestartTimeArgument
    extends CommandArgument {
        private final BasePlugin plugin;

        public AutoRestartTimeArgument(BasePlugin plugin) {
            super("time", "Gets the remaining time until next restart.");
            this.plugin = plugin;
            this.aliases = new String[]{"remaining", "time"};
        }

        @Override
        public String getUsage(String label) {
            return "" + '/' + label + ' ' + this.getName();
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            String reason;
            if (!this.plugin.getAutoRestartHandler().isPendingRestart()) {
                sender.sendMessage((Object)ChatColor.RED + "There is not a restart task pending.");
                return true;
            }
            sender.sendMessage((Object)ChatColor.YELLOW + "Automatic restart task occurring in " + DurationFormatUtils.formatDurationWords((long)this.plugin.getAutoRestartHandler().getRemainingMilliseconds(), (boolean)true, (boolean)true) + (Strings.nullToEmpty((String)(reason = this.plugin.getAutoRestartHandler().getReason())).isEmpty() ? "" : new StringBuilder().append(" for ").append(reason).toString()) + '.');
            return true;
        }
    }

    private static class AutoRestartScheduleArgument
    extends CommandArgument {
        private final BasePlugin plugin;

        public AutoRestartScheduleArgument(BasePlugin plugin) {
            super("schedule", "Schedule an automatic restart.");
            this.plugin = plugin;
            this.aliases = new String[]{"reschedule"};
        }

        @Override
        public String getUsage(String label) {
            return "" + '/' + label + ' ' + this.getName() + " <time> [reason]";
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length < 2) {
                sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + ' ' + args[0].toLowerCase() + " <time> [reason]");
                return true;
            }
            long millis = JavaUtils.parse(args[1]);
            if (millis == -1) {
                sender.sendMessage((Object)ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
                return true;
            }
            String reason = StringUtils.join((Object[])args, (char)' ', (int)2, (int)args.length);
            this.plugin.getAutoRestartHandler().scheduleRestart(millis, reason);
            Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Scheduled a restart to occur in " + DurationFormatUtils.formatDurationWords((long)millis, (boolean)true, (boolean)true) + (reason.isEmpty() ? "" : new StringBuilder().append(" for ").append(reason).toString()) + '.'));
            return true;
        }
    }

    private static class AutoRestartCancelArgument
    extends CommandArgument {
        private final BasePlugin plugin;

        public AutoRestartCancelArgument(BasePlugin plugin) {
            super("cancel", "Cancels the current automatic restart.");
            this.plugin = plugin;
        }

        @Override
        public String getUsage(String label) {
            return "" + '/' + label + ' ' + this.getName();
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!this.plugin.getAutoRestartHandler().isPendingRestart()) {
                sender.sendMessage((Object)ChatColor.RED + "There is not a restart task pending.");
                return true;
            }
            this.plugin.getAutoRestartHandler().cancelRestart();
            sender.sendMessage((Object)ChatColor.YELLOW + "Automatic restart task cancelled.");
            return true;
        }
    }

}

