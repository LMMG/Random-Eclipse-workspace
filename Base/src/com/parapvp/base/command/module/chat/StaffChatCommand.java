/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.parapvp.base.command.module.chat;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.ServerParticipator;
import com.parapvp.base.user.UserManager;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public StaffChatCommand(BasePlugin plugin) {
        super("staffchat", "Enters staff chat mode.");
        this.setAliases(new String[]{"sc", "ac", "gsc"});
        this.setUsage("/(command) [playerName]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ServerParticipator target;
        ServerParticipator participator = this.plugin.getUserManager().getParticipator(sender);
        if (participator == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not allowed to do this.");
            return true;
        }
        if (args.length <= 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage((Object)ChatColor.RED + "Usage: /" + label + " <message|playerName>");
                return true;
            }
            target = participator;
        } else {
            Player targetPlayer = Bukkit.getPlayerExact((String)args[0]);
            if (targetPlayer == null || !BaseCommand.canSee(sender, targetPlayer) || !sender.hasPermission(command.getPermission() + ".others")) {
                String message = StringUtils.join((Object[])args, (char)' ');
                String format = (Object)ChatColor.LIGHT_PURPLE + "(Staff Chat) " + (Object)ChatColor.AQUA + String.format(Locale.ENGLISH, new StringBuilder().append("%1$s: ").append((Object)ChatColor.GRAY).append("%2$s").toString(), sender.getName(), message);
                Bukkit.getConsoleSender().sendMessage(format);
                for (Player other : Bukkit.getOnlinePlayers()) {
                    BaseUser otherUser = this.plugin.getUserManager().getUser(other.getUniqueId());
                    if (!otherUser.isStaffChatVisible() || !other.hasPermission("base.command.staffchat")) continue;
                    other.sendMessage(format);
                }
                return true;
            }
            target = this.plugin.getUserManager().getUser(targetPlayer.getUniqueId());
        }
        boolean newStaffChat = !target.isInStaffChat() || args.length >= 2 && Boolean.parseBoolean(args[1]);
        target.setInStaffChat(newStaffChat);
        sender.sendMessage((Object)ChatColor.YELLOW + "Staff chat mode of " + target.getName() + " set to " + newStaffChat + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

