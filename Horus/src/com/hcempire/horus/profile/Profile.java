package com.hcempire.horus.profile;

import com.alexandeh.ekko.utils.LocationSerialization;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hcempire.horus.Horus;
import com.hcempire.horus.claimwall.ClaimWall;
import com.hcempire.horus.deathlookup.DeathLookup;
import com.hcempire.horus.event.koth.procedure.KothCreateProcedure;
import com.hcempire.horus.profile.cooldown.ProfileCooldown;
import com.hcempire.horus.profile.cooldown.ProfileCooldownType;
import com.hcempire.horus.profile.kit.ProfileKit;
import com.hcempire.horus.profile.kit.ProfileKitCooldown;
import com.hcempire.horus.profile.kit.ProfileKitEnergy;
import com.hcempire.horus.profile.kit.ProfileKitWarmup;
import com.hcempire.horus.profile.kit.ability.ProfileKitAbility;
import com.hcempire.horus.util.InventorySerialisation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.type;

public class Profile {

    private static Horus main = Horus.getInstance();
    private static MongoCollection collection = main.getHorusDatabase().getProfiles();
    private static Set<Profile> profiles = new HashSet<>();

    @Getter private UUID uuid;
    @Getter List<ProfileCooldown> cooldowns;
    @Getter private List<ProfileKitCooldown> kitCooldowns;
    @Getter private List<PotionEffect> cachedEffects;
    @Getter private Map<Location, ClaimWall> walls;
    @Getter private LinkedHashMap<UUID, Boolean> previousFights;
    @Getter @Setter private ProfileKitEnergy energy;
    @Getter @Setter private String name;
    @Getter @Setter private ProfileKit kit;
    @Getter @Setter private ProfileKitWarmup kitWarmup;
    @Getter @Setter private boolean leftSpawn;
    @Getter @Setter private boolean respawning;
    @Getter @Setter private boolean combatLogged;
    @Getter @Setter private long playTime;
    @Getter @Setter private DeathLookup deathLookup;
    @Getter @Setter private boolean safeLogout;
    @Getter @Setter private Location logoutLocation;
    @Getter @Setter private KothCreateProcedure kothCreateProcedure;
    @Getter @Setter private Pair<UUID, ItemStack> lastDamager;
    @Getter @Setter private Location pearlLocation;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.leftSpawn = true;
        this.cooldowns = new ArrayList<>();
        this.kitCooldowns = new ArrayList<>();
        this.cachedEffects = new ArrayList<>();
        this.previousFights = new LinkedHashMap<>();
        this.walls = new HashMap<>();

        profiles.add(this);
    }

    public ProfileCooldown getCooldownByType(ProfileCooldownType type) {
        Iterator<ProfileCooldown> iterator = cooldowns.iterator();
        while (iterator.hasNext()) {
            ProfileCooldown cooldown = iterator.next();
            if (cooldown.getType() == type)  {
                if (cooldown.isFinished()) {

                    if (type == ProfileCooldownType.LOGOUT) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                safeLogout = true;
                                Player player = Bukkit.getPlayer(uuid);
                                if (player != null) {
                                    player.kickPlayer(main.getLangFile().getString("COMBAT_LOGGER.LOGOUT_KICK"));
                                }
                            }
                        }.runTask(main);
                    }

                    iterator.remove();
                } else {
                    return cooldown;
                }
            }
        }
        return null;
    }

    public ProfileKitCooldown getKitCooldownByType(ProfileKitAbility ability) {
        Iterator<ProfileKitCooldown> iterator = kitCooldowns.iterator();
        while (iterator.hasNext()) {
            ProfileKitCooldown cooldown = iterator.next();
            if (cooldown.getAbility() == ability)  {
                if (cooldown.isFinished()) {
                    iterator.remove();
                } else {
                    return cooldown;
                }
            }
        }
        return null;
    }

    public static Profile getByName(String name) {

        for (Profile profile : profiles) {
            if (profile.getName() != null && profile.getName().equals(name)) {
                return profile;
            }
        }

        Document document = (Document) collection.find(eq("recentNameLowercase", name.toLowerCase())).first();
        if (document != null) {
            return new Profile(UUID.fromString(document.getString("uuid")));
        }

        return null;
    }

    public static Profile getByPlayer(Player player) {
        for (Profile profile : profiles) {
            if (profile.getUuid().equals(player.getUniqueId())) {
                if (profile.getName() == null) {
                    profile.setName(profile.getName());
                }
                return profile;
            }
        }
        return new Profile(player.getUniqueId());
    }

    public static Set<Profile> getProfiles() {
        return profiles;
    }
}
