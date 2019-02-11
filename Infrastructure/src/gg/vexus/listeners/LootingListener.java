package gg.vexus.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class LootingListener implements Listener
{

	@EventHandler
	public void onXP(PlayerExpChangeEvent e)
	{
		e.setAmount((int) (e.getAmount() * 2.5D));
	}
}
