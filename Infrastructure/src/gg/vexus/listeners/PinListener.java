package gg.vexus.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import gg.vexus.commands.pin.LoginCommand;

public class PinListener implements Listener
{

	@EventHandler
	public void onChatBeforeLoggedIn(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName()))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onCommandBeforeLoggedIn(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		if ((!e.getMessage().startsWith("/pin")) && (LoginCommand.loggingin.contains(p.getName())))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}
	
	@EventHandler
	public void onMoveBeforeLoggedIn(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName()))
		{
			e.setTo(e.getFrom());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}
}
