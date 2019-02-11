package org.hcgames.icefyre.game.arena;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.util.ItemBuilder;
import org.hcgames.icefyre.util.file.ConfigFile;
import lombok.Getter;
import org.bukkit.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Arena {

    private static Set<Arena> arenas = new HashSet<>();
    private static Icefyre main = Icefyre.getInstance();

    @Getter private final String name, description;
    @Getter private final Location[] locations;

    public Arena(String name, String description, Location[] locations) {
        this.name = name;
        this.description = description;
        this.locations = locations;

        arenas.add(this);
    }


    public ItemStack getIcon() {
        List<String> lore = new ArrayList<>();

        for (String string : main.getLangFile().getStringList("DUEL.INVENTORY.ARENA_ITEM.LORE")) {
            lore.add(string.replace("%DESCRIPTION%", description).replace("%NAME%", name));
        }

        return new ItemBuilder(Material.valueOf(main.getLangFile().getString("DUEL.INVENTORY.ARENA_ITEM.ITEM")))
                .name(main.getLangFile().getString("DUEL.INVENTORY.ARENA_ITEM.NAME").replace("%ARENA%", name))
                .durability(main.getLangFile().getInt("DUEL.INVENTORY.ARENA_ITEM.DATA"))
                .lore(lore)
                .build();
    }

    public static void load(Icefyre main) {
        ConfigFile arenaFile = main.getArenaFile();
        for (String key : arenaFile.getConfiguration().getKeys(false)) {
            Arena arena =  new Arena(arenaFile.getString(key + ".NAME"), arenaFile.getString(key + ".DESCRIPTION"), new Location[2]);
            System.out.println("Loaded arena '" + arena.getName() + "'!");
            if (arenaFile.getConfiguration().contains(key + ".LOCATION")) {
                for (String locKey : arenaFile.getConfiguration().getConfigurationSection(key + ".LOCATION").getKeys(false)) {
                    String root = key + ".LOCATION." + locKey + ".";
                    String worldName = arenaFile.getString(root + "WORLD");
                    double x = arenaFile.getDouble(root + "X");
                    double y = arenaFile.getDouble(root + "Y");
                    double z = arenaFile.getDouble(root + "Z");
                    float yaw = arenaFile.getInt(root + "YAW");
                    float pitch = arenaFile.getInt(root + "PITCH");
                    arena.getLocations()[Integer.parseInt(locKey)] = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                }
            }
        }
    }

    public static void save(Icefyre main) {
        ConfigFile arenaFile = main.getArenaFile();

        for (String key : arenaFile.getConfiguration().getKeys(false)) {
            Arena arena = getByName(arenaFile.getString(key + ".NAME"));
            if (arena != null) {
                for (int i = 0; i < arena.getLocations().length; i++) {
                    Location location = arena.getLocations()[i];
                    if (location != null) {
                        arenaFile.getConfiguration().set(key + ".LOCATION." + i + ".WORLD", location.getWorld().getName());
                        arenaFile.getConfiguration().set(key + ".LOCATION." + i + ".X", location.getX());
                        arenaFile.getConfiguration().set(key + ".LOCATION." + i + ".Y", location.getY());
                        arenaFile.getConfiguration().set(key + ".LOCATION." + i + ".Z", location.getZ());
                        arenaFile.getConfiguration().set(key + ".LOCATION." + i + ".YAW", location.getYaw());
                        arenaFile.getConfiguration().set(key + ".LOCATION." + i + ".PITCH", location.getPitch());
                    }
                }
            }
        }

        try { arenaFile.getConfiguration().save(arenaFile.getFile()); } catch (IOException ignored) {}
    }

    public static Arena getByName(String name) {
        for (Arena arena : arenas) {
            if (arena.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", ""))) {
                return arena;
            }
        }
        return null;
    }

    public static Set<Arena> getArenas() {
        return arenas;
    }
}
