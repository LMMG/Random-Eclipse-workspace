/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 */
package com.parapvp.base.listener;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlockListener
implements Listener {
    @EventHandler
    public void onPreProcess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("/pex") && !e.getPlayer().hasPermission("*")) {
            e.getPlayer().sendMessage((Object)ChatColor.YELLOW + "You do not have enough permissions for this " + e.getPlayer().getName());
            e.setCancelled(true);
        }
    }
}

