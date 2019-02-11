package net.wenjapvp.kohisg.border;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.wenjapvp.kohisg.KohiSG;

public class BorderManager
{
  private final KohiSG plugin;
  private final Map<Player, List<Location>> map;
  private final byte color;
  private int border;



    public void init()
  {
    Bukkit.getServer().getPluginManager().registerEvents(new GlitchPreventListener(this), plugin);
    Bukkit.getServer().getPluginManager().registerEvents(new GlassListener(this), plugin);
  }

    public List<Location> getBlocks(Player player) {
        return map.get(player);
    }

    public void update(Player player, List<Location> toUpdate) {
        if (map.containsKey(player)) {
            for (Location location : map.get(player)) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            for (Location location2 : toUpdate) {
                player.sendBlockChange(location2, 95, color);
            }
            map.put(player, toUpdate);
        }
        else {
            for (Location location2 : toUpdate) {
                player.sendBlockChange(location2, 95, color);
            }
            map.put(player, toUpdate);
        }
    }

    public void resend(Player player) {
        if (isRunning(player)) {
            for (Location location1 : map.get(player)) {
                player.sendBlockChange(location1, 95, (byte) 5);
            }
        }
    }

    public boolean isRunning(Player player) {
        return map.containsKey(player);
    }

    public void removeGlass(Player player) {
        if (map.containsKey(player)) {
            for (Location location : map.get(player)) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            map.remove(player);
        }
    }

    public BorderManager(KohiSG plugin) {
        map = new WeakHashMap<>();
        color = 5;
        border = 500;
        this.plugin = plugin;
    }

    public KohiSG getPlugin() {
        return plugin;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
