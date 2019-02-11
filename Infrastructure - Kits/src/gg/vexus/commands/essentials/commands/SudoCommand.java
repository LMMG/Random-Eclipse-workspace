package gg.vexus.commands.essentials.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class SudoCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("sudo")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "sudo")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0 || args.length == 1) {
                p.sendMessage(ChatColor.RED + "Usage: /sudo <player|all> <command>");
                return true;
            }
            if (args[0].equalsIgnoreCase("all")) {
                Player[] onlinePlayers;
                for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                    final Player all = onlinePlayers[i];
                    Bukkit.dispatchCommand((CommandSender)all, StringUtils.join((Object[])args, " ", 1, args.length));
                }
                p.sendMessage(ChatColor.GREEN + "Forced everyone to perform command '" + ChatColor.GRAY + "/" + StringUtils.join((Object[])args, " ", 1, args.length) + ChatColor.GREEN + "'.");
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                p.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            Bukkit.dispatchCommand((CommandSender)target, StringUtils.join((Object[])args, " ", 1, args.length));
            p.sendMessage(ChatColor.GREEN + "Forced " + target.getName() + " to perform command '" + ChatColor.GRAY + "/" + StringUtils.join((Object[])args, " ", 1, args.length) + ChatColor.GREEN + "'.");
        }
        return false;
    }
}