package gg.vexus.utils;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class StringUtils
{

	public static String join(final String seperator, final String... strings)
	{
		return String.join(seperator, (CharSequence[]) strings);
	}

	public static String colorize(final String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String getScoreboardId(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

}
