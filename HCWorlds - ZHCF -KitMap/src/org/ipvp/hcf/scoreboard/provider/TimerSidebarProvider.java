package org.ipvp.hcf.scoreboard.provider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.ipvp.hcf.ConfigurationService;
import org.ipvp.hcf.DateTimeFormats;
import org.ipvp.hcf.DurationFormatter;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.eventgame.EventTimer;
import org.ipvp.hcf.eventgame.eotw.EotwHandler;
import org.ipvp.hcf.eventgame.faction.ConquestFaction;
import org.ipvp.hcf.eventgame.faction.EventFaction;
import org.ipvp.hcf.eventgame.faction.KothFaction;
import org.ipvp.hcf.eventgame.tracker.ConquestTracker;
import org.ipvp.hcf.faction.type.PlayerFaction;
import org.ipvp.hcf.pvpclass.PvpClass;
import org.ipvp.hcf.pvpclass.archer.ArcherClass;
//import org.ipvp.hcf.pvpclass.archer.ArcherMark;
//import org.ipvp.hcf.pvpclass.archer.ArcherMark;
import org.ipvp.hcf.pvpclass.bard.BardClass;
import org.ipvp.hcf.pvpclass.type.AssassinClass;
import org.ipvp.hcf.pvpclass.type.MinerClass;
import org.ipvp.hcf.pvpclass.type.RogueClass;
import org.ipvp.hcf.scoreboard.SidebarEntry;
import org.ipvp.hcf.scoreboard.SidebarProvider;
import org.ipvp.hcf.sotw.SotwTimer;
import org.ipvp.hcf.timer.PlayerTimer;
import org.ipvp.hcf.timer.Timer;


import com.doctordark.util.BukkitUtils;
import com.google.common.collect.Ordering;



public class TimerSidebarProvider implements SidebarProvider
{
//	private static final Comparator<Map.Entry<UUID, ArcherMark>> ARCHER_MARK_COMPARATOR;
    public static final ThreadLocal<DecimalFormat> CONQUEST_FORMATTER;
    protected static final String STRAIGHT_LINE;
    private static final SidebarEntry EMPTY_ENTRY_FILLER;
    private final HCF plugin;
    int playercount;
   
    
    static {
        CONQUEST_FORMATTER = new ThreadLocal<DecimalFormat>() {
            @Override
            protected DecimalFormat initialValue() {
                return new DecimalFormat("00.0");
            }
        };
        STRAIGHT_LINE = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 14);
        EMPTY_ENTRY_FILLER = new SidebarEntry(" ", " ", " ");
        //ARCHER_MARK_COMPARATOR = ((o1, o2) -> o1.getValue().compareTo((ArcherMark)o2.getValue()));
    }
    
    public TimerSidebarProvider(final HCF plugin) {
        this.playercount = Bukkit.getServer().getOnlinePlayers().size();

        this.plugin = plugin;
    }
    
    @Override
    public String getTitle() {
        return ChatColor.GOLD + ChatColor.BOLD.toString() + "HCFactions" + ChatColor.RED  + " [Kits]";
    }
    
    @Override
    public List<SidebarEntry> getLines(final Player player) {
        List<SidebarEntry> lines = new ArrayList<SidebarEntry>();
        final EotwHandler.EotwRunnable eotwRunnable = this.plugin.getEotwHandler().getRunnable();
        World.Environment[] values;
        for (int length = (values = World.Environment.values()).length, i = 0; i < length; ++i) {
            final World.Environment current = values[i];
            final int borderSize = ConfigurationService.BORDER_SIZES.get(current);
            lines.add(new SidebarEntry(ChatColor.DARK_RED + "Kills: " + ChatColor.RED.toString() + player.getStatistic(Statistic.PLAYER_KILLS)));
            lines.add(new SidebarEntry(ChatColor.DARK_RED + "Deaths: " + ChatColor.RED.toString() + player.getStatistic(Statistic.DEATHS)));
            if (eotwRunnable != null) {
                long remaining = eotwRunnable.getMillisUntilStarting();
                if (remaining > 0L) {
                    lines.add(new SidebarEntry(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD, "EOTW" + ChatColor.RED + " starts", " in " + ChatColor.BOLD + DurationFormatter.getRemaining(remaining, true)));
                }
                else if ((remaining = eotwRunnable.getMillisUntilCappable()) > 0L) {
                    lines.add(new SidebarEntry(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD, "EOTW" + ChatColor.RED + " cappable", " in " + ChatColor.BOLD + DurationFormatter.getRemaining(remaining, true)));
                }
            }
        }
        final SotwTimer.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
        if (sotwRunnable != null) {
            lines.add(new SidebarEntry(String.valueOf(ChatColor.DARK_GREEN.toString()) + ChatColor.BOLD, "SOTW" + ChatColor.GRAY + " ends in ", String.valueOf(ChatColor.WHITE.toString()) + DurationFormatter.getRemaining(sotwRunnable.getRemaining(), true)));
        }
        final EventTimer eventTimer = this.plugin.getTimerManager().getEventTimer();
        List<SidebarEntry> conquestLines = null;
        final EventFaction eventFaction = eventTimer.getEventFaction();
        if (eventFaction instanceof KothFaction) {
            lines.add(new SidebarEntry(eventTimer.getScoreboardPrefix(), String.valueOf(eventFaction.getScoreboardName()) + ChatColor.GRAY, ": " + ChatColor.WHITE + DurationFormatter.getRemaining(eventTimer.getRemaining(), true)));
        }
        else if (eventFaction instanceof ConquestFaction) {
            final ConquestFaction conquestFaction = (ConquestFaction)eventFaction;
            final DecimalFormat format = TimerSidebarProvider.CONQUEST_FORMATTER.get();
            conquestLines = new ArrayList<SidebarEntry>();
            conquestLines.add(new SidebarEntry(ChatColor.GOLD.toString() + ChatColor.BOLD + "Conquest", ChatColor.GOLD.toString() + ChatColor.BOLD + " Event", ChatColor.GRAY + ":"));
            conquestLines.add(new SidebarEntry("  " + ChatColor.RED.toString() + conquestFaction.getRed().getScoreboardRemaining(), ChatColor.RESET + " : ", String.valueOf(ChatColor.YELLOW.toString()) + conquestFaction.getYellow().getScoreboardRemaining()));
            conquestLines.add(new SidebarEntry("  " + ChatColor.GREEN.toString() + conquestFaction.getGreen().getScoreboardRemaining(), ChatColor.RESET + " : " + ChatColor.RESET, String.valueOf(ChatColor.AQUA.toString()) + conquestFaction.getBlue().getScoreboardRemaining()));
            final ConquestTracker conquestTracker = (ConquestTracker)conquestFaction.getEventType().getEventTracker();
            int count = 0;
            for (final Map.Entry<PlayerFaction, Integer> entry : conquestTracker.getFactionPointsMap().entrySet()) {
                String factionName = entry.getKey().getName();
                if (factionName.length() > 11) {
                    factionName = factionName.substring(0, 11);
                }
                //conquestLines.add(new SidebarEntry (ChatColor.GOLD.toString() + ChatColor.BOLD + "Points"));
                conquestLines.add(new SidebarEntry(ChatColor.GOLD, " » " + ChatColor.RED + factionName, ChatColor.GRAY + ": " + ChatColor.WHITE + entry.getValue()));
                if (++count == 3) {
                    break;
                }
            }
        }
        final PvpClass pvpClass = this.plugin.getPvpClassManager().getEquippedClass(player);
        if (pvpClass != null) {
            if (pvpClass instanceof BardClass || pvpClass instanceof AssassinClass); {
                lines.add(new SidebarEntry(ChatColor.YELLOW.toString(), ChatColor.BOLD + pvpClass.getName(), ChatColor.GRAY + ":" + ChatColor.GREEN.toString()  + " Active"));
            }
            if (pvpClass instanceof BardClass) {
                final BardClass bardClass = (BardClass)pvpClass;
                lines.add(new SidebarEntry(ChatColor.GOLD + " » ", ChatColor.RED + "Energy", ChatColor.GRAY + ": " + ChatColor.WHITE + handleBardFormat(bardClass.getEnergyMillis(player), true)));
                final long remaining2 = bardClass.getRemainingBuffDelay(player);
                if (remaining2 > 0L) {
                    lines.add(new SidebarEntry(ChatColor.GOLD + " » ", ChatColor.RED + "Buff Delay", ChatColor.GRAY + ": " + ChatColor.WHITE +  DurationFormatter.getRemaining(remaining2, true)));
                }
            }
        }
        else if (pvpClass instanceof ArcherClass) {
            final ArcherClass archerClass = (ArcherClass)pvpClass;
            if (ArcherClass.TAGGED.containsValue(player.getUniqueId())) {
                for (final UUID uuid : ArcherClass.TAGGED.keySet()) {
                    if (ArcherClass.TAGGED.get(uuid).equals(player.getUniqueId()) && Bukkit.getPlayer(uuid) != null) {
                        lines.add(new SidebarEntry(String.valueOf(ChatColor.GOLD.toString()) + " » " + ChatColor.RED.toString(), "", Bukkit.getPlayer(uuid).getName()));
                    
      } else if (pvpClass instanceof MinerClass) {
          MinerClass minerClass = (MinerClass) pvpClass;
          lines.add(new SidebarEntry(ChatColor.GOLD + " \u00bb ", ChatColor.AQUA + "Diamonds", ChatColor.GRAY + ": " + ChatColor.WHITE + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
                 //   lines.add(new SidebarEntry(ChatColor.GOLD + " \u00bb ", ChatColor.AQUA + "Invisble", ChatColor.GRAY + ": " + ChatColor.WHITE + player.hasPotionEffect(PotionEffectType.INVISIBILITY) != null ? (ChatColor.GREEN + " Yes") : (ChatColor.RED + " No") ));
                    }
                }
            }
        }
        final Collection<Timer> timers = this.plugin.getTimerManager().getTimers();
        for (final Timer timer : timers) {
            if (timer instanceof PlayerTimer) {
                final PlayerTimer playerTimer = (PlayerTimer)timer;
                final long remaining3 = playerTimer.getRemaining(player);
                if (remaining3 <= 0L) {
                    continue;
                }
                String timerName = playerTimer.getName();
                if (timerName.length() > 14) {
                    timerName = timerName.substring(0, timerName.length());
                }
                lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), String.valueOf(timerName) + ChatColor.GRAY, ": " + ChatColor.WHITE + DurationFormatter.getRemaining(remaining3, true)));
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
    private static String handleBardFormat(final long millis, final boolean trailingZero) {
        return (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(millis * 0.001);
    }
}