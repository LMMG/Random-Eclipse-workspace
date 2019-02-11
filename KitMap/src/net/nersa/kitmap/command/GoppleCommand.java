package net.nersa.kitmap.command;

import net.nersa.kitmap.DurationFormatter;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.timer.PlayerTimer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GoppleCommand implements CommandExecutor, TabCompleter {

	private final HCF plugin;

	public GoppleCommand(HCF plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}

		Player player = (Player) sender;

		PlayerTimer timer = plugin.getTimerManager().getGappleTimer();
		long remaining = timer.getRemaining(player);

		if (remaining <= 0L) {
			//sender.sendMessage(ChatColor.WHITE + "Your " + ChatColor.GOLD + "Gapple" + " timer is currently not active.");
			sender.sendMessage(ChatColor.RED + "You do not have an active Gapple cooldown.");
			return true;
		}

		//sender.sendMessage(ChatColor.WHITE + "Your " + ChatColor.GOLD + "Gapple" + ChatColor.WHITE + " timer is active for another " + ChatColor.GOLD + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.WHITE + '.');
		sender.sendMessage(ChatColor.RED + "You must wait another " + ChatColor.BOLD.toString() + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.RED + " before consuming another Gapple.");

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}
}
