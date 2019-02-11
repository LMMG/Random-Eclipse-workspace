package test.evocative.scoreboard;

import org.bukkit.entity.Player;

public class Provider
{
	public static ScoreboardTimerProvider get(Player player) {
		return ScoreboardTimerProvider.getInstance();
	}
}
