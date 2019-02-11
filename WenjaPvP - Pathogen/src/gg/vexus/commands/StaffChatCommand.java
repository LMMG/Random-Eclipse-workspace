package gg.vexus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class StaffChatCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String arg2, String[] args)
	{
		Player p = (Player) commandSender;
		if (!p.hasPermission("command.staffchat"))
		{
			p.sendMessage(ChatColor.RED + "This command cant be ran by you :P");
			return true;
		}

		if (args.length != 0)
		{
			StringBuilder msg = new StringBuilder();
			for (int i = 0; i < args.length; i++)
			{
				msg.append(args[i] + " ");
			}
			Player[] aplayer;
			int j = (aplayer = Bukkit.getServer().getOnlinePlayers()).length;
			for (int i = 0; i < j; i++)
			{
				Player all = aplayer[i];
				if (all.hasPermission("command.staffchat"))
				{
					all.sendMessage("§7§o(Staff) §b" + commandSender.getName() + "§b: " + msg.toString().trim());
				}
			}
		}
		return false;
	}
}
