package org.hcgames.icefyre.lobby;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.potion.PotionEffect;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.kit.builder.KitBuilder;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.ItemBuilder;
import org.hcgames.icefyre.util.file.ConfigFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.hcgames.icefyre.util.packet.ProjectileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class Lobby {

    public static final Location DEFAULT_LOCATION = Bukkit.getWorlds().get(0).getSpawnLocation();

    @Getter private Icefyre main;
    @Getter @Setter private Location location;
    @Getter @Setter private KitBuilder kitBuilder;
    @Getter private ConfigFile configFile;
    @Getter private ConfigFile langFile;

    public Lobby(Icefyre main) {
        this.main = main;
        this.location = DEFAULT_LOCATION;
        this.configFile = main.getConfigFile();
        this.langFile = main.getLangFile();
        this.kitBuilder = new KitBuilder();

        File file = new File(main.getDataFolder() + File.separator + "lobby.yml");
        if (file.exists()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

            location = new Location(
                    Bukkit.getWorld(configuration.getString("WORLD")),
                    configuration.getDouble("X"),
                    configuration.getDouble("Y"),
                    configuration.getDouble("Z"),
                    (float) configuration.getDouble("YAW"),
                    (float) configuration.getDouble("PITCH")
            );

            if (configuration.contains("KIT_BUILDER")) {
                kitBuilder.setLocation(new Location(
                        Bukkit.getWorld(configuration.getString("KIT_BUILDER.WORLD")),
                        configuration.getDouble("KIT_BUILDER.X"),
                        configuration.getDouble("KIT_BUILDER.Y"),
                        configuration.getDouble("KIT_BUILDER.Z"),
                        (float) configuration.getDouble("KIT_BUILDER.YAW"),
                        (float) configuration.getDouble("KIT_BUILDER.PITCH")
                ));
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            teleportToSpawn(player);
            setupPlayer(player);
        }
    }

    public void teleportToSpawn(Player player) {
        player.teleport(location);
    }

    public void setupPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);

        for (PotionEffect effect : new HashSet<>(player.getActivePotionEffects())) {
            player.removePotionEffect(effect.getType());
        }

        Inventory inventory = player.getInventory();
        //TODO: Clean this shit up.
        inventory.setItem(0, new ItemBuilder(Material.valueOf(configFile.getString("HOTBAR_ITEMS.NO_TEAM.UNRANKED.ITEM"))).name(configFile.getString("HOTBAR_ITEMS.NO_TEAM.UNRANKED.NAME")).lore(configFile.getStringList("HOTBAR_ITEMS.NO_TEAM.UNRANKED.LORE")).build());
        inventory.setItem(1, new ItemBuilder(Material.valueOf(configFile.getString("HOTBAR_ITEMS.NO_TEAM.RANKED.ITEM"))).name(configFile.getString("HOTBAR_ITEMS.NO_TEAM.RANKED.NAME")).lore(configFile.getStringList("HOTBAR_ITEMS.NO_TEAM.RANKED.LORE")).build());
        inventory.setItem(8, new ItemBuilder(Material.valueOf(configFile.getString("HOTBAR_ITEMS.NO_TEAM.KIT_BUILDER.ITEM"))).name(configFile.getString("HOTBAR_ITEMS.NO_TEAM.KIT_BUILDER.NAME")).lore(configFile.getStringList("HOTBAR_ITEMS.NO_TEAM.KIT_BUILDER.LORE")).build());
        player.updateInventory();
        player.getInventory().setHeldItemSlot(0);
    }

    public void save() {
        if (!location.equals(DEFAULT_LOCATION)) {
            File file = new File(main.getDataFolder() + File.separator + "lobby.yml");

            if (!(file.exists())) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("WORLD", location.getWorld().getName());
            configuration.set("X", location.getX());
            configuration.set("Y", location.getY());
            configuration.set("Z", location.getZ());
            configuration.set("YAW", location.getYaw());
            configuration.set("PITCH", location.getPitch());

            if (kitBuilder.getLocation() != null) {
                Location kitBuilderLocation = kitBuilder.getLocation();
                configuration.set("KIT_BUILDER.WORLD", kitBuilderLocation.getWorld().getName());
                configuration.set("KIT_BUILDER.X", kitBuilderLocation.getX());
                configuration.set("KIT_BUILDER.Y", kitBuilderLocation.getY());
                configuration.set("KIT_BUILDER.Z", kitBuilderLocation.getZ());
                configuration.set("KIT_BUILDER.YAW", kitBuilderLocation.getYaw());
                configuration.set("KIT_BUILDER.PITCH", kitBuilderLocation.getPitch());
            }

            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isInside(Player player) {
        Profile profile = Profile.getByPlayer(player);
        return profile != null && (profile.getState() == ProfileState.IN_LOBBY || profile.getState() == ProfileState.IN_BUILDER || profile.getState() == ProfileState.IN_QUEUE);
    }
}
