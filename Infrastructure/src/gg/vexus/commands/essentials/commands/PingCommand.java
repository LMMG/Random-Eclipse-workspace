package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class PingCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "ping")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                final int ping = ((CraftPlayer)p).getHandle().ping;
                p.sendMessage(ChatColor.YELLOW + "Your ping " + ChatColor.GOLD + "» " + ChatColor.GREEN + ping);
            }
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    p.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                final int ping2 = ((CraftPlayer)target).getHandle().ping;
                p.sendMessage(ChatColor.YELLOW + target.getName() + "'s ping " + ChatColor.GOLD + "» " + ChatColor.GREEN + ping2);
            }
        }
        return false;
    }
}
