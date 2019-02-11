package gg.mist.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SecurityCommandListener implements Listener
{

	@EventHandler
	public void PreventDisable(AsyncPlayerChatEvent e1)
	{

		if (e1.getMessage().contains("/plugman disable Core"))
		{

			Player player = e1.getPlayer();

			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + player + " 7d [Security Ban] Attempted to disable security plugin / core via plugman");
		}
	}

	@EventHandler
	public void PreventBanOwners(AsyncPlayerChatEvent e2)
	{
		if (e2.getMessage().contains("/ban Possibilities"))
		{
			Player player = e2.getPlayer();
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + player + " 7d [Security Ban] Attempted to ban an owner.");
		}
	}

	@EventHandler
	public void PreventBanOwner(AsyncPlayerChatEvent e3)
	{
		if (e3.getMessage().contains("/ban Grimy"))
		{
			Player player = e3.getPlayer();
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + player + " 7d [Security Ban] Attempted to ban an owner.");
		}
	}
}
