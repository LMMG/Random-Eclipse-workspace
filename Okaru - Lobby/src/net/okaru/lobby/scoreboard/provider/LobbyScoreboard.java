package net.okaru.lobby.scoreboard.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.okaru.lobby.scoreboard.ScoreboardProvider;
import net.okaru.lobby.server.Server;
import net.okaru.lobby.utils.DateUtil;
import net.okaru.permissions.grant.Grant;
import net.okaru.permissions.profile.Profile;

import org.bukkit.entity.Player;

public class LobbyScoreboard implements ScoreboardProvider
{


	@Override
	public String getTitle(Player player)
	{
		return ChatColor.translateAlternateColorCodes('&', "&6&lOkaru &r&f┃&f Lobby");
	}

	@Override
	public List<String> getLines(Player player)
	{
		List<String> lines = new ArrayList<>();
		lines.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		Profile profile = Profile.getByUuid(player.getUniqueId());
		Grant grant = profile.getActiveGrant();
		lines.add("§eCurrent Players§7:" + "§f " + Server.globalPlayers);
		lines.add("§eRank§7:§f " + grant.getRank().getData().getColorPrefix() + grant.getRank().getData().getName());
		if (grant.getDuration() == 2147483647L) {
			lines.add("§f┣ §eDuration §7:§f Permanent");
		}
		if (grant.getDuration() != 2147483647L) {
			String end = "";
			Calendar from = Calendar.getInstance();
			Calendar to = Calendar.getInstance();
			from.setTime(new Date(System.currentTimeMillis()));
			to.setTime(new Date(System.currentTimeMillis() + grant.getDuration()));
			end = DateUtil.formatDateDiff(from, to);
			
			lines.add("§f┣ §eDuration §7:§f " + end);
		}
		lines.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		return lines;
	}
}
