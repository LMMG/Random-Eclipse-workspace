package rip.evocative.command;

import org.bukkit.command.CommandSender;

public class EssentialsModule
{

	public static boolean hasPermission(CommandSender sender, String command)
	{
		return sender.hasPermission("essentials.permission." + command);
	}
}
