package rip.kirai.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;
import rip.kirai.database.MongoDB;

public class LoginCommand implements CommandExecutor, Listener
{

	public static ArrayList<UUID> AuthenticationPhase = new ArrayList<UUID>();

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
	{
		if (!(commandSender instanceof Player)) { return true; }

		Player player = (Player) commandSender;
		if (command.getName().equalsIgnoreCase("login"))
		{
			if (player.hasPermission("core.login"))
			{
				if (args.length == 0)
				{
					player.sendMessage(ChatColor.GREEN + "Usage /login <Password>");
					return true;
				}
				if (args.length == 1)
				{
					if (RegisterCommand.isInteger(args[0]))
					{
						if (!(AuthenticationPhase.contains(player.getUniqueId())))
						{
							player.sendMessage(ChatColor.RED + "You already logged in!");
							return true;
						}
						if (MongoDB.getInstance().isStaff(player))
						{
							MongoDB.getInstance().StaffPinRefresh(player);
							if (args[0].equalsIgnoreCase(MongoDB.getInstance().pwd))
							{
								player.sendMessage(ChatColor.GREEN + "Your now authenticated!");
								AuthenticationPhase.remove(player.getUniqueId());
								return true;
							}
							else
							{
								player.sendMessage(ChatColor.RED + "Your password is incorrect!");
								return true;
							}
						}
						else
						{
							player.sendMessage(ChatColor.RED + "Register first with /register <pwd>!");
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Enter numbers!");
					}
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You don't have sufficient permissions!");
			}
		}
		return false;
	}
	
	@EventHandler
	public void Chat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.AuthenticationPhase.contains(p.getUniqueId()))
		{
			if (!e.getMessage().startsWith("/login") || !e.getMessage().startsWith("/register"))
			{
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Please authenticate!");
				return;
			}
		}
	}

	@EventHandler
	public void Move(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.AuthenticationPhase.contains(p.getUniqueId()))
		{
			e.setTo(e.getFrom());
			p.sendMessage(ChatColor.RED + "Please Authenticate to be able to move!");
			return;
		}
	}
}
