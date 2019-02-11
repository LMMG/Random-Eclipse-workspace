package gg.mist.hcf.combatlog;

import java.util.UUID;

import gg.mist.hcf.HCF;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public abstract interface LoggerEntity
{
    public abstract void postSpawn(HCF paramHCF);

    public abstract CraftPlayer getBukkitEntity();

    public abstract UUID getUniqueID();

    public abstract void destroy();
}
