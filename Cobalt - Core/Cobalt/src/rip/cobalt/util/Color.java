
package rip.cobalt.util;

import org.bukkit.ChatColor;

public class Color
{
	public static String color(String toColor)
	{
		return ChatColor.translateAlternateColorCodes('&', toColor);
	}
}
