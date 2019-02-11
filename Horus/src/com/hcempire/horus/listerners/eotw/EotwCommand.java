/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.alexandeh.ekko.factions.type.PlayerFaction
 *  com.alexandeh.ekko.factions.type.SystemFaction
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package com.hcempire.horus.listerners.eotw;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.hcempire.horus.event.Event;
import com.hcempire.horus.event.EventManager;
import com.hcempire.horus.event.koth.KothEvent;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EotwCommand
implements CommandExecutor,
Listener {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("eotw")) {
            if (strings.length == 0) {
                commandSender.sendMessage(ChatColor.RED + "Usage: /eotw start | stop");
                return true;
            }
            if (strings.length >= 2) {
                commandSender.sendMessage(ChatColor.RED + "Usage: /eotw start | stop");
                return true;
            }
            if (strings[0].equalsIgnoreCase("start")) {
                if (EventManager.getInstance().getByName("EOTW") != null) {
                    Event event = EventManager.getInstance().getByName("EOTW");
                    KothEvent koth = (KothEvent)event;
                    koth.start(1800000);
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Bukkit.dispatchCommand(player, "f unclaimall");
                    Bukkit.dispatchCommand(commandSender, "f deathban Spawn");
                    Bukkit.dispatchCommand(commandSender, "f deathban NetherSpawn");
            }

                Bukkit.broadcastMessage((ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588 " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "EOTW"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588 " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW has commenced."));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588\u2588 " + ChatColor.RED + "All SafeZones are now Deathban."));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588 " + ChatColor.RED + "All factions are now raidable."));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));

            } else if (strings[0].equalsIgnoreCase("stop")) {
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588 " + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "EOTW"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588 " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW has finished."));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588\u2588"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588"));
                Bukkit.broadcastMessage((ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                if (EventManager.getInstance().getByName("EOTW") != null) {
                    Event event = EventManager.getInstance().getByName("EOTW");
                    KothEvent koth = (KothEvent)event;
                    koth.stop(true);
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        KothEvent koth;
        Event event;
        if (EventManager.getInstance().getByName("EOTW") != null && (koth = (KothEvent)(event = EventManager.getInstance().getByName("EOTW"))).isActive() && (e.getMessage().contains("claim") || e.getMessage().contains("claimchunk"))) {
            e.getPlayer().sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "You can't claim in a EOTW.");
            e.setCancelled(true);
        }
    }
}

