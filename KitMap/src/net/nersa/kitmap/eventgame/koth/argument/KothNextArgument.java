package net.nersa.kitmap.eventgame.koth.argument;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.nersa.kitmap.DateTimeFormats;
import net.nersa.kitmap.HCF;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.doctordark.util.command.CommandArgument;

/**
 * An {@link CommandArgument} used to check the next running KingOfTheHill event.
 */
public class KothNextArgument extends CommandArgument {

	private final HCF plugin;

	public KothNextArgument(HCF plugin) {
		super("next", "View the next scheduled KOTH");
		this.plugin = plugin;
		this.permission = "hcf.command.koth.argument." + getName();
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		long millis = System.currentTimeMillis();
		sender.sendMessage(ChatColor.WHITE + "The server time is currently " + ChatColor.GOLD + DateTimeFormats.DAY_MTH_HR_MIN_AMPM.format(millis) + ChatColor.WHITE + '.');

		Map<LocalDateTime, String> scheduleMap = plugin.getEventScheduler().getScheduleMap();

		if (scheduleMap.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "There is not an event schedule for after now.");
			return true;
		}

		LocalDateTime now = LocalDateTime.now(DateTimeFormats.SERVER_ZONE_ID);

		for (Map.Entry<LocalDateTime, String> entry : scheduleMap.entrySet()) {
			// Only show the events that haven't been yet.
			LocalDateTime scheduleDateTime = entry.getKey();
			if (now.isAfter(scheduleDateTime)) continue;

			String monthName = scheduleDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			String weekName = scheduleDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			sender.sendMessage(ChatColor.GOLD + WordUtils.capitalizeFully(entry.getValue()) + ChatColor.WHITE + " is the next event and starts in " + ChatColor.GOLD + DateTimeFormats.HR_MIN_AMPM.format(TimeUnit.HOURS.toMillis(scheduleDateTime.getHour()) + TimeUnit.MINUTES.toMillis(scheduleDateTime.getMinute())) + ChatColor.WHITE + '.');

			return true;
		}

		sender.sendMessage(ChatColor.RED + "There is not an event scheduled after now.");
		return true;
	}
}
