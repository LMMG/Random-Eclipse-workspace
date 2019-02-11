package rip.hulk.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HulkCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
	{
		Player player = (Player) commandSender;
		if (command.getName().equalsIgnoreCase("hulk"))
		{
			if (!(commandSender instanceof Player))
			{
				commandSender.sendMessage(ChatColor.GREEN + "NO");
				return true;
			}

			if (args.length == 0)
			{
				player.sendMessage(ChatColor.RED + "Usage /hulk <version, author, commands>");
				return true;
			}
			else if (args[0].equalsIgnoreCase("version"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m&r------------------------------------"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a * &7Plugin Version: &a0.1-UNSTABLE"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m&r------------------------------------"));
			}
			else if (args[0].equalsIgnoreCase("author"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m&r------------------------------------"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a * &7Plugin Author: &aSquirtedDev, Backend"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m&r------------------------------------"));
			}
			else if (args[0].equalsIgnoreCase("commands"))
			{
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m&r------------------------------------"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a * &7Hulk Commands"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&m&r------------------------------------"));
			}
		}
		return false;
	}
}
