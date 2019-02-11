package us.kirai.bunkers.scoreboard.provider;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import us.kirai.bunkers.Bunkers;
import us.kirai.bunkers.game.Team;
import us.kirai.bunkers.game.managers.CooldownManager;
import us.kirai.bunkers.scoreboard.SidebarEntry;
import us.kirai.bunkers.scoreboard.SidebarProvider;

public class TimerSidebarProvider implements SidebarProvider
{
    public static final String STRAIGHT_LINE;
    private final Bunkers plugin;
	private boolean add;
    
    static {
        STRAIGHT_LINE = "------------------------------------".substring(0, 14);
    }
    
    public TimerSidebarProvider(final Bunkers plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getTitle() {
        return "§9§lKirai §c[Bunkers]";
    }
    
    @Override
    public List<SidebarEntry> getLines(final Player player) {
        final List<SidebarEntry> lines = new ArrayList<SidebarEntry>();
        if (!this.plugin.getGameHandler().getTime().equalsIgnoreCase("pre game")) {
            lines.add(new SidebarEntry("", "§aGame Time§7: ", "§f" + this.plugin.getGameHandler().getTime()));
            final Team t = this.plugin.getGameHandler().getPlayers().get(player.getName());
            lines.add(new SidebarEntry("", "§aTeam§7: ", t.getColor() + StringUtils.capitalize(t.toString().toLowerCase())));
            lines.add(new SidebarEntry(" §9» ", "§aKills§7: ", "§f" + this.plugin.getTeamManager().getKills(player)));
            lines.add(new SidebarEntry(" §9» §aBala", "§ance§7: §f", this.plugin.getBalanceManager().getBalanceFormatted(player)));
            lines.add(new SidebarEntry(" §9» ", "§aPoints§7: ", "§f" + this.plugin.getPointManager().getPoints(player)));
            lines.add(new SidebarEntry(" §9» ", "§aDTR§7: ", this.plugin.getDTRManager().getDTRFormatted(Bunkers.getInstance().getGameHandler().getPlayers().get(player.getName()))));
        }
        else {
            if (this.plugin.getGameHandler().gameStartCountdown != 10L) {
                lines.add(new SidebarEntry("", "§6Countdown§7: ", "§f" + this.plugin.getGameHandler().gameStartCountdown + "s"));
            }
            else {
                lines.add(new SidebarEntry("", "§6Game State§7: ", "§eLobby"));
            }
            lines.add(new SidebarEntry("", "§6Players§7: ", "§e" + String.valueOf(this.plugin.getGameHandler().getPlayers().size()) + "/" + 16 + ":"));
            lines.add(new SidebarEntry("  §9»  ", "§cRed§7: ", "§e" + this.plugin.getGameHandler().teamSize(Team.RED) + "/" + 4));
            lines.add(new SidebarEntry("  §9»  ", "§aGreen§7: ", "§e" + this.plugin.getGameHandler().teamSize(Team.GREEN) + "/" + 4));
            lines.add(new SidebarEntry("  §9»  ", "§9Blue§7: ", "§e" + this.plugin.getGameHandler().teamSize(Team.BLUE) + "/" + 4));
            lines.add(new SidebarEntry("  §9»  ", "§eYellow§7: ", "§e" + this.plugin.getGameHandler().teamSize(Team.YELLOW) + "/" + 4));
        }
        if (Bunkers.getInstance().getCooldownManager().hasCooldown(player, CooldownManager.Type.EPEARL)) {
            lines.add(new SidebarEntry(" * ", "§9§lEnder", "pearl§7: §f" + Bunkers.getInstance().getCooldownManager().getCooldownFormatted(player, CooldownManager.Type.EPEARL)));
        }
        if (Bunkers.getInstance().getCooldownManager().hasCooldown(player, CooldownManager.Type.GAPPLE)) {
            lines.add(new SidebarEntry(" * ", "§6§lApple", "§7: §f" + Bunkers.getInstance().getCooldownManager().getCooldownFormatted(player, CooldownManager.Type.GAPPLE)));
        }
        if (!lines.isEmpty()) {
            lines.add(0, new SidebarEntry(String.valueOf(ChatColor.GRAY.toString()) + ChatColor.STRIKETHROUGH.toString(), TimerSidebarProvider.STRAIGHT_LINE, TimerSidebarProvider.STRAIGHT_LINE));
            lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY.toString(), String.valueOf(ChatColor.STRIKETHROUGH.toString()) + TimerSidebarProvider.STRAIGHT_LINE, TimerSidebarProvider.STRAIGHT_LINE));
        }
        return lines;
    }
}
