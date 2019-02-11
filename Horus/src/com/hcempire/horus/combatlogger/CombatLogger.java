package com.hcempire.horus.combatlogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.hcempire.horus.Horus;
import com.hcempire.horus.profile.deathban.ProfileDeathban;
import com.hcempire.horus.profile.fight.ProfileFightEffect;

import lombok.Getter;

public class CombatLogger {

    private static Map<CombatLogger, Long> loggers = new HashMap<>();
    private static Horus main = Horus.getInstance();
    public static final EntityType ENTITY_TYPE = EntityType.valueOf(main.getConfigFile().getString("COMBAT_LOGGER.ENTITY_TYPE"));
    private static final String ENTITY_CUSTOM_NAME = main.getLangFile().getString("COMBAT_LOGGER.NAME");

    @Getter private final LivingEntity entity;
    @Getter private final String name;
    @Getter private final UUID uuid;
    @Getter private final long deathbanDuration;
    @Getter private final double hunger;
    @Getter private final List<ProfileFightEffect> effects;
    @Getter private ItemStack[] contents, armor;

    public CombatLogger(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.deathbanDuration = ProfileDeathban.getDuration(player);
        this.contents = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.hunger = player.getFoodLevel();
        this.effects = new ArrayList<>();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            effects.add(new ProfileFightEffect(effect));
        }

        this.entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), ENTITY_TYPE);
        this.entity.setMaxHealth(((CraftPlayer)player).getMaxHealth() * 5);
        this.entity.setHealth(((CraftPlayer)player).getHealth() * 5);
        this.entity.setFallDistance(player.getFallDistance());
        this.entity.setLastDamageCause(player.getLastDamageCause());
        this.entity.setCustomName(ENTITY_CUSTOM_NAME.replace("%PLAYER%", player.getName()));

        if (this.entity instanceof Villager) {
            Villager villager = (Villager) this.entity;
            villager.setProfession(Villager.Profession.FARMER);
            villager.setAdult();
        }

        this.entity.setCustomNameVisible(true);

        loggers.put(this, System.currentTimeMillis());
    }

    public static CombatLogger getByEntity(LivingEntity entity) {
        for (CombatLogger logger : loggers.keySet()) {
            if (logger.getEntity().getEntityId() == entity.getEntityId()) {
                return logger;
            }
        }
        return null;
    }

    public static CombatLogger getByPlayer(Player player) {
        for (CombatLogger logger : loggers.keySet()) {
            if (logger.getUuid().equals(player.getUniqueId())) {
                return logger;
            }
        }
        return null;
    }

    public static Set<CombatLogger> getLoggers() {
        return loggers.keySet();
    }

    public static Map<CombatLogger, Long> getLoggersMap() {
        return loggers;
    }
}
