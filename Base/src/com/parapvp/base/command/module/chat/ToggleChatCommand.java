/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.parapvp.base.command.module.chat;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.UserManager;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleChatCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public ToggleChatCommand(BasePlugin plugin) {
        super("togglechat", "Toggles global chat visibility.");
        this.setAliases(new String[]{"tgc", "toggleglobalchat"});
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        Player player = (Player)sender;
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        boolean newChatToggled = !baseUser.isGlobalChatVisible();
        baseUser.setGlobalChatVisible(newChatToggled);
        sender.sendMessage((Object)ChatColor.YELLOW + "You have toggled global chat visibility " + (newChatToggled ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()) + (Object)ChatColor.YELLOW + '.');
        return true;
    }
}

