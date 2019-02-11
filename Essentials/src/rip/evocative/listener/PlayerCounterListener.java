package rip.evocative.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import rip.evocative.Essentials;

public class PlayerCounterListener implements Listener
{
	public static HashMap<String, Integer> playerCount;
	private static Essentials essentials;

	static
	{
		playerCount = new HashMap<String, Integer>();
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent e)
	{
		if (essentials.config.getBoolean("PLAYERCOUNT.MODULE"))
		{
			final Player p = e.getPlayer();
			if (!p.hasPlayedBefore())
			{
				this.addPlayer();
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', essentials.getInstance().getConfig().getString("MESSAGE.PLAYERCOUNTER").replace("%player%", p.getName())).replace("%number%", String.valueOf(essentials.getInstance().serverData.getInt("playerCount") + playerCount.get("count"))));
			}
		}
	}

	public void addPlayer()
	{
		if (essentials.config.getBoolean("PLAYERCOUNT.MODULE"))
		{
			if (playerCount.containsKey("count"))
			{
				playerCount.put("count", playerCount.get("count") + 1);
			}
			else
			{
				playerCount.put("count", 1);
			}
		}
	}
}
