package net.wenjapvp.kohisg.commands;

import net.wenjapvp.kohisg.KohiSG;
import net.wenjapvp.kohisg.utils.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class SurvivalGamesCommand implements CommandExecutor, TabCompleter
{
	private final KohiSG kohiSG = KohiSG.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(Color.translate("&cYou can not execute this command on console."));
			return false;
		}

		Player player = (Player) sender;

		if (player.hasPermission("kohisg.command.survivalgames"))
		{
			if (arguments.length == 0 || arguments.length > 2)
			{
				player.sendMessage(Color.translate("&cUsage: /" + label + " <forcestart>"));
				return true;
			}

			if (arguments.length == 1)
			{
				if (arguments[0].equalsIgnoreCase("forcestart"))
				{
					kohiSG.getGameTimer().setForceStart(true);
					kohiSG.getGameTimer().setLeft(15);
				}
				else
				{
					player.sendMessage(Color.translate("&cSub-command called '" + arguments[0] + "' not found."));
				}
			}
		}
		else
		{
			player.sendMessage(Color.translate("&cYou do not have permissions to execute this command."));
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
	{
		List<String> suggest = new ArrayList();
		if (sender.hasPermission("kohisg.command.survivalgames"))
		{
			if (arguments.length == 1)
			{
				if (arguments[0].toLowerCase().startsWith("f"))
				{
					suggest.add("forcestart");
					return suggest;
				}
				else if (arguments[0].toLowerCase().startsWith(""))
				{
					suggest.add("forcestart");
					return suggest;
				}
			}
			else
			{
				return Collections.emptyList();
			}
			return null;
		}
		else
		{
			return Collections.emptyList();
		}
	}
}