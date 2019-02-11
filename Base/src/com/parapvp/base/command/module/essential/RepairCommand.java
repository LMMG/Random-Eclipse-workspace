/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.parapvp.base.command.module.essential;

import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.util.BaseConstants;
import com.parapvp.util.BukkitUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RepairCommand
extends BaseCommand {
    private static final short FULLY_REPAIRED_DURABILITY = 0;

    public RepairCommand() {
        super("repair", "Allows repairing of damaged tools for a player.");
        this.setUsage("/(command) <playerName> [all]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length > 0) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        HashSet<ItemStack> toRepair = new HashSet<ItemStack>();
        if (args.length >= 2 && args[1].equalsIgnoreCase("all")) {
            PlayerInventory targetInventory = target.getInventory();
            toRepair.addAll(Arrays.asList(targetInventory.getContents()));
            toRepair.addAll(Arrays.asList(targetInventory.getArmorContents()));
        } else {
            toRepair.add(target.getItemInHand());
        }
        for (ItemStack stack : toRepair) {
            if (stack == null || stack.getType() == Material.AIR) continue;
            stack.setDurability((short) 0);
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Repaired " + (toRepair.size() > 1 ? "inventory" : "held item") + " of " + target.getName() + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

