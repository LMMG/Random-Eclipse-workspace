package gg.vexus.commands.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class TopCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("top")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "top")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            p.teleport(new Location(p.getLocation().getWorld(), (double)p.getLocation().getBlockX(), (double)p.getLocation().getWorld().getHighestBlockYAt(p.getLocation().getBlockX(), p.getLocation().getBlockZ()), (double)p.getLocation().getBlockZ(), p.getLocation().getYaw(), p.getLocation().getPitch()));
            p.sendMessage(ChatColor.BLUE + "You have been teleported!");
        }
        return false;
    }
}

