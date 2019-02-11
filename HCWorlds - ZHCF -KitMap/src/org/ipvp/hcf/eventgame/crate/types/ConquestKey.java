package org.ipvp.hcf.eventgame.crate.types;

import org.bukkit.ChatColor;
import org.ipvp.hcf.eventgame.crate.EnderChestKey;

public class ConquestKey extends EnderChestKey
{
    public ConquestKey() {
        super("Conquest", 6);
    }
    
    @Override
    public ChatColor getColour() {
        return ChatColor.YELLOW;
    }
    
    @Override
    public boolean getBroadcastItems() {
        return true;
    }
}
