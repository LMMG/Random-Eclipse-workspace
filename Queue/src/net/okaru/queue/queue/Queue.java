package net.okaru.queue.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.okaru.queue.oQueue;
import net.okaru.queue.ConfigFile;
import net.okaru.queue.util.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Queue {
    private static Set<Queue> queues = new HashSet<Queue>();
    private final oQueue main;
    private final String name;
    private final String server;
    private final Map<UUID, Integer> players;
    private final int[] data;
    private final Map<String, Integer> info;

    public Queue(oQueue main, String name, String server) {
        this.main = main;
        this.name = name;
        this.server = server;
        this.players = new HashMap<UUID, Integer>();
        this.data = new int[5];
        this.data[4] = 1;
        this.info = new HashMap<String, Integer>();
        main.getPublisher().write(name + ";");
        queues.add(this);
    }

    public void addPlayer(Player player) {
        if (this.main.getPriority(player) <= 0) {
            this.sendPlayer(player);
            return;
        }
        this.main.getPublisher().write(this.name + ";add;" + player.getUniqueId().toString() + ";" + this.main.getPriority(player) + ";" + this.main.getRank(player));
    }

    public void removePlayer(Player player, boolean block) {
        this.main.getPublisher().write(this.name + ";remove;" + player.getUniqueId().toString() + ";" + (block ? "1" : "0"));
        if (block) {
            while (this.players.containsKey(player.getUniqueId()) && !this.main.isShuttingDown()) {
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendPlayer(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(this.server);
        player.sendPluginMessage(this.main, "BungeeCord", out.toByteArray());
    }

    public Set<Player> getPlayerSet() {
        HashSet<Player> toReturn = new HashSet<Player>();
        for (UUID uuid : this.players.keySet()) {
            if (Bukkit.getPlayer(uuid) == null) continue;
            toReturn.add(Bukkit.getPlayer(uuid));
        }
        return toReturn;
    }

    public static Queue getByName(String name) {
        for (Queue queue : queues) {
            if (!queue.getName().equalsIgnoreCase(name)) continue;
            return queue;
        }
        return null;
    }

    public ItemStack getIcon(Player player) {
        ConfigFile configFile = this.main.getConfigFile();
        for (String key : configFile.getConfiguration().getConfigurationSection("QUEUES").getKeys(false)) {
            if (!configFile.getString("QUEUES." + key + ".SERVER").equalsIgnoreCase(this.server)) continue;
            String status = this.main.isOffline() || this.getData()[4] == 1 ? "OFFLINE" : (this.getData()[3] == 1 ? "WHITELISTED" : (this.getPlayers().containsKey(player.getUniqueId()) ? "QUEUED" : "NOT_QUEUED"));
            ItemStack toReturn = new ItemBuilder(Material.valueOf(configFile.getString("QUEUES." + key + ".ICON." + status + ".MATERIAL"))).durability(configFile.getInt("QUEUES." + key + ".ICON." + status + ".DATA")).name(configFile.getString("QUEUES." + key + ".ICON." + status + ".NAME")).build();
            ArrayList<String> lore = new ArrayList<String>();
            Iterator<String> iterator = configFile.getStringList("QUEUES." + key + ".ICON." + status + ".LORE").iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                line = line.replace("%QUEUE%", this.name);
                line = line.replace("%ONLINE%", "" + this.getData()[1] + "");
                line = line.replace("%MAX%", "" + this.getData()[2] + "");
                if (status.equalsIgnoreCase("QUEUED")) {
                    line = line.replace("%POSITION%", this.players.get(player.getUniqueId()) + "");
                    line = line.replace("%TOTAL%", "" + this.getData()[0] + "");
                }
                lore.add(line);
            }
            return new ItemBuilder(toReturn).lore(lore).build();
        }
        return null;
    }

    public int getIconPosition() {
        ConfigFile configFile = this.main.getConfigFile();
        for (String key : configFile.getConfiguration().getConfigurationSection("QUEUES").getKeys(false)) {
            if (!configFile.getString("QUEUES." + key + ".SERVER").equalsIgnoreCase(this.server)) continue;
            return configFile.getInt("QUEUES." + key + ".ICON.POSITION");
        }
        return 0;
    }

    public static Queue getByPlayer(Player player) {
        for (Queue queue : queues) {
            if (!queue.getPlayers().containsKey(player.getUniqueId())) continue;
            return queue;
        }
        return null;
    }

    public static Set<Queue> getQueues() {
        return queues;
    }

    public oQueue getMain() {
        return this.main;
    }

    public String getName() {
        return this.name;
    }

    public String getServer() {
        return this.server;
    }

    public Map<UUID, Integer> getPlayers() {
        return this.players;
    }

    public int[] getData() {
        return this.data;
    }

    public Map<String, Integer> getInfo() {
        return this.info;
    }
}

