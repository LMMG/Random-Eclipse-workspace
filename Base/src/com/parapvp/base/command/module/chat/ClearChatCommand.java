/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.parapvp.base.command.module.chat;

import com.parapvp.base.command.BaseCommand;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand
extends BaseCommand {
    private static final int CHAT_HEIGHT = 101;
    private static final String[] CLEAR_MESSAGE = new String[101];

    public ClearChatCommand() {
        super("clearchat", "Clears the server chat for players.");
        this.setAliases(new String[]{"cc"});
        this.setUsage("/(command) <reason>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.RED + this.getUsage());
            return true;
        }
        String reason = StringUtils.join((Object[])args, (char)' ');
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(CLEAR_MESSAGE);
            if (!player.hasPermission("base.command.clearchat")) continue;
            player.sendMessage((Object)ChatColor.DARK_AQUA + sender.getName() + (Object)ChatColor.YELLOW + " has cleared chat for: " + reason);
        }
        Bukkit.getConsoleSender().sendMessage((Object)ChatColor.YELLOW + sender.getName() + " cleared in-game chat.");
        return true;
    }
}

