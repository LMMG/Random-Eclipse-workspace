/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.parapvp.base.command.module.inventory;

import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.util.BaseConstants;
import com.parapvp.util.BukkitUtils;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CopyInvCommand
extends BaseCommand {
    public CopyInvCommand() {
        super("copyinv", "Copies a players inv");
        this.setAliases(new String[]{"copyinventory"});
        this.setUsage("/(command) <playerName>");
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage((Object)ChatColor.RED + "Error, your not a player ;)");
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage((Object)ChatColor.RED + this.getUsage());
            return true;
        }
        Player player = (Player)cs;
        if (args.length == 1) {
            Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
            if (target == null || !CopyInvCommand.canSee((CommandSender)player, target)) {
                player.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                return true;
            }
            player.getInventory().setContents(target.getInventory().getContents());
            player.getInventory().setArmorContents(target.getInventory().getArmorContents());
            player.sendMessage((Object)ChatColor.YELLOW + "You have copied the inventory of " + target.getName());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

