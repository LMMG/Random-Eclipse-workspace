package net.nersa.kitmap.faction.argument;


import net.nersa.kitmap.DurationFormatter;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.type.SpawnFaction;
import net.nersa.kitmap.timer.type.StuckTimer;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.util.command.CommandArgument;

public class FactionStuckArgument extends CommandArgument {

	private HCF plugin;

	public FactionStuckArgument(HCF plugin) {
		super("stuck", "Teleport to a safe position.", new String[]{"trap", "trapped"});
		this.plugin = plugin;
	}

	public String getUsage(String label) {
		return '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage( ChatColor.RED + "This command is only executable by players.");
			return true;
		}

		Player player = (Player) sender;

		if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
			sender.sendMessage( ChatColor.RED + "You can only use this command from the overworld.");
			return true;
		}

		if (plugin.getFactionManager().getFactionAt(((Player) sender).getLocation()) instanceof SpawnFaction) {
			sender.sendMessage( ChatColor.RED + "You cannot " + label + " " + this.getName() + " inside of spawn.");
			return true;
		}

		if ((plugin.getFactionManager().getFactionAt(player.getLocation()) instanceof SpawnFaction)) {
			player.sendMessage( ChatColor.RED + "You cannot use this command in the safe-zone!");
			return true;
		}

		StuckTimer stuckTimer = this.plugin.getTimerManager().getStuckTimer();

		if (!stuckTimer.setCooldown(player, player.getUniqueId())) {
			sender.sendMessage( ChatColor.YELLOW + "Your " + stuckTimer.getDisplayName() + ChatColor.YELLOW + " timer has a remaining " + ChatColor.LIGHT_PURPLE + DurationFormatUtils.formatDurationWords(stuckTimer.getRemaining(player), true, true) + ChatColor.YELLOW + '.');
			return true;
		}

		sender.sendMessage( ChatColor.YELLOW + stuckTimer.getDisplayName() + ChatColor.YELLOW + " timer has started. " + "\nTeleportation will commence in " + ChatColor.LIGHT_PURPLE + DurationFormatter.getRemaining(stuckTimer.getRemaining(player), true, false) + ChatColor.YELLOW + ". " + "\nThis will cancel if you move more than " + 5 + " blocks.");

		return true;
	}
}
