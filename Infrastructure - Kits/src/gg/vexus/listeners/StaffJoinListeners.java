package gg.vexus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffJoinListeners implements Listener
{

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if (p.hasPermission("common.mod"))
		{
			Bukkit.broadcast(ChatColor.BLUE + p.getName() + " has joined.", "common.mod");
		}
	}
	
	@EventHandler
	public void onJoin2(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		if (p.hasPermission("common.mod"))
		{
			Bukkit.broadcast(ChatColor.BLUE + p.getName() + " has left.", "common.mod");
		}
	}
}
