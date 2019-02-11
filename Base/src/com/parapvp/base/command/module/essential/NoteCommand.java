/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.time.DateFormatUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.parapvp.base.command.module.essential;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.UserManager;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoteCommand
extends BaseCommand {
    public NoteCommand() {
        super("note", "add, removes, and checks notes for a base user");
        this.setUsage("/(command) <add|remove|check> <playerName> [note]");
        this.setAliases(new String[]{"addnote, notes, checknote, removenote"});
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage((Object)ChatColor.RED + "Please use the server to execute this command.");
            return true;
        }
        Player player = (Player)cs;
        if (args.length >= 2) {
            if (Bukkit.getPlayer((String)args[1]) == null && Bukkit.getOfflinePlayer((String)args[1]) == null) {
                player.sendMessage((Object)ChatColor.RED + "User not found, please try a different name.");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer((String)args[1]);
            BaseUser targetUser = BasePlugin.getPlugin().getUserManager().getUser(target.getUniqueId());
            String note = StringUtils.join((Object[])args, (char)' ', (int)2, (int)args.length);
            if (args[0].equalsIgnoreCase("add")) {
                String upTime = DateFormatUtils.format((long)System.currentTimeMillis(), (String)"MM/dd/yy");
                String time = DateFormatUtils.format((long)System.currentTimeMillis(), (String)"hh:mm");
                targetUser.setNote((Object)ChatColor.LIGHT_PURPLE + cs.getName() + (Object)ChatColor.GRAY + " [" + upTime + " | " + time + "]" + (Object)ChatColor.YELLOW + " - " + note);
                player.sendMessage((Object)ChatColor.YELLOW + "You added a note to " + targetUser.getName());
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (player.hasPermission(this.getPermission() + ".remove")) {
                    if (targetUser.tryRemoveNote()) {
                        Command.broadcastCommandMessage((CommandSender)cs, (String)((Object)ChatColor.YELLOW + "Removed note of " + target.getName() + (Object)ChatColor.YELLOW + '.'));
                        return true;
                    }
                    player.sendMessage((Object)ChatColor.RED + "Note not found or other error.");
                    return true;
                }
                player.sendMessage((Object)ChatColor.RED + "No permission to this argument.");
                return true;
            }
            if (args[0].equalsIgnoreCase("check")) {
                for (String notes : targetUser.getNotes()) {
                    player.sendMessage(notes);
                }
                return true;
            }
            player.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(s));
            return true;
        }
        player.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(s));
        return true;
    }
}

