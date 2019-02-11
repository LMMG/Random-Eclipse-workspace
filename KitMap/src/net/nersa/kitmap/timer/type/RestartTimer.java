package net.nersa.kitmap.timer.type;

import net.nersa.kitmap.timer.GlobalTimer;

import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

public class RestartTimer extends GlobalTimer {
	public RestartTimer() {
		super("Restart", TimeUnit.MINUTES.toMillis(30L));
	}

	public String getScoreboardPrefix() {
		return ChatColor.RED.toString() + ChatColor.BOLD;
	}
}
