package net.okaru.lobby.scoreboard;

import java.util.List;

import org.bukkit.entity.Player;

public interface ScoreboardProvider {

	String getTitle(Player player);
	
	List<String> getLines(Player player);
	
}