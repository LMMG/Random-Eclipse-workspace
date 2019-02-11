/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.parapvp.base.command.module.chat;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.user.ServerParticipator;
import com.parapvp.base.user.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ToggleStaffChatCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public ToggleStaffChatCommand(BasePlugin plugin) {
        super("togglestaffchat", "Toggles staff chat visibility.");
        this.setAliases(new String[]{"tsc", "togglesc"});
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ServerParticipator participator = this.plugin.getUserManager().getParticipator(sender);
        if (participator == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not allowed to do this.");
            return true;
        }
        boolean newChatToggled = !participator.isStaffChatVisible();
        participator.setStaffChatVisible(newChatToggled);
        sender.sendMessage((Object)ChatColor.YELLOW + "You have toggled staff chat visibility " + (newChatToggled ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()) + (Object)ChatColor.YELLOW + '.');
        return true;
    }
}

