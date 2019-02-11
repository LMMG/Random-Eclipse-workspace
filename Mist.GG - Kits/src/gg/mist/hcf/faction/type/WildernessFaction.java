package gg.mist.hcf.faction.type;

import gg.mist.hcf.config.ConfigurationService;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Represents the {@link WildernessFaction}.
 */
public class WildernessFaction extends Faction {

    public WildernessFaction() {
        super("The Wilderness");
    }

    public WildernessFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return ConfigurationService.WILDERNESS_COLOUR + getName();
    }
}
