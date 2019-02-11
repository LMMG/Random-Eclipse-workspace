package net.nersa.utils;

import net.nersa.kitmap.HCF;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static ChatColor getChatColorByCode(String colorCode) {
		switch (colorCode) {
			case "&b":
				return ChatColor.AQUA;
			case "&0":
				return ChatColor.BLACK;
			case "&9":
				return ChatColor.BLUE;
			case "&l":
				return ChatColor.BOLD;
			case "&3":
				return ChatColor.DARK_AQUA;
			case "&1":
				return ChatColor.DARK_BLUE;
			case "&8":
				return ChatColor.DARK_GRAY;
			case "&2":
				return ChatColor.DARK_GREEN;
			case "&5":
				return ChatColor.DARK_PURPLE;
			case "&4":
				return ChatColor.DARK_RED;
			case "&6":
				return ChatColor.GOLD;
			case "&7":
				return ChatColor.GRAY;
			case "&a":
				return ChatColor.GREEN;
			case "&o":
				return ChatColor.ITALIC;
			case "&d":
				return ChatColor.LIGHT_PURPLE;
			case "&k":
				return ChatColor.MAGIC;
			case "&c":
				return ChatColor.RED;
			case "&r":
				return ChatColor.RESET;
			case "&m":
				return ChatColor.STRIKETHROUGH;
			case "&n":
				return ChatColor.UNDERLINE;
			case "&f":
				return ChatColor.WHITE;
			case "&e":
				return ChatColor.YELLOW;
			default:
				return ChatColor.WHITE;
		}
	}

	public static String getFormattedName(Player player) {
		String prefix = HCF.getInstance().getChat().getPlayerPrefix(player).replace("_", " ");
		return color(prefix + player.getName());
	}

}