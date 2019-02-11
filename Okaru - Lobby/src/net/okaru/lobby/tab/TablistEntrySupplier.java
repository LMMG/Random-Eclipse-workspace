
package net.okaru.lobby.tab;

import org.bukkit.entity.Player;

import com.google.common.collect.Table;

public interface TablistEntrySupplier {
    public Table<Integer, Integer, String> getEntries(Player var1);

    public String getHeader(Player var1);

    public String getFooter(Player var1);
}

