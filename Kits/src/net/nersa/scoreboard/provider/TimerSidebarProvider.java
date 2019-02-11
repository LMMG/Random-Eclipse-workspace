package net.nersa.scoreboard.provider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.doctordark.util.BukkitUtils;
import com.parapvp.base.BasePlugin;

import net.nersa.kitmap.ConfigurationService;
import net.nersa.kitmap.DateTimeFormats;
import net.nersa.kitmap.DurationFormatter;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.EventTimer;
import net.nersa.kitmap.eventgame.faction.ConquestFaction;
import net.nersa.kitmap.eventgame.faction.EventFaction;
import net.nersa.kitmap.eventgame.faction.KothFaction;
import net.nersa.kitmap.eventgame.tracker.ConquestTracker;
import net.nersa.kitmap.faction.type.PlayerFaction;
import net.nersa.kitmap.listener.KillstreakListener;
import net.nersa.kitmap.pvpclass.PvpClass;
import net.nersa.kitmap.pvpclass.archer.ArcherClass;
import net.nersa.kitmap.pvpclass.archer.ArcherMark;
import net.nersa.kitmap.pvpclass.bard.BardClass;
import net.nersa.kitmap.pvpclass.type.MinerClass;
import net.nersa.kitmap.timer.PlayerTimer;
import net.nersa.kitmap.timer.Timer;
import net.nersa.scoreboard.SidebarEntry;
import net.nersa.scoreboard.SidebarProvider;

public class TimerSidebarProvider implements SidebarProvider {

	public static ThreadLocal<DecimalFormat> CONQUEST_FORMATTER = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			return new DecimalFormat("00.0");
		}
	};
	protected static String STRAIGHT_LINE = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 14);
	protected static String STAFFMODE_LINE = ChatColor.translateAlternateColorCodes('&', "&7&m------------");
	private static SidebarEntry EMPTY_ENTRY_FILLER = new SidebarEntry(" "," "," ");
	private static Comparator<Map.Entry<UUID, ArcherMark>> ARCHER_MARK_COMPARATOR = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

	private HCF plugin;

	public TimerSidebarProvider(HCF plugin) {
		this.plugin = plugin;
	}

	int playercount = Bukkit.getOnlinePlayers().size();

	@Override
	public String getTitle() {
		return ConfigurationService.SCOREBOARD_TITLE;
	}

	ArrayList<String> kills = new ArrayList<String>();

	@Override
	public List<SidebarEntry> getLines(Player player) {
		List<SidebarEntry> lines = new ArrayList<>();
		lines.add(new SidebarEntry("§6§lPlayer", " Stats:", null));
		lines.add(new SidebarEntry(ChatColor.RED + " " + ChatColor.BOLD, "Kills" + ChatColor.GRAY + ": " + ChatColor.RED, plugin.getPlayerManager().getPlayerData(player).getKills()));
		lines.add(new SidebarEntry(ChatColor.RED + " " + ChatColor.BOLD, "Deaths" + ChatColor.GRAY + ": " + ChatColor.RED, plugin.getPlayerManager().getPlayerData(player).getDeaths()));

		if (player.hasPermission("mod"))
		{
			if (BasePlugin.getPlugin().getServerHandler().isChatSlowed())
			{
				lines.add(new SidebarEntry("", "§eChat §6» §cSl", "owed " + BasePlugin.getPlugin().getServerHandler().getChatSlowedDelay()));
			}
			else
			{
				lines.add(new SidebarEntry("§eChat §6» §a", "Fast", " (0s)"));
			}
		}
		
		if(KillstreakListener.kills.containsKey(player.getUniqueId()) && KillstreakListener.kills.get(player.getUniqueId()) != null && KillstreakListener.kills.get(player.getUniqueId()) >= 3 &&( (KillstreakListener.kills.get(player.getUniqueId()) % 5 == 0) || (KillstreakListener.kills.get(player.getUniqueId()) == 3)) || kills.contains(player.getUniqueId().toString()) &&
				!new SidebarEntry(ChatColor.GOLD.toString() + ChatColor.BOLD.toString(),  "Killstreak" + "§7: " + ChatColor.WHITE, KillstreakListener.kills.get(player.getUniqueId())).suffix.toLowerCase().contains("null")){
			lines.add(new SidebarEntry(ChatColor.GOLD.toString() + ChatColor.BOLD.toString(),  "Killstreak" + "§7: " + ChatColor.WHITE, KillstreakListener.kills.get(player.getUniqueId())));
			if(!kills.contains(player.getUniqueId().toString())){
				kills.add(player.getUniqueId().toString());
			}
		}
		
		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction != null && playerFaction.getFocus() != null){
			lines.add(new SidebarEntry(ChatColor.RED + "" + ChatColor.BOLD, "Focus" + ChatColor.GRAY + ": " + ChatColor.WHITE, BukkitUtils.offlinePlayerWithNameOrUUID(playerFaction.getFocus().toString()).getName()));
		}
		
		EventTimer eventTimer = plugin.getTimerManager().getEventTimer();
		List<SidebarEntry> conquestLines = null;

		EventFaction eventFaction = eventTimer.getEventFaction();
		if (eventFaction instanceof KothFaction) {
			// lines.add(new SidebarEntry(ChatColor.AQUA.toString(), ChatColor.BOLD + "Active Events", null));
			lines.add(new SidebarEntry(eventTimer.getScoreboardPrefix(), eventFaction.getScoreboardName() + ChatColor.GRAY, ": " +
					ChatColor.GOLD + DurationFormatter.getRemaining(eventTimer.getRemaining(), true)));
		} else if (eventFaction instanceof ConquestFaction) {
			ConquestFaction conquestFaction = (ConquestFaction) eventFaction;
			DecimalFormat format = CONQUEST_FORMATTER.get();

			conquestLines = new ArrayList<>();
			conquestLines.add(new SidebarEntry(ChatColor.GOLD.toString(), ChatColor.BOLD + "Conquest Event" + ChatColor.GRAY, ":"));

			conquestLines.add(new SidebarEntry("  " +
					ChatColor.RED.toString() + (int)Math.round(conquestFaction.getRed().getRemainingCaptureMillis() / 1000.00),
					ChatColor.RESET + " ",
					ChatColor.YELLOW.toString() + (int)Math.round(conquestFaction.getYellow().getRemainingCaptureMillis() / 1000.00)));

			conquestLines.add(new SidebarEntry("  " +
					ChatColor.GREEN.toString() + (int)Math.round(conquestFaction.getGreen().getRemainingCaptureMillis() / 1000.00),
					ChatColor.RESET + " " + ChatColor.RESET,
					ChatColor.AQUA.toString() + (int)Math.round(conquestFaction.getBlue().getRemainingCaptureMillis() / 1000.00)));

			// Show the top 3 factions next.
			ConquestTracker conquestTracker = (ConquestTracker) conquestFaction.getEventType().getEventTracker();
			int count = 0;
			for (Map.Entry<PlayerFaction, Integer> entry : conquestTracker.getFactionPointsMap().entrySet()) {
				String factionName = entry.getKey().getName();
				if (factionName.length() > 14) factionName = factionName.substring(0, 14);
				conquestLines.add(new SidebarEntry(ChatColor.WHITE, "⎥ " + ChatColor.LIGHT_PURPLE + factionName, ChatColor.GRAY + ": " + ChatColor.GOLD + entry.getValue()));
				if (++count == 3) break;
			}
		}

		// Show the current PVP Class statistics of the player.
		PvpClass pvpClass = plugin.getPvpClassManager().getEquippedClass(player);
		if (pvpClass != null) {
			if (pvpClass instanceof BardClass) {
				BardClass bardClass = (BardClass)pvpClass;
				lines.add(new SidebarEntry(ChatColor.YELLOW + "Active", " Class", ChatColor.GRAY + ": " + ChatColor.RED + "Bard"));
				lines.add(new SidebarEntry(ChatColor.AQUA + "Bard", ChatColor.AQUA + " Energy", ChatColor.GRAY + ": " + ChatColor.WHITE + handleBardFormat(bardClass.getEnergyMillis(player), true)));
				long remaining2 = bardClass.getRemainingBuffDelay(player);
				if (remaining2 > 0L) {
					lines.add(new SidebarEntry(ChatColor.AQUA + "Buff ", "Cooldown" + ChatColor.GRAY + ": " + ChatColor.WHITE, DurationFormatter.getRemaining(remaining2, true)));
				}
			} else if (pvpClass instanceof ArcherClass) {
				lines.add(new SidebarEntry(ChatColor.YELLOW + "Active", " Class", ChatColor.GRAY + ": " + ChatColor.WHITE + "Archer"));

				if (ArcherClass.TAGGED.containsValue(player.getUniqueId())) {
					for (UUID uuid : ArcherClass.TAGGED.keySet()) {
						if (((ArcherClass.TAGGED.get(uuid)).equals(player.getUniqueId())) && (Bukkit.getPlayer(uuid) != null)) {
							lines.add(new SidebarEntry(ChatColor.WHITE.toString() + " ⎥  " + ChatColor.LIGHT_PURPLE.toString(), "", Bukkit.getPlayer(uuid).getName()));
						}
					}
				}
			} else if (pvpClass instanceof MinerClass) {
				lines.add(new SidebarEntry(ChatColor.YELLOW + "Active", ChatColor.YELLOW + " Class", ChatColor.GRAY + ": " + ChatColor.WHITE + "Miner"));
			}
		}

		Collection<Timer> timers = plugin.getTimerManager().getTimers();

		for (Timer timer : timers) {
			if (timer instanceof PlayerTimer) {
				PlayerTimer playerTimer = (PlayerTimer) timer;
				long remaining = playerTimer.getRemaining(player);
				if (remaining <= 0) continue;

				String timerName = playerTimer.getName();
				if (timerName.length() > 14) timerName = timerName.substring(0, timerName.length());
				lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), timerName + ChatColor.GRAY, ": " + ChatColor.WHITE + DurationFormatter.getRemaining(remaining, true)));
			}
		}

		if (conquestLines != null && !conquestLines.isEmpty()) {
			if (!lines.isEmpty()) {
				conquestLines.add(new SidebarEntry("", "", ""));
			}

			conquestLines.addAll(lines);
			lines = conquestLines;
		}

		if (!lines.isEmpty()) {
			lines.add(0, new SidebarEntry(ChatColor.GRAY, TimerSidebarProvider.STRAIGHT_LINE, TimerSidebarProvider.STRAIGHT_LINE));
			lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + TimerSidebarProvider.STRAIGHT_LINE, TimerSidebarProvider.STRAIGHT_LINE));
		}

		return lines;
	}

	private static String handleBardFormat(long millis, boolean trailingZero) {
		return (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(millis * 0.001);
	}
}