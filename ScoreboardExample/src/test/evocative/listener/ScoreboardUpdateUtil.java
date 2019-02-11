package test.evocative.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import rip.evocative.PlayerBoard;

public class ScoreboardUpdateUtil extends BukkitRunnable
{

	@Override
	public void run()
	{
		for (Player p : Bukkit.getServer().getOnlinePlayers())
		{
			
			PlayerBoard.get(p).send();
		}
	}
}