package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class WorldCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("world")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "world")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /world <world>");
                return true;
            }
            for (final org.bukkit.World w : Bukkit.getWorlds()) {
                if (args[0].equalsIgnoreCase(w.getName())) {
                    p.teleport(w.getSpawnLocation());
                    p.sendMessage(ChatColor.BLUE + "You have been teleported!");
                }
            }
        }
        return false;
    }
}