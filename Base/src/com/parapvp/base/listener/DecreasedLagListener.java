/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBurnEvent
 *  org.bukkit.event.block.BlockFormEvent
 *  org.bukkit.event.block.BlockFromToEvent
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.block.BlockSpreadEvent
 *  org.bukkit.event.block.LeavesDecayEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.ExplosionPrimeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package com.parapvp.base.listener;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.command.CommandManager;
import com.parapvp.base.manager.ServerHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DecreasedLagListener
implements Listener {
    private final BasePlugin plugin;

    public DecreasedLagListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BaseCommand baseCommand;
        Player player;
        if (this.plugin.getServerHandler().isDecreasedLagMode() && (player = event.getPlayer()).hasPermission((baseCommand = this.plugin.getCommandManager().getCommand("stoplag")).getPermission())) {
            event.getPlayer().sendMessage((Object)ChatColor.YELLOW + "Intense server activity is currently prevented. Use /" + baseCommand.getName() + " to toggle.");
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onBlockForm(BlockFormEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (this.plugin.getServerHandler().isDecreasedLagMode()) {
            switch (event.getSpawnReason()) {
                case SPAWNER: 
                case SPAWNER_EGG: 
                case BUILD_SNOWMAN: 
                case BUILD_IRONGOLEM: 
                case BUILD_WITHER: 
                case DISPENSE_EGG: {
                    break;
                }
                default: {
                    event.setCancelled(true);
                }
            }
        }
    }

}

