package org.ipvp.hcf.faction.type;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.ipvp.hcf.ConfigurationService;
import org.ipvp.hcf.faction.claim.Claim;

import net.md_5.bungee.api.ChatColor;

import java.util.Map;

/**
 * Represents the {@link SpawnFaction}.
 */
public class SpawnFaction extends ClaimableFaction implements ConfigurationSerializable {

    public SpawnFaction() {
        super("Spawn");

        this.safezone = true;
        for (World world : Bukkit.getWorlds()) {
            int radius = ConfigurationService.SPAWN_RADIUS_MAP.get(world.getEnvironment());
            if (radius > 0) {
                addClaim(new Claim(this, new Location(world, radius, 0, radius), new Location(world, -radius, world.getMaxHeight(), -radius)), null);
                    final World.Environment environment = world.getEnvironment();
                    if (environment != World.Environment.THE_END) {
                        this.addClaim(new Claim(this, new Location(world, 100.0, 0.0, 100.0), new Location(world, -100.0, (double)world.getMaxHeight(), -100.0)), null);
                    }
            }
        }
    }

    public SpawnFaction(Map<String, Object> map) {
        super(map);
    }
}