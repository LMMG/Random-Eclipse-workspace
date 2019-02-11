package rip.cobalt.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rip.cobalt.database.MongoDB;

public class OresCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String arg2, String[] args)
	{
		if(command.getName().equalsIgnoreCase("ores")) {
			if(!(commandSender instanceof Player)) {
				return true;
			}
			Player player = (Player) commandSender;
			loadOres(player);
		}
		return false;
	}
	
	public void loadOres(Player player) {
		MongoDB.getInstance().readPlayer(player);
		MongoDB.getInstance().updatesPlayer(player);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Diamond:&6 " + MongoDB.getInstance().diamond));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Emerald:&6 " + MongoDB.getInstance().emerald));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Gold:&6 " + MongoDB.getInstance().gold));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Iron:&6 " + MongoDB.getInstance().iron));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Coal:&6 " + MongoDB.getInstance().coal));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Lapis:&6 " + MongoDB.getInstance().lapis));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 * &7Redstone:&6 " + MongoDB.getInstance().redstone));
	}
}
