package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.util.chat.Lang;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;
import net.minecraft.util.com.google.common.primitives.Doubles;

public class TeleportCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player.");
                return true;
            }
            if (!Essentials.hasPermission(sender, "teleport")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /teleport <player|coordinates>");
                return true;
            }
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    p.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                final Location loc = new Location(Bukkit.getWorld(target.getWorld().getName()), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());
                p.teleport(loc);
                p.sendMessage(ChatColor.BLUE + "You have been teleported to " + target.getName() + ".");
                return true;
            }
            else {
                if (args.length == 2) {
                    p.sendMessage(ChatColor.RED + "Usage: /teleport <x> <y> <z>");
                    return true;
                }
                if (args.length == 3) {
                    if (Doubles.tryParse(args[0]) == null || Doubles.tryParse(args[1]) == null || Doubles.tryParse(args[2]) == null) {
                        p.sendMessage(ChatColor.RED + "Invalid coordinates!");
                        return true;
                    }
                    final double x = Doubles.tryParse(args[0]);
                    final double y = Doubles.tryParse(args[1]);
                    final double z = Doubles.tryParse(args[2]);
                    p.teleport(new Location(p.getWorld(), x, y, z));
                    p.sendMessage(ChatColor.BLUE + "You have been teleported! " + ChatColor.GRAY + "(" + x + ", " + y + ", " + z + ")");
                    return true;
                }
            }
        }
        return false;
    }
}
