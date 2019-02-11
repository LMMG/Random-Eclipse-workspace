package com.hcempire.horus.supplydrop;

import com.hcempire.horus.Horus;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class SupplyDrop {

    private static Horus main = Horus.getInstance();
    private static final int PREPARATION_TIME = 2;

    @Getter @Setter private SupplyDropState state;
    @Getter private final Location location;

    public SupplyDrop(Location location) {
        this.location = location;
        this.state = SupplyDropState.PREPARING;

        new BukkitRunnable() {
            @Override
            public void run() {
                location.getWorld().spawnFallingBlock(new Location(location.getWorld(), location.getX(), location.getWorld().getMaxHeight(), location.getZ()), Material.CHEST, (byte) 0);
            }
        }.runTaskLater(main, PREPARATION_TIME);
    }
}
