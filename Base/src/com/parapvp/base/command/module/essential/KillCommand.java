/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package com.parapvp.base.command.module.essential;

import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.util.BaseConstants;
import com.parapvp.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class KillCommand
extends BaseCommand {
    public KillCommand() {
        super("kill", "Kills a player.");
        this.setAliases(new String[]{"slay"});
        this.setUsage("/(command) <playerName>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
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
        if (target.isDead()) {
            sender.sendMessage((Object)ChatColor.RED + target.getName() + " is already dead.");
            return true;
        }
        EntityDamageEvent event = new EntityDamageEvent((Entity)target, EntityDamageEvent.DamageCause.SUICIDE, 10000);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot kill " + target.getName() + '.');
            return true;
        }
        target.setLastDamageCause(event);
        target.setHealth(0.0);
        if (sender.equals((Object)target)) {
            sender.sendMessage((Object)ChatColor.LIGHT_PURPLE + "You have been killed.");
            return true;
        }
        Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Slain player " + target.getName() + '.'));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 && sender.hasPermission(command.getPermission() + ".others") ? null : Collections.emptyList();
    }
}

