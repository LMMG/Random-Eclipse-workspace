/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Enums
 *  com.google.common.base.Optional
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.SkullType
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package com.parapvp.base.command.module.inventory;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.util.BukkitUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCommand
extends BaseCommand {
    private static final ImmutableList<String> SKULL_NAMES;

    public SkullCommand() {
        super("skull", "Spawns a player head skull item.");
        this.setAliases(new String[]{"head", "playerhead"});
        this.setUsage("/(command) <playerName>");
    }

    @Override
    public boolean isPlayerOnlyCommand() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Optional skullType;
        ItemStack stack;
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        Optional optional = skullType = args.length > 0 ? Enums.getIfPresent((Class)SkullType.class, (String)args[0]) : Optional.absent();
        if (skullType.isPresent()) {
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short)((SkullType)skullType.get()).getData());
        } else {
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.getData());
            String ownerName = args.length > 0 ? args[0] : sender.getName();
            SkullMeta meta = (SkullMeta)stack.getItemMeta();
            meta.setOwner(ownerName);
            stack.setItemMeta((ItemMeta)meta);
        }
        ((Player)sender).getInventory().addItem(new ItemStack[]{stack});
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        ArrayList<String> completions = new ArrayList<String>((Collection<String>)SKULL_NAMES);
        Player senderPlayer = sender instanceof Player ? (Player)sender : null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (senderPlayer != null && !senderPlayer.canSee(player)) continue;
            completions.add(player.getName());
        }
        return BukkitUtils.getCompletions(args, completions);
    }

    static {
        ImmutableList.Builder builder = new ImmutableList.Builder();
        for (SkullType skullType : SkullType.values()) {
            builder.add((Object)skullType.name());
        }
        SKULL_NAMES = builder.build();
    }
}

