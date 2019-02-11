package rip.kirai.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import rip.kirai.commands.LoginCommand;

public class AuthListener implements Listener
{

//	@EventHandler
//	public void Chat(AsyncPlayerChatEvent e)
//	{
//		Player p = e.getPlayer();
//		if (LoginCommand.AuthenticationPhase.contains(p.getUniqueId()))
//		{
//			if (!e.getMessage().startsWith("/login") || !e.getMessage().startsWith("/register"))
//			{
//				e.setCancelled(true);
//				p.sendMessage(ChatColor.RED + "Please authenticate!");
//				return;
//			}
//		}
//	}
//
//	@EventHandler
//	public void Move(PlayerMoveEvent e)
//	{
//		Player p = e.getPlayer();
//		if (LoginCommand.AuthenticationPhase.contains(p.getUniqueId()))
//		{
//			e.setTo(e.getFrom());
//			p.sendMessage(ChatColor.RED + "Please Authenticate to be able to move!");
//			return;
//		}
//	}
}
