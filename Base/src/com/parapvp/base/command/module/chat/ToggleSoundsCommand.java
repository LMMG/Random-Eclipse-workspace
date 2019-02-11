/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.parapvp.base.command.module.chat;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.event.PlayerMessageEvent;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.UserManager;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ToggleSoundsCommand
extends BaseCommand
implements Listener {
    private final BasePlugin plugin;

    public ToggleSoundsCommand(BasePlugin plugin) {
        super("sounds", "Toggles messaging sounds.");
        this.setAliases(new String[]{"pmsounds", "togglepmsounds", "messagingsounds"});
        this.setUsage("/(command) [playerName]");
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        BaseUser baseUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        boolean newMessagingSounds = !baseUser.isMessagingSounds() || args.length >= 2 && Boolean.parseBoolean(args[1]);
        baseUser.setMessagingSounds(newMessagingSounds);
        sender.sendMessage((Object)ChatColor.YELLOW + "You have toggled message sounds " + (newMessagingSounds ? new StringBuilder().append((Object)ChatColor.GREEN).append("on").toString() : new StringBuilder().append((Object)ChatColor.RED).append("off").toString()) + (Object)ChatColor.YELLOW + '.');
        return true;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerMessage(PlayerMessageEvent event) {
        Player recipient = event.getRecipient();
        BaseUser recipientUser = this.plugin.getUserManager().getUser(recipient.getUniqueId());
        if (recipientUser.isMessagingSounds()) {
            recipient.playSound(recipient.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        }
    }
}

