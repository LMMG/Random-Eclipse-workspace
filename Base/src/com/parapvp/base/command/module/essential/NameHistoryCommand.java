/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.time.FastDateFormat
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.parapvp.base.command.module.essential;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.NameHistory;
import com.parapvp.base.user.ServerParticipator;
import com.parapvp.base.user.UserManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class NameHistoryCommand
extends BaseCommand {
    private static final FastDateFormat FORMAT = FastDateFormat.getInstance((String)"EEE, MMM d yy, hh:mmaaa", (Locale)Locale.ENGLISH);
    private final BasePlugin plugin;

    public NameHistoryCommand(BasePlugin plugin) {
        super("namehistory", "Checks name change histories of players.");
        this.setUsage("/(command) <player>");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        BaseUser targetUser = null;
        ArrayList<String> messages = new ArrayList<String>();
        block0 : for (ServerParticipator participator : this.plugin.getUserManager().getParticipators().values()) {
            if (!(participator instanceof BaseUser)) continue;
            BaseUser baseUser = (BaseUser)participator;
            List<NameHistory> nameHistories = baseUser.getNameHistories();
            for (NameHistory nameHistory : nameHistories) {
                if (!nameHistory.getName().equalsIgnoreCase(args[0])) continue;
                messages.add((Object)ChatColor.GRAY + nameHistory.getName() + " (" + FORMAT.format(nameHistory.getMillis()) + ')');
                targetUser = baseUser;
                continue block0;
            }
        }
        if (targetUser == null) {
            sender.sendMessage((Object)ChatColor.GOLD + "Player '" + (Object)ChatColor.WHITE + args[0] + (Object)ChatColor.GOLD + "' not found.");
            return true;
        }
        sender.sendMessage(messages.toArray(new String[messages.size()]));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

