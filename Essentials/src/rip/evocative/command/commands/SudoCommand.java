package rip.evocative.command.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import rip.evocative.command.EssentialsModule;
import rip.evocative.util.Strings;

import org.bukkit.*;
import org.apache.commons.lang.*;

public class SudoCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equalsIgnoreCase("sudo"))
		{
			if (!(commandSender instanceof Player))
			{
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			if (!EssentialsModule.hasPermission(commandSender, "sudo"))
			{
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			final Player p = (Player) commandSender;
			if (args.length == 0 || args.length == 1)
			{
				p.sendMessage(ChatColor.RED + "Usage: /sudo <player|all> <command>");
				return true;
			}
			if (args[0].equalsIgnoreCase("all"))
			{
				Player[] onlinePlayers;
				for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i)
				{
					Player all = onlinePlayers[i];
					Bukkit.dispatchCommand((CommandSender) all, StringUtils.join((Object[]) args, " ", 1, args.length));
				}
				p.sendMessage(ChatColor.GREEN + "Forced everyone to perform command '" + ChatColor.GRAY + "/" + StringUtils.join((Object[]) args, " ", 1, args.length) + ChatColor.GREEN + "'.");
				return true;
			}
			if (Bukkit.getPlayer(args[0]) == null)
			{
				p.sendMessage(ChatColor.RED + "Player not found!");
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			Bukkit.dispatchCommand((CommandSender) target, StringUtils.join((Object[]) args, " ", 1, args.length));
			p.sendMessage(ChatColor.GREEN + "Forced " + target.getName() + " to perform command '" + ChatColor.GRAY + "/" + StringUtils.join((Object[]) args, " ", 1, args.length) + ChatColor.GREEN + "'.");
		}
		return false;
	}
}
