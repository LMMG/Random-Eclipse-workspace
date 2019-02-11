package gg.vexus.commands.essentials;

import org.bukkit.command.CommandSender;

public class Essentials
{

	public static boolean hasPermission(final CommandSender commandSender, String command)
	{
		return commandSender.hasPermission("common.essentials." + command);
	}
}
