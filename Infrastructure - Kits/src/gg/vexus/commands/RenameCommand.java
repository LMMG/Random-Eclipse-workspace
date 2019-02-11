package gg.vexus.commands;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class RenameCommand implements Listener, CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        final Player p = (Player)sender;
        if (args.length != 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong number of args &7(&cUse '_' instead of space&7)"));
        }
        else if (!p.hasPermission("command.rename")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have &epermission &cto do this."));
        }
        else if (p.getItemInHand() == null || p.getItemInHand() == new ItemStack(Material.AIR)) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're not holding an item!"));
        }
        else {
            final ItemMeta meta = p.getItemInHand().getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0].replace("_", " ")));
            p.getItemInHand().setItemMeta(meta);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Item &asuccessfully &7renamed!"));
        }
        return false;
    }
}
