package rip.evocative.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.evocative.Essentials;
import rip.evocative.command.EssentialsModule;
import rip.evocative.util.Strings;

public class InvseeCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equalsIgnoreCase("invsee"))
		{
			if (!(commandSender instanceof Player))
			{
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			if (!EssentialsModule.hasPermission(commandSender, "invsee"))
			{
				commandSender.sendMessage(Strings.PERMISSION);
				return true;
			}
			Player player = (Player) commandSender;
			if (args.length == 0)
			{
				player.sendMessage(ChatColor.RED + "Usage: /invsee <Players>");
				return true;
			}
			if (args.length == 1)
			{
				if (Bukkit.getPlayer(args[0]) == null)
				{
					player.sendMessage(ChatColor.RED + "Player Not Found!");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				player.openInventory(target.getInventory());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Essentials.config.getString("MESSAGE.INVSEE")));
				return true;
			}
		}
		return false;
	}
}
