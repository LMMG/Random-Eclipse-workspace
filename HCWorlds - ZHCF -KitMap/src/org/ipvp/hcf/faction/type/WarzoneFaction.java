package org.ipvp.hcf.faction.type;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.ipvp.hcf.ConfigurationService;

/**
 * Represents the {@link WarzoneFaction}.
 */
public class WarzoneFaction extends Faction {

	public enum FactionType {
		WARZONE;
	}
	
    public WarzoneFaction() {
        super("Warzone");
    }

    public WarzoneFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return ConfigurationService.WARZONE_COLOUR + getName();
    }
    
    public FactionType getFactionType() {
        return FactionType.WARZONE;
    }
}
