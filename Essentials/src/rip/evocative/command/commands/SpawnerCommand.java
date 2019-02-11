package rip.evocative.command.commands;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rip.evocative.command.EssentialsModule;
import rip.evocative.util.Strings;

public class SpawnerCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args)
	{
		if (command.getName().equalsIgnoreCase("spawner"))
		{
			if (!(commandSender instanceof Player))
			{
				commandSender.sendMessage(Strings.CONSOLE);
				return true;
			}
			if (EssentialsModule.hasPermission(commandSender, "spawner"))
			{
				commandSender.sendMessage(Strings.PERMISSION);
				return true;
			}
			Player player = (Player) commandSender;
			if (args.length == 0)
			{
				player.sendMessage(ChatColor.RED + "Usage: /spawner <type>");
				return true;
			}
			if (args.length == 1)
			{
				if (player.getTargetBlock(null, 0).getType() != Material.MOB_SPAWNER)
				{
					player.sendMessage(ChatColor.RED + "Your not looking at a spawner");
					return true;
				}
				EntityType type;
				try
				{
					type = EntityType.valueOf(args[0].toUpperCase());
				}
				catch (Exception e)
				{
					player.sendMessage(ChatColor.RED + args[0] + " is not a valid mob type.");
					return true;
				}
				CreatureSpawner spawner = (CreatureSpawner) player.getTargetBlock(null, 0).getState();
				spawner.setSpawnedType(type);
				spawner.update();
				player.sendMessage(ChatColor.BLUE + "Spawner type changed to " + type.getName() + ".");
				return true;
			}
		}
		return false;
	}
}
