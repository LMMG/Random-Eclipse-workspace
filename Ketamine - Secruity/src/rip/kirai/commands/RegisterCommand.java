package rip.kirai.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mongodb.Mongo;

import net.md_5.bungee.api.ChatColor;
import rip.kirai.database.MongoDB;

public class RegisterCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
	{
		if (!(commandSender instanceof Player)) { return true; }

		Player player = (Player) commandSender;

		if (command.getName().equalsIgnoreCase("register"))
		{
			if (player.hasPermission("core.register"))
			{
				if (args.length == 0)
				{
					player.sendMessage(ChatColor.RED + "Usage /register <password>");
					return true;
				}
				if (args.length == 1)
				{
					if (isInteger(args[0]))
					{
						if (MongoDB.getInstance().isStaff(player))
						{
							player.sendMessage(ChatColor.RED + "Your already registered");
							return true;
						}
						MongoDB.getInstance().AddStaffPassword(player, args[0]);
						MongoDB.getInstance().StaffPinRefresh(player);
						player.sendMessage(ChatColor.GREEN + "You have successfully been added to the database!");
						return true;
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Please enter numbers!");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Ya dont have permz");
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isInteger(String s)
	{
		try
		{
			Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
		return true;
	}
}
