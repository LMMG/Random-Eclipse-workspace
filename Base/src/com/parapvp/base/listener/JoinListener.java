/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package com.parapvp.base.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.user.BaseUser;

public class JoinListener implements Listener
{
	private static final int MAX_ACCOUNTS_PER_IP = 3;
	private final BasePlugin plugin;

	public JoinListener(BasePlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
		Integer value = baseUser.getAddressHistories().size();
		baseUser.tryLoggingName(player);
		baseUser.tryLoggingAddress(player.getAddress().getAddress().getHostAddress());
		if (player.getAddress().getAddress().getHostAddress().startsWith("216.") || player.getAddress().getAddress().getHostAddress().startsWith("172.") || value >= 3 && !baseUser.getAddressHistories().contains(player.getAddress().getAddress().getHostAddress()))
		{
			for (Player player1 : Bukkit.getOnlinePlayers())
			{
				if (!player1.hasPermission("base.vpn.view") || player.hasPermission("base.vpn.bypass"))
					continue;
				player1.sendMessage((Object) ChatColor.GRAY + "[" + (Object) ChatColor.DARK_AQUA + "!" + (Object) ChatColor.GRAY + "]" + (Object) ChatColor.RED + " " + player.getName() + (Object) ChatColor.GRAY + " might be using a proxy " + (Object) ChatColor.RED + "(" + value + " unique IPs)");
			}
		}
	}
}
