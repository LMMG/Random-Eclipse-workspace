/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.SignChangeEvent
 */
package com.parapvp.base.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ColouredSignListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.hasPermission("base.sign.colour")) {
            String[] lines = event.getLines();
            for (int i = 0; i < lines.length; ++i) {
                if (!player.hasPermission("base.sign.admin") && (event.getLine(i).contains(ChatColor.translateAlternateColorCodes((char)'&', (String)"Sell")) || event.getLine(i).contains("Buy"))) {
                    player.sendMessage((Object)ChatColor.RED + "You have used a sign that you're not allowed to use.");
                    event.setCancelled(true);
                }
                event.setLine(i, ChatColor.translateAlternateColorCodes((char)'&', (String)lines[i]));
            }
        }
    }
}

