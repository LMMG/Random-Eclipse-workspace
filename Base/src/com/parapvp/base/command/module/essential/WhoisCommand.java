/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.ImmutableMap
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.NetworkManager
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.apache.commons.lang3.text.WordUtils
 *  org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package com.parapvp.base.command.module.essential;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.UserManager;
import com.parapvp.base.util.BaseConstants;
import com.parapvp.util.BukkitUtils;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class WhoisCommand
extends BaseCommand {
	  private static final Map<Integer, String> CLIENT_PROTOCOL_IDS = ImmutableMap.of(Integer.valueOf(4), "1.7.2 -> 1.7.5", Integer.valueOf(5), "1.7.6 -> 1.7.10", Integer.valueOf(47), "1.8 -> 1.8.8");
    private final BasePlugin plugin;

    public WhoisCommand(BasePlugin plugin) {
        super("whois", "Check information about a player.");
        this.plugin = plugin;
        this.setUsage("/(command) [playerName]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage());
            return true;
        }
        Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        Location location = target.getLocation();
        World world = location.getWorld();
        BaseUser baseUser = this.plugin.getUserManager().getUser(target.getUniqueId());
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage((Object)ChatColor.GREEN + " [" + target.getDisplayName() + (Object)ChatColor.GREEN + ']');
        sender.sendMessage((Object)ChatColor.YELLOW + "  Health: " + (Object)ChatColor.GOLD + ((CraftPlayer)target).getHealth() + '/' + ((CraftPlayer)target).getMaxHealth());
        sender.sendMessage((Object)ChatColor.YELLOW + "  Hunger: " + (Object)ChatColor.GOLD + target.getFoodLevel() + '/' + 20 + " (" + target.getSaturation() + " saturation)");
        sender.sendMessage((Object)ChatColor.YELLOW + "  Exp/Level: " + (Object)ChatColor.GOLD + target.getExp() + '/' + target.getLevel());
        sender.sendMessage((Object)ChatColor.YELLOW + "  Location: " + (Object)ChatColor.GOLD + world.getName() + ' ' + (Object)ChatColor.GRAY + '[' + WordUtils.capitalizeFully((String)world.getEnvironment().name().replace('_', ' ')) + "] " + (Object)ChatColor.GOLD + '(' + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ')');
        sender.sendMessage((Object)ChatColor.YELLOW + "  Vanished: " + (Object)ChatColor.GOLD + baseUser.isVanished() + " (priority="  + ')');
        sender.sendMessage((Object)ChatColor.YELLOW + "  Operator: " + (Object)ChatColor.GOLD + target.isOp());
        sender.sendMessage((Object)ChatColor.YELLOW + "  Game Mode: " + (Object)ChatColor.GOLD + WordUtils.capitalizeFully((String)target.getGameMode().name().replace('_', ' ')));
        sender.sendMessage((Object)ChatColor.YELLOW + "  Idle Time: " + (Object)ChatColor.GOLD + DurationFormatUtils.formatDurationWords((long)BukkitUtils.getIdleTime(target), (boolean)true, (boolean)true));
        int version = ((CraftPlayer)target).getHandle().playerConnection.networkManager.getVersion();
        sender.sendMessage((Object)ChatColor.YELLOW + "  Client Version: " + (Object)ChatColor.GOLD + version + (Object)ChatColor.GRAY + " [" + (String)MoreObjects.firstNonNull((Object)CLIENT_PROTOCOL_IDS.get(version), (Object)"Unknown (check at http://wiki.vg/Protocol_version_numbers)") + "]");
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

