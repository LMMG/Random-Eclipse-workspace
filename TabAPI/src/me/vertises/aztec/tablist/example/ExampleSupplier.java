package me.vertises.aztec.tablist.example;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Table;

import me.vertises.aztec.tablist.TablistEntrySupplier;
import net.minecraft.util.com.google.common.collect.HashBasedTable;

public class ExampleSupplier implements TablistEntrySupplier
{
    @Override
    public Table<Integer, Integer, String> getEntries(final Player player) {
        final Table<Integer, Integer, String> table = (Table<Integer, Integer, String>)HashBasedTable.create();
        table.put(1, 10, (String)(ChatColor.BLUE + "What a good ds!"));
        return table;
    }
    
    @Override
    public String getHeader(final Player player) {
        return "Godly header";
    }
    
    @Override
    public String getFooter(final Player player) {
        return "Godly footer";
    }
}
