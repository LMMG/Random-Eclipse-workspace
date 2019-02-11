package gg.mist.listener;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffActionListener implements Listener
{

	// @SuppressWarnings("deprecation")
	// @EventHandler
	// public void onJoin(ServerConnectEvent e) {
	// if (!e.getPlayer().hasPermission("mod.diplay")) {
	// return;
	// }
	// for (ProxiedPlayer player : e.getTarget().getPlayers()) {
	// if (player.hasPermission("mod.diplay")) {
	// player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&e&l"
	// + player.getServer().getInfo().getName() + "&7) &a" + player.getName() +
	// "joined the server."));
	// }
	// }
	// }

	@EventHandler
	public void join(ServerConnectEvent e)
	{
		if ((e.getPlayer().hasPermission("mod")))
		{
			ProxiedPlayer[] arrayOfPlayer;
			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
			{
				if ((player.hasPermission("mod")))
				{
					player.sendMessage(ChatColor.GRAY + "(" + ChatColor.YELLOW + ChatColor.BOLD.toString() + player.getServer().getInfo().getName() + ChatColor.GRAY + ")"
							+ ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has joined the server");
				}
			}
		}
	}

	@EventHandler
	public void join(ServerDisconnectEvent e)
	{
		if ((e.getPlayer().hasPermission("infamous.mod")))
		{
			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
			{
				if ((player.hasPermission("infamous.mod")))
				{
					player.sendMessage(ChatColor.GRAY + "(" + ChatColor.YELLOW + ChatColor.BOLD.toString() + player.getServer().getInfo().getName() + ChatColor.GRAY + ")"
							+ ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has disconnected from the server");				}
			}
		}
	}
}
