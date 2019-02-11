package gg.vexus.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import gg.vexus.Core;
import net.md_5.bungee.api.ChatColor;

public class JoinManager implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void join(PlayerJoinEvent event)
	{
		if (!event.getPlayer().hasPermission("ipmatcher.authentication")) { return; }
		if (Core.getCore().getDb().getIp(event.getPlayer()) == null)
		{
			Core.getCore().getDb().setIp(event.getPlayer(), event.getPlayer().getAddress().getAddress().getHostAddress());
			event.getPlayer().sendMessage("§aLooks like it's your first time joining as staff, you have been added to the authentication system.");
			return;
		}
		if (Core.getCore().getDb().getResetQueue().contains(event.getPlayer().getUniqueId().toString()))
		{
			event.getPlayer().sendMessage("§aLooks like an admin reset your IP. Welcome, §e" + event.getPlayer().getName() + "§a.");
			Core.getCore().getDb().setIp(event.getPlayer(), event.getPlayer().getAddress().getAddress().getHostAddress());
			Core.getCore().getDb().delFromResetQueue(event.getPlayer());
			return;
		}
		if (!Core.getCore().getDb().getIp(event.getPlayer()).equals(event.getPlayer().getAddress().getAddress().getHostAddress()))
		{
			event.getPlayer().kickPlayer("§cAuthentication failed! Looks like your IP has changed, please contact an admin.");
			return;
		}
		event.getPlayer().sendMessage("§aAuthentication successful, welcome §e" + event.getPlayer().getName() + "§a.");
	}
}
