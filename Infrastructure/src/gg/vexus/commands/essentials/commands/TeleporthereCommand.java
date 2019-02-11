package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class TeleporthereCommand implements CommandExecutor
{
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("teleporthere"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "You are not a player!");
				return true;
			}
			if (!Essentials.hasPermission(sender, "teleporthere"))
			{
				sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
				return true;
			}
			final Player p = (Player) sender;
			if (args.length == 0)
			{
				p.sendMessage(ChatColor.RED + "Usage: /teleporthere <player>");
				return true;
			}
			if (args.length == 1)
			{
				if (Bukkit.getPlayer(args[0]) == null)
				{
					p.sendMessage(ChatColor.RED + "Player not found.");
					return true;
				}
				final Player target = Bukkit.getPlayer(args[0]);
				final Location loc = new Location(Bukkit.getWorld(p.getWorld().getName()), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
				target.teleport(loc);
				p.sendMessage(ChatColor.BLUE + target.getName() + " has been teleported to you.");
				return true;
			}
		}
		return false;
	}
}
