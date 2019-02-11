package rip.cobalt.commands.essentials;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Essentials {
	public static boolean hasPermission(CommandSender sender, String command) {
		if (!sender.hasPermission("core." + command)) {
			return false;
		}
		return true;
	}
}
