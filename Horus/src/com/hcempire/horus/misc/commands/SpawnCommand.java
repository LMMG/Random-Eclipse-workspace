package com.hcempire.horus.misc.commands;

import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.utils.LocationSerialization;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.Horus;
import com.hcempire.horus.util.HorusCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SpawnCommand extends HorusCommand {

    @Command(name = "spawn", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (!(sender.hasPermission("horus.spawn"))) {
            sender.sendMessage("Unknown command. Type \"/help\" for help.");
            return;
        }

        if (args.length == 0 && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "Usage: /spawn <player>");
            return;
        }

        Player toTeleport;
        if (args.length == 0 && sender instanceof Player) {
            toTeleport = command.getPlayer();
        } else {
            toTeleport = Bukkit.getPlayer(args[0]);
        }

        if (toTeleport == null) {
            sender.sendMessage(ChatColor.RED + "Invalid target.");
            return;
        }

        if (getSpawnLocation() == null) {
            sender.sendMessage(ChatColor.RED + "Spawn location not set.");
            return;
        }

        toTeleport.teleport(getSpawnLocation());
        toTeleport.sendMessage(ChatColor.GOLD + "You have successfully teleported to " + ChatColor.YELLOW + "Spawn" + ChatColor.GOLD + ".");

        if (!toTeleport.getName().equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(ChatColor.GOLD + "You have successfully teleported " + ChatColor.YELLOW + toTeleport.getName() + ChatColor.GOLD + " to " + ChatColor.YELLOW + "Spawn" + ChatColor.GOLD + ".");
        }
        return;
    }

    private Location getSpawnLocation() {
        SystemFaction faction = SystemFaction.getByName("Spawn");
        if (faction != null && faction.getHome() != null) {
           return LocationSerialization.deserializeLocation(faction.getHome());
        }
        return null;
    }
}
