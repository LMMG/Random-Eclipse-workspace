/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.parapvp.base.command.module.chat;

import com.parapvp.base.command.BaseCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BroadcastRawCommand
extends BaseCommand {
    public BroadcastRawCommand() {
        super("broadcastraw", "Broadcasts a raw message to the server.");
        this.setAliases(new String[]{"bcraw", "raw", "rawcast"});
        this.setUsage("/(command) [-p *perm*] <text..>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int position;
        String arg;
        String requiredNode;
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (args.length > 2 && (arg = args[0]).startsWith("-p")) {
            position = 1;
            requiredNode = arg.substring(2, arg.length());
        } else {
            position = 0;
            requiredNode = null;
        }
        String message = StringUtils.join((Object[])args, (char)' ', (int)position, (int)args.length);
        if (message.length() < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Broadcasts must be at least 3 characters.");
            return true;
        }
        message = ChatColor.translateAlternateColorCodes((char)'&', (String)message);
        if (requiredNode != null) {
            Bukkit.broadcast((String)message, (String)requiredNode);
        } else {
            Bukkit.broadcastMessage((String)message);
        }
        return true;
    }
}

