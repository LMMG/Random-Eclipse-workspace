/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.alexandeh.ekko.factions.type.PlayerFaction
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.hcempire.horus.listerners.ffa;

import com.alexandeh.ekko.factions.type.PlayerFaction;
import java.util.Set;

import com.hcempire.horus.Horus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FreeForAllCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("ffa")) {
            if (strings.length == 0) {
                commandSender.sendMessage((Object)ChatColor.RED + "Usage: /ffa start");
                commandSender.sendMessage((Object)ChatColor.RED + "REMEMBER THIS WILL TP ALL THE PLAYERS TO YOU AND WILL DISBAND ALL THE FACTIONS.");
                return true;
            }
            if (strings.length >= 2) {
                commandSender.sendMessage((Object)ChatColor.RED + "Usage: /ffa start");
                commandSender.sendMessage((Object)ChatColor.RED + "REMEMBER THIS WILL TP ALL THE PLAYERS TO YOU AND WILL DISBAND ALL THE FACTIONS.");
                return true;
            }
            if (strings[0].equalsIgnoreCase("start")) {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Player sender = (Player)commandSender;
                    player.teleport((Entity)sender);
                    Bukkit.dispatchCommand(player, "f leave");
                    Bukkit.dispatchCommand(player, "f disband");
                }
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + (Object)ChatColor.RED + "\u2588 " + ChatColor.DARK_RED.toString() + (Object)ChatColor.BOLD + "FFA"));
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588 " + ChatColor.RED.toString() + (Object)ChatColor.BOLD + "FFA has commenced."));
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588" + (Object)ChatColor.RED + "\u2588\u2588 " + (Object)ChatColor.RED + "Good Luck!"));
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((String)((Object)ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
            }
        }
        return false;
    }
}

