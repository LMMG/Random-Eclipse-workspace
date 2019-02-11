/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.parapvp.base.command.module.essential;

import com.parapvp.base.command.BaseCommand;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand
extends BaseCommand {
    public RenameCommand() {
        super("rename", "Rename your held item.");
        this.setUsage("/(command) <newItemName>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage((Object)ChatColor.RED + "You are not holding anything.");
            return true;
        }
        ItemMeta meta = stack.getItemMeta();
        String oldName = meta.getDisplayName();
        if (oldName != null) {
            oldName = oldName.trim();
        }
        String newName = args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("null") ? null : ChatColor.translateAlternateColorCodes((char)'&', (String)StringUtils.join((Object[])args, (char)' ', (int)0, (int)args.length));
        if (oldName == null && newName == null) {
            sender.sendMessage((Object)ChatColor.RED + "Your held item already has no name.");
            return true;
        }
        if (oldName != null && oldName.equals(newName)) {
            sender.sendMessage((Object)ChatColor.RED + "Your held item is already named this.");
            return true;
        }
        meta.setDisplayName(newName);
        stack.setItemMeta(meta);
        if (newName == null) {
            sender.sendMessage((Object)ChatColor.YELLOW + "Removed name of held item from " + oldName + '.');
            return true;
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Renamed held item from " + (oldName == null ? "no name" : oldName) + ChatColor.YELLOW + " to " + newName + (Object)ChatColor.YELLOW + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

