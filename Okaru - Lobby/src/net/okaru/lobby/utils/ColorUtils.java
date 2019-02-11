package net.okaru.lobby.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.ChatColor;

public class ColorUtils {

	public static String c(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String complete(String string) {
		String current = "";
		for (char c : string.toCharArray()) {
			String color = ChatColor.getLastColors(current);

			current = ChatColor
					.translateAlternateColorCodes(
							'&',
							current.endsWith("&")
									|| current.endsWith(ChatColor.COLOR_CHAR
											+ "") ? current + c : current
									+ color + c);
		}

		return current;
	}
	    public String translateFromString(String text) {
	        return StringEscapeUtils.unescapeJava((String)ChatColor.translateAlternateColorCodes((char)'&', (String)text));
	    }

	    public List<String> translateFromArray(List<String> text) {
	        ArrayList<String> messages = new ArrayList<String>();
	        for (String string : text) {
	            messages.add(this.translateFromString(string));
	        }
	        return messages;
	    }
}
