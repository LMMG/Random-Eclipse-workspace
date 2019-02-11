/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 */
package com.parapvp.base.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerLimitListener implements Listener
{
	private static final String BYPASS_FULL_JOIN = "base.serverfull.bypass";

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL && event.getPlayer().hasPermission("base.serverfull.bypass"))
		{
			event.allow();
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		if (player.hasPermission("core"))
		{
		}
	}
}
