package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class InvseeCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("invsee")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "invsee")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /invsee <player>");
                return true;
            }
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    p.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                p.openInventory((Inventory)target.getInventory());
                p.sendMessage(ChatColor.BLUE + "Viewing " + target.getName() + "'s inventory.");
                return true;
            }
        }
        return false;
    }
}
