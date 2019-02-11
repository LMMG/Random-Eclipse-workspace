package net.okaru.lobby.listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import net.okaru.lobby.tab.TablistEntrySupplier;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class PlayerTabEvent
  implements TablistEntrySupplier
{
  @Override
public Table<Integer, Integer, String> getEntries(Player player)
  {
    Table<Integer, Integer, String> table = HashBasedTable.create();
    
    //Time module
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
    SimpleDateFormat df = new SimpleDateFormat("h:mm a EEEEEE");
    df.setTimeZone(cal.getTimeZone());
    
    //Send tab to players
    
    table.put(Integer.valueOf(1), Integer.valueOf(1), ChatColor.GOLD.toString() + ChatColor.BOLD + "       Okaru");
    table.put(Integer.valueOf(1), Integer.valueOf(2), ChatColor.WHITE.toString() + "      Hardcore");
    table.put(Integer.valueOf(1), Integer.valueOf(19), ChatColor.GRAY.toString() + "www.okaru.net");
      
    table.put(Integer.valueOf(2), Integer.valueOf(0), ChatColor.GRAY.toString() + ChatColor.ITALIC + df.format(cal.getTime()));
    
    table.put(Integer.valueOf(2), Integer.valueOf(4), ChatColor.GOLD.toString() + ChatColor.BOLD + "  Network Points");
    table.put(Integer.valueOf(2), Integer.valueOf(5), ChatColor.YELLOW.toString() + "Conquest"+ ChatColor.GRAY + ": " + ChatColor.WHITE + "0");
    table.put(Integer.valueOf(2), Integer.valueOf(6), ChatColor.YELLOW.toString() + "Honor"+ ChatColor.GRAY + ": " + ChatColor.WHITE + "0");
    
    
    
    
    /*
    table.put(Integer.valueOf(1), Integer.valueOf(0), ChatColor.DARK_PURPLE + "Prevail" + ChatColor.GRAY + " [Bunkers]");
    table.put(Integer.valueOf(0), Integer.valueOf(3), ChatColor.LIGHT_PURPLE + "Bunkers:" + ChatColor.GRAY + " [Online]");
    table.put(Integer.valueOf(0), Integer.valueOf(4), ChatColor.LIGHT_PURPLE + "HCF:" + ChatColor.GRAY + " [Offline]");
    table.put(Integer.valueOf(0), Integer.valueOf(5), ChatColor.LIGHT_PURPLE + "Practice:" + ChatColor.GRAY + " [Offline]");
    
    table.put(Integer.valueOf(1), Integer.valueOf(3), ChatColor.LIGHT_PURPLE + "Team:" + ChatColor.GRAY + " [Red]");
    table.put(Integer.valueOf(1), Integer.valueOf(4), ChatColor.LIGHT_PURPLE + "DTR:" + ChatColor.GRAY + " [5.0]");
    table.put(Integer.valueOf(1), Integer.valueOf(5), ChatColor.LIGHT_PURPLE + "Balance:" + ChatColor.GRAY + " [$]");
    
    table.put(Integer.valueOf(2), Integer.valueOf(3), ChatColor.YELLOW + "" + ChatColor.GRAY + "");
    table.put(Integer.valueOf(2), Integer.valueOf(4), ChatColor.YELLOW + "" + ChatColor.GRAY + "");
    table.put(Integer.valueOf(2), Integer.valueOf(5), ChatColor.YELLOW + "" + ChatColor.GRAY + "");
    
    table.put(Integer.valueOf(0), Integer.valueOf(19), ChatColor.RED + "1.8 Fixed" + ChatColor.GRAY);
    table.put(Integer.valueOf(1), Integer.valueOf(19), ChatColor.RED + "1.8 Fixed" + ChatColor.GRAY);
    table.put(Integer.valueOf(2), Integer.valueOf(19), ChatColor.RED + "1.8 Fixed" + ChatColor.GRAY);
    table.put(Integer.valueOf(3), Integer.valueOf(19), ChatColor.RED + "1.8 Fixed" + ChatColor.GRAY);
    
    table.put(Integer.valueOf(3), Integer.valueOf(0), ChatColor.GRAY + "You are using 1.8" + ChatColor.YELLOW);
    table.put(Integer.valueOf(3), Integer.valueOf(1), ChatColor.GRAY + "Please use 1.7 for" + ChatColor.YELLOW);
    table.put(Integer.valueOf(3), Integer.valueOf(2), ChatColor.GRAY + "For the best quality!" + ChatColor.YELLOW);*/
    
    
    return table;
  }
  
  @Override
  public String getFooter(Player player)
  {
    //1.8 is natively blocked via the core so this shoudn't be printed anyways but fuck it.
	return "We suggest using 1.7 for the highest quality!";
  }
  
  @Override
  public String getHeader(Player player)
  {
	//1.8 is natively blocked via the core so this shoudn't be printed anyways but fuck it.
    return "You are currently connected with 1.8";
  }
}
