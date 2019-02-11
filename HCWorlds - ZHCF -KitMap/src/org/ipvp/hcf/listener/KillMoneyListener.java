package org.ipvp.hcf.listener;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.ipvp.hcf.HCF;
import org.bukkit.entity.*;

public class KillMoneyListener implements Listener
{
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            final Entity killer = (Entity)e.getEntity().getKiller();
            final Entity dead = (Entity)e.getEntity().getPlayer();
                HCF.getPlugin().getEconomyManager().addBalance(killer.getUniqueId(), 150);
                e.getEntity().getKiller().sendMessage(ChatColor.YELLOW.toString() + "You have recieved " + ChatColor.WHITE + "$150" + ChatColor.YELLOW + " for killing " + ChatColor.WHITE + e.getEntity().getPlayer().getName() + ChatColor.YELLOW + ".");
            }
        }
    }
