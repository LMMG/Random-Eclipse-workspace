/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.defaults.VanillaCommand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.parapvp.base.command.module.teleport;

import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.util.BaseConstants;
import com.parapvp.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportCommand
extends BaseCommand {
    static final int MAX_COORD = 30000000;
    static final int MIN_COORD_MINUS_ONE = -30000001;
    static final int MIN_COORD = -30000000;

    public TeleportCommand() {
        super("teleport", "Teleport to a player or position.");
        this.setUsage("/(command) (<playerName> [otherPlayerName]) | (x y z)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player targetA;
        if (args.length < 1 || args.length > 4) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (args.length == 1 || args.length == 3) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Usage: " + this.getUsage());
                return true;
            }
            targetA = (Player)sender;
        } else {
            targetA = BukkitUtils.playerWithNameOrUUID(args[0]);
        }
        if (targetA == null || !BaseCommand.canSee(sender, targetA)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        if (args.length < 3) {
            Player targetB = BukkitUtils.playerWithNameOrUUID(args[args.length - 1]);
            if (targetB == null || !BaseCommand.canSee(sender, targetB)) {
                sender.sendMessage((Object)ChatColor.GOLD + "Player named or with UUID '" + (Object)ChatColor.WHITE + args[args.length - 1] + (Object)ChatColor.GOLD + "' not found.");
                return true;
            }
            if (targetA.equals((Object)targetB)) {
                sender.sendMessage((Object)ChatColor.RED + "The teleportee and teleported are the same player.");
                return true;
            }
            if (targetA.teleport((Entity)targetB, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                sender.sendMessage((Object)ChatColor.YELLOW + "Teleported " + targetA.getName() + " to " + targetB.getName() + '.');
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Failed to teleport you to " + targetB.getName() + '.');
            }
        } else if (targetA.getWorld() != null) {
            Location targetALocation = targetA.getLocation();
            double x = this.getCoordinate(sender, targetALocation.getX(), args[args.length - 3]);
            double y = this.getCoordinate(sender, targetALocation.getY(), args[args.length - 2], 0, 0);
            double z = this.getCoordinate(sender, targetALocation.getZ(), args[args.length - 1]);
            if (x == -3.0000001E7 || y == -3.0000001E7 || z == -3.0000001E7) {
                sender.sendMessage("Please provide a valid location.");
                return true;
            }
            targetALocation.setX(x);
            targetALocation.setY(y);
            targetALocation.setZ(z);
            if (targetA.teleport(targetALocation, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                sender.sendMessage(String.format((Object)ChatColor.YELLOW + "Teleported %s to %.2f, %.2f, %.2f.", targetA.getName(), x, y, z));
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Failed to teleport you.");
            }
        }
        return true;
    }

    private double getCoordinate(CommandSender sender, double current, String input) {
        return this.getCoordinate(sender, current, input, -30000000, 30000000);
    }

    private double getCoordinate(CommandSender sender, double current, String input, int min, int max) {
        double result;
        boolean relative = input.startsWith("~");
        double d = result = relative ? current : 0.0;
        if (!relative || input.length() > 1) {
            double testResult;
            boolean exact = input.contains(".");
            if (relative) {
                input = input.substring(1);
            }
            if ((testResult = VanillaCommand.getDouble((CommandSender)sender, (String)input)) == -3.0000001E7) {
                return -3.0000001E7;
            }
            result += testResult;
            if (!exact && !relative) {
                result += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (result < (double)min) {
                result = -3.0000001E7;
            }
            if (result > (double)max) {
                result = -3.0000001E7;
            }
        }
        return result;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 || args.length == 2 ? null : Collections.emptyList();
    }
}

