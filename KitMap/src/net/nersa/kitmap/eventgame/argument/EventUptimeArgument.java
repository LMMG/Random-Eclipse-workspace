package net.nersa.kitmap.eventgame.argument;

import com.doctordark.util.command.CommandArgument;

import net.nersa.kitmap.DateTimeFormats;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.EventTimer;
import net.nersa.kitmap.eventgame.faction.EventFaction;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * A {@link CommandArgument} argument used for checking the uptime of current event.
 */
public class EventUptimeArgument extends CommandArgument {

	private final HCF plugin;

	public EventUptimeArgument(HCF plugin) {
		super("uptime", "Check the uptime of an event");
		this.plugin = plugin;
		this.permission = "hcf.command.event.argument." + getName();
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		EventTimer eventTimer = plugin.getTimerManager().getEventTimer();

		if (eventTimer.getRemaining() <= 0L) {
			sender.sendMessage(ChatColor.RED + "There is not an event running.");
			return true;
		}

		EventFaction eventFaction = eventTimer.getEventFaction();
		sender.sendMessage(ChatColor.GOLD + "Up-time of " + ChatColor.WHITE + eventTimer.getName() + "" + (eventFaction == null ? "" : ": " + ChatColor.GRAY.toString() + ChatColor.ITALIC + '(' + eventFaction.getDisplayName(sender) + ')') + ChatColor.GOLD + " is " + ChatColor.WHITE + DurationFormatUtils.formatDurationWords(eventTimer.getUptime(), true, true) + ChatColor.GOLD + ", started at " + ChatColor.WHITE + DateTimeFormats.HR_MIN_AMPM_TIMEZONE.format(eventTimer.getStartStamp()) + ChatColor.GOLD + '.');

		return true;
	}
}
