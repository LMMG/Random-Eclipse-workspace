package rip.evocative.util;

import org.bukkit.ChatColor;
import rip.evocative.Essentials;

public class Strings
{

	public static final String PERMISSION;
	public static final String CONSOLE;
	public static String PREFIX;
	private static Essentials essentials;

	static
	{
		PREFIX = ChatColor.translateAlternateColorCodes('&', essentials.config.getString("MESSAGE.PREFIX"));
		CONSOLE = ChatColor.translateAlternateColorCodes('&', essentials.config.getString("MESSAGE.CONSOLE"));
		PERMISSION = ChatColor.translateAlternateColorCodes('&', essentials.config.getString("MESSAGE.PERMISSIONS"));
	}
}
