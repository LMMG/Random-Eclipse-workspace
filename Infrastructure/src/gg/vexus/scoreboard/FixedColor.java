package gg.vexus.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import server.wenjapvp.hcf.HCF;


public class FixedColor implements Listener
{
	@EventHandler
	public void onJoin(final PlayerJoinEvent e)
	{
		Player[] onlinePlayers;
		for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i)
		{
			final Player all = onlinePlayers[i];
			updateTab(all);
		}
	}

	public static void updateTab(final Player player)
	{
		boolean newScoreboard = false;
		Scoreboard scoreboard;
		if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard())
		{
			scoreboard = player.getScoreboard();
		}
		else
		{
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			newScoreboard = true;
		}
		final Team friendly = getExistingOrCreateNewTeam(player, "friendly", scoreboard, ChatColor.DARK_GREEN);
		final Team enemy = getExistingOrCreateNewTeam(player, "enemy", scoreboard, ChatColor.RED);
		final Team ally = getExistingOrCreateNewTeam(player, "ally", scoreboard, ChatColor.BLUE);
		Player[] onlinePlayers;
		for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i)
		{
			final Player all = onlinePlayers[i];
			if (HCF.getPlugin().getFactionManager().getPlayerFaction(player) == null)
			{
				enemy.addEntry(all.getName());
			}
			else if (HCF.getPlugin().getFactionManager().getPlayerFaction(all) == null)
			{
				enemy.addEntry(all.getName());
			}
			else if (HCF.getPlugin().getFactionManager().getPlayerFaction(player) == HCF.getPlugin().getFactionManager().getPlayerFaction(all))
			{
				friendly.addEntry(all.getName());
			}
			else if (HCF.getPlugin().getFactionManager().getPlayerFaction(player).getAllied().contains(all.getUniqueId()))
			{
				ally.addEntry(all.getName());
			}
			else
			{
				enemy.addEntry(all.getName());
			}
		}
		if (newScoreboard)
		{
			player.setScoreboard(scoreboard);
		}
	}

	private static Team getExistingOrCreateNewTeam(final Player player, final String string, final Scoreboard scoreboard, final ChatColor prefix)
	{
		Team toReturn = scoreboard.getTeam(string);
		if (toReturn == null)
		{
			toReturn = scoreboard.registerNewTeam(string);
			toReturn.setPrefix(new StringBuilder().append(prefix).toString());
			if (string == "friendly")
			{
				toReturn.setCanSeeFriendlyInvisibles(true);
			}
		}
		return toReturn;
	}
}