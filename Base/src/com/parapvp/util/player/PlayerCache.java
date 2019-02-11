/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.potion.PotionEffect
 */
package com.parapvp.util.player;

import com.google.common.collect.Sets;
import com.parapvp.util.InventoryUtils;
import com.parapvp.util.player.PlayerUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public class PlayerCache {
    public UUID playerUUID;
    public Location location;
    public GameMode gameMode;
    public boolean allowFlight;
    public boolean flying;
    public ItemStack[] inventory;
    public ItemStack[] armor;
    public double health;
    public int food;
    public int level;
    public float xp;
    public int fireTicks;
    public Collection<PotionEffect> potions;

    public PlayerCache(Player player) {
        this.playerUUID = player.getUniqueId();
        this.location = player.getLocation();
        this.gameMode = player.getGameMode();
        this.allowFlight = player.getAllowFlight();
        this.flying = player.isFlying();
        this.inventory = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.health = ((CraftPlayer)player).getHealth();
        this.food = player.getFoodLevel();
        this.level = player.getLevel();
        this.xp = player.getExp();
        this.fireTicks = player.getFireTicks();
        this.potions = Sets.newHashSet((Iterable)player.getActivePotionEffects());
    }

    public PlayerCache(Location location, GameMode gamemode, boolean allowFlight, boolean flying, ItemStack[] inventory, ItemStack[] armor, double health, int food, int level, float xp, int fireTicks, Collection<PotionEffect> potions) {
        this.location = location;
        this.gameMode = gamemode;
        this.allowFlight = allowFlight;
        this.flying = flying;
        this.inventory = InventoryUtils.deepClone(inventory);
        this.armor = InventoryUtils.deepClone(armor);
        this.health = health;
        this.food = food;
        this.level = level;
        this.xp = xp;
        this.fireTicks = fireTicks;
        this.potions = potions;
    }

    public void apply(Player player) {
        PlayerUtil.wipe(player);
        if (this.location != null) {
            player.teleport(this.location);
        }
        player.setGameMode(this.gameMode);
        player.setAllowFlight(this.allowFlight);
        player.setFlying(this.flying);
        player.getInventory().setContents(this.inventory);
        player.getInventory().setArmorContents(this.armor);
        player.setHealth(this.health);
        player.setFoodLevel(this.food);
        player.setLevel(this.level);
        player.setExp(this.xp);
        player.setFireTicks(this.fireTicks);
        player.getActivePotionEffects().clear();
        for (PotionEffect effect : this.potions) {
            player.addPotionEffect(effect);
        }
    }
}

