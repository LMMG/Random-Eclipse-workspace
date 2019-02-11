package gg.vexus.commands.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import gg.vexus.commands.essentials.Essentials;

public class SkullCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("skull")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "skull")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /skull <player>");
                return true;
            }
            if (args.length == 1) {
                if (p.getItemInHand().getType() != Material.SKULL_ITEM && p.getItemInHand().getData().getData() != 3) {
                    p.sendMessage(ChatColor.RED + "You must be holding a type of skull to get a players skull.");
                    return true;
                }
                p.sendMessage(ChatColor.BLUE + "Skull owner changed to " + args[0] + ".");
                final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                final SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
                skullMeta.setDisplayName(ChatColor.GOLD + args[0] + "'s head.");
                skullMeta.setOwner(args[0]);
                skull.setItemMeta((ItemMeta)skullMeta);
                p.setItemInHand(skull);
            }
        }
        return false;
    }
}
