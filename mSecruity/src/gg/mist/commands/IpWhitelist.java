package gg.mist.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.mist.mSecruity;
import net.md_5.bungee.api.ChatColor;

public class IpWhitelist implements CommandExecutor
{

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
	{
		if (!commandSender.hasPermission("common.ipwhitelist.admin"))
		{
			commandSender.sendMessage(ChatColor.RED + "Fuck off!");
			return true;
		}

		if (command.getName().equalsIgnoreCase("ipreset"))
		{
			if (args.length != 1)
			{
				commandSender.sendMessage(ChatColor.RED + "Wrong usage: /ipreset <IGN>");
				return true;
			}

			if (args[0] == null)
			{
				commandSender.sendMessage(ChatColor.RED + "Fuck you!");
				return true;
			}

			Player p = Bukkit.getPlayer(args[0]);
			OfflinePlayer ofp = Bukkit.getOfflinePlayer(args[0]);
			if ((p == null) && (ofp == null))
			{
				commandSender.sendMessage("");
				return true;
			}

			if (p != null)
			{
				mSecruity.msecruity.getDb().addToResetQueue(p);
				commandSender.sendMessage("§aYou have reset " + p.getName() + "'s IP!");
			}
			else
			{
				mSecruity.msecruity.getDb().addToResetQueue(p);
				commandSender.sendMessage("§aYou have reset " + p.getName() + "'s IP!");
			}
			return true;
		}
		return false;
	}
}
