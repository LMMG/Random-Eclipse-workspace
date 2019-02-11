package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import gg.vexus.commands.essentials.Essentials;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class BroadcastCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("broadcast")) {
            if (!Essentials.hasPermission(sender, "broadcast")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /broadcast <message>");
                return true;
            }
            final String message = StringUtils.join((Object[])args, " ");
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED.toString() + ChatColor.BOLD + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + ChatColor.translateAlternateColorCodes('&', message));
        }
        return false;
    }
}
