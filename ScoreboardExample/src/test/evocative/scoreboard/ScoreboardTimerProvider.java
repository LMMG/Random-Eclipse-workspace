package test.evocative.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import rip.evocative.provider.ScoreboardProvider;

public class ScoreboardTimerProvider implements ScoreboardProvider
{

	public static ScoreboardTimerProvider instance;
	
	public ScoreboardTimerProvider() {
		instance = this;
	}
	
	@Override
	public String getTitle(Player player)
	{
		return ChatColor.translateAlternateColorCodes('&', "&6&ltest");
	}

	@Override
	public String getHeader()
	{
		return ChatColor.translateAlternateColorCodes('&', "&7&m-------");
	}

	@Override
	public String getFooter()
	{
		return ChatColor.translateAlternateColorCodes('&', "&7&m-----");
	}

	@Override
	public List<String> getLinesFor(Player player)
	{
		List<String> lines = new ArrayList<String>();
		
		lines.add(ChatColor.translateAlternateColorCodes('&', "&6test"));
		lines.add("nigger");
		
		return lines;
	}
	
	public static ScoreboardTimerProvider getInstance() {
		return instance;
	}
}
