/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.text.WordUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.parapvp.base.command.module.teleport;

import com.parapvp.base.command.BaseCommand;
import com.parapvp.util.BukkitUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldCommand
extends BaseCommand {
    public WorldCommand() {
        super("world", "Change current world.");
        this.setAliases(new String[]{"changeworld", "switchworld"});
        this.setUsage("/(command) <worldName>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + this.getUsage());
            return true;
        }
        World world = Bukkit.getWorld((String)args[0]);
        if (world == null) {
            sender.sendMessage((Object)ChatColor.RED + "World '" + args[0] + "' not found.");
            return true;
        }
        Player player = (Player)sender;
        if (player.getWorld().equals((Object)world)) {
            sender.sendMessage((Object)ChatColor.RED + "You are already in that world.");
            return true;
        }
        Location origin = player.getLocation();
        Location location = new Location(world, origin.getX(), origin.getY(), origin.getZ(), origin.getYaw(), origin.getPitch());
        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
        sender.sendMessage((Object)ChatColor.AQUA + "Switched world to '" + world.getName() + (Object)ChatColor.YELLOW + " [" + WordUtils.capitalizeFully((String)world.getEnvironment().name().replace('_', ' ')) + ']' + (Object)ChatColor.AQUA + "'.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        List worlds = Bukkit.getWorlds();
        ArrayList<String> results = new ArrayList<String>(worlds.size());
        for (World world : Bukkit.getWorlds()) {
            results.add(world.getName());
        }
        return BukkitUtils.getCompletions(args, results);
    }
}

