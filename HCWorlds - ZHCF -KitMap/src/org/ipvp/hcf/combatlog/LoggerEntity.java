package org.ipvp.hcf.combatlog;


import com.google.common.base.Function;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.ipvp.hcf.HCF;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public interface LoggerEntity {

    /**
     * Spawns this NPC.
     *
     * @param plugin the plugin instance
     */
    void postSpawn(HCF plugin);

    /**
     * Gets the Bukkit entity view.
     *
     * @return the {@link org.bukkit.entity.Entity}
     */
    CraftPlayer getBukkitEntity();

    /**
     * Gets the {@link UUID} of the represented.
     *
     * @return the represented {@link UUID}
     */
    UUID getUniqueID();

    /**
     * Removes this entity.
     */
    void destroy();
}
