package me.vertises.aztec.tablist;

import org.bukkit.entity.*;
import com.google.common.collect.*;

public interface TablistEntrySupplier
{
    Table<Integer, Integer, String> getEntries(final Player p0);
    
    String getHeader(final Player p0);
    
    String getFooter(final Player p0);
}
