package rip.cobalt.util;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.CobaltPlugin;

public class Strings {
	
	public static String PERMISSION;
	public static String CONSOLE;
	public static String USAGE;
	public static String ERROR;
	public static String LOADINGPROFILE;
	public static String LOADEDPROFILE;
	
	/*
	 * Make Strings configurable
	 */
	
	static {
		LOADEDPROFILE = ChatColor.translateAlternateColorCodes('&', CobaltPlugin.config.getString("MESSAGE.LOADEDPROFILE"));
		LOADINGPROFILE = ChatColor.translateAlternateColorCodes('&', CobaltPlugin.config.getString("MESSAGE.LOADINGPROFILE"));
		PERMISSION = ChatColor.translateAlternateColorCodes('&', CobaltPlugin.config.getString("MESSAGE.PERMISSION"));
		CONSOLE = "";
		USAGE = "";
		ERROR = "";
	}
}
