package us.kirai.bunkers.tab;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import us.kirai.bunkers.tab.api.TablistEntrySupplier;
import us.kirai.bunkers.tab.api.TablistEntrySupplier;

public class TabEvent implements TablistEntrySupplier {

	
	@Override
	public Table<Integer, Integer, String> getEntries(Player player) {
		Table<Integer, Integer, String> table = HashBasedTable.create();
		table.put(1, 0, ChatColor.BLUE + "Kirai" + ChatColor.GRAY + " [Bunkers]");
		table.put(0, 3, ChatColor.BLUE + "Bunkers:" + ChatColor.GRAY + " [Online]");
		table.put(0, 4, ChatColor.BLUE + "HCFactions:" + ChatColor.GRAY + " [Offline]");
		table.put(0, 5, ChatColor.BLUE + "Practice:" + ChatColor.GRAY + " [Offline]");
		
		//mid
		table.put(1, 3, ChatColor.BLUE + "Wins:" + ChatColor.GRAY + " [3]");
		table.put(1, 4, ChatColor.BLUE + "Team:" + ChatColor.GRAY + " [Blue]");
		table.put(1, 5, ChatColor.BLUE + "Proxy:" + ChatColor.GRAY + " [EU-1]");
		
		table.put(2, 3, ChatColor.BLUE + "Kills:" + ChatColor.GRAY + " [13]");
		table.put(2, 4, ChatColor.BLUE + "Deaths:" + ChatColor.GRAY + " [41]");
		table.put(2, 5, ChatColor.BLUE + "Online:" + ChatColor.GRAY + " [1]");
		
		table.put(0, 19, ChatColor.BLUE + "1.8 Fixed" + ChatColor.GRAY + "");
		table.put(1, 19, ChatColor.BLUE + "1.8 Fixed" + ChatColor.GRAY + "");
		table.put(2, 19, ChatColor.BLUE + "1.8 Fixed" + ChatColor.GRAY + "");
		table.put(3, 19, ChatColor.BLUE + "1.8 Fixed" + ChatColor.GRAY + "");
		//
		
		table.put(3, 0, ChatColor.GRAY + "You are using 1.8" + ChatColor.YELLOW + "");
		table.put(3, 1, ChatColor.GRAY + "Please use 1.7 for" + ChatColor.YELLOW + "");
		table.put(3, 2, ChatColor.GRAY + "For the best quality!" + ChatColor.YELLOW + "");
		return table;
	}

	@Override
	public String getHeader(Player player) {
		return "§bYou are connected to 1.8 Bunkers!";
	}

	@Override
	public String getFooter(Player player) {
		return "§CWe suggest using 1.7 for the highest quality!";
	}

}
