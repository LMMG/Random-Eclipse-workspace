package net.nersa.kitmap.command;

import java.util.Collections;
import java.util.List;

import net.nersa.kitmap.ConfigurationService;

import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Command used to check the current time for the server.
 */
public class ServerTimeCommand implements CommandExecutor, TabCompleter {

	private static final FastDateFormat FORMAT = FastDateFormat.getInstance("E MMM dd h:mm:ssa z yyyy", ConfigurationService.SERVER_TIME_ZONE);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "The server time is " + ChatColor.BOLD + FORMAT.format(System.currentTimeMillis()) + ChatColor.GOLD + '.');
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}
}
