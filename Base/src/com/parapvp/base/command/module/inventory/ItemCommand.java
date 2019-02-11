/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.parapvp.base.command.module.inventory;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.util.itemdb.ItemDb;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemCommand
extends BaseCommand {
    public ItemCommand() {
        super("item", "Spawns an item.");
        this.setAliases(new String[]{"i", "get"});
        this.setUsage("/(command) <itemName> [quantity]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        Player p = (Player)sender;
        if (args.length == 0) {
            p.sendMessage((Object)ChatColor.RED + this.getUsage());
            return true;
        }
        if (BasePlugin.getPlugin().getItemDb().getItem(args[0]) == null) {
            sender.sendMessage((Object)ChatColor.GOLD + "Item named or with ID '" + (Object)ChatColor.RESET + args[0] + (Object)ChatColor.GOLD + "' not found.");
            return true;
        }
        if (args.length == 1) {
            if (!p.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[0], BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize())}).isEmpty()) {
                p.sendMessage((Object)ChatColor.RED + "Your inventory is full.");
                return true;
            }
            for (Player on : Bukkit.getOnlinePlayers()) {
                if (!on.hasPermission("base.command.give")) continue;
                if (on != p) {
                    on.sendMessage((Object)ChatColor.GRAY + "[" + p.getName() + " has given " + (Object)ChatColor.YELLOW + "himself" + (Object)ChatColor.GRAY + BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize() + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[0])) + "]");
                    continue;
                }
                on.sendMessage((Object)ChatColor.GOLD + "You gave yourself " + BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize() + ", " + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[0])));
            }
        }
        if (args.length == 2) {
            if (!p.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[0], Integer.parseInt(args[1]))}).isEmpty()) {
                p.sendMessage((Object)ChatColor.RED + "Your inventory is full.");
                return true;
            }
            for (Player on : Bukkit.getOnlinePlayers()) {
                if (!on.hasPermission("base.command.give")) continue;
                if (on != p) {
                    on.sendMessage((Object)ChatColor.GRAY + "[" + p.getName() + " has given " + (Object)ChatColor.YELLOW + "himself" + (Object)ChatColor.GRAY + " " + args[1] + ", " + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[0])) + "]");
                    continue;
                }
                on.sendMessage((Object)ChatColor.GOLD + "You gave yourself " + args[1] + ", " + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[0])));
            }
        }
        return true;
    }
}

