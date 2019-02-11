/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Ints
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.parapvp.base.command.module.essential;

import com.google.common.primitives.Ints;
import com.parapvp.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetMaxPlayersCommand
extends BaseCommand {
    public SetMaxPlayersCommand() {
        super("setmaxplayers", "Sets the max player cap.");
        this.setAliases(new String[]{"setplayercap"});
        this.setUsage("/(command) <amount>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Integer amount = Ints.tryParse((String)args[0]);
        if (amount == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[0] + "' is not a number.");
            return true;
        }
        Bukkit.setMaxPlayers((int)amount);
        Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Set the maximum players to " + amount + '.'));
        return true;
    }
}

