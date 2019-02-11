package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import gg.vexus.commands.essentials.Essentials;

public class ClearCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clear")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player.");
                return true;
            }
            if (!Essentials.hasPermission(sender, "clear")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.BLUE + "Your inventory has been cleared!");
                p.getInventory().clear();
                p.getInventory().setArmorContents((ItemStack[])null);
                return true;
            }
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    p.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                target.sendMessage(ChatColor.BLUE + "Your inventory has been cleared!");
                target.getInventory().clear();
                target.getInventory().setArmorContents((ItemStack[])null);
                return true;
            }
        }
        return false;
    }
}
