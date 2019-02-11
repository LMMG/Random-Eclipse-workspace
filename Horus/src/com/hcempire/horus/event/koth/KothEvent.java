package com.hcempire.horus.event.koth;

import com.alexandeh.ekko.utils.LocationSerialization;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.hcempire.horus.Horus;
import com.hcempire.horus.event.Event;
import com.hcempire.horus.event.EventManager;
import com.hcempire.horus.event.EventZone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.print.Doc;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class KothEvent implements Event {

    private static Horus main = Horus.getInstance();
    private static EventManager manager = EventManager.getInstance();

    @Getter private final UUID uuid;
    @Getter private final String name;
    @Getter private final EventZone zone;
    @Getter @Setter private long time;
    @Getter @Setter private long capTime;
    @Getter @Setter private long grace;
    @Getter @Setter private boolean active;
    @Getter @Setter private Player cappingPlayer;

    public KothEvent(UUID uuid, String name, EventZone zone) {
        this.uuid = uuid;
        this.name = name;
        this.zone = zone;

        manager.getEvents().add(this);
    }

    public KothEvent(String name, EventZone zone) {
        this(UUID.randomUUID(), name, zone);
    }

    public void start(long capTime) {
        this.capTime = capTime;
        this.active = true;

        Bukkit.broadcastMessage(main.getLangFile().getString("KOTH.START").replace("%KOTH%", name).replace("%TIME%", getTimeLeft()));
    }

    public void stop(boolean force) {

        if (force || cappingPlayer == null) {
            Bukkit.broadcastMessage(main.getLangFile().getString("KOTH.STOP").replace("%KOTH%", name).replace("%TIME%", getTimeLeft()));
        } else {
            Bukkit.broadcastMessage(main.getLangFile().getString("KOTH.STOP_WINNER").replace("%KOTH%", name).replace("%TIME%", getTimeLeft()).replace("%PLAYER%", cappingPlayer.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), main.getConfigFile().getString("KOTH.KEY.COMMAND").replace("%PLAYER%", cappingPlayer.getName()));
        }

        this.active = false;
        this.capTime = 0;
        this.time = 0;
        this.cappingPlayer = null;
    }

    public boolean isGrace() {
        return time + grace > System.currentTimeMillis();
    }

    public boolean isFinished() {
        return active && time + capTime - (System.currentTimeMillis() + 1000) <= 0 && cappingPlayer != null;
    }

    public void setCappingPlayer(Player player) {
        if (player == null) {
            Bukkit.broadcastMessage(main.getLangFile().getString("KOTH.KNOCKED").replace("%KOTH%", name).replace("%TIME%", getTimeLeft()).replace("%PLAYER%", cappingPlayer.getName()));

            grace = 5000;
            time = System.currentTimeMillis();
        } else {
            Bukkit.broadcastMessage(main.getLangFile().getString("KOTH.CONTESTED").replace("%KOTH%", name).replace("%TIME%", getTimeLeft()).replace("%PLAYER%", player.getName()));
        }

        this.cappingPlayer = player;
        this.time = System.currentTimeMillis();
    }

    public long getDecisecondsLeft() {
        if (cappingPlayer == null) {
            return capTime / 100;
        } else {
            return (time + capTime - System.currentTimeMillis()) / 100;
        }
    }

    public String getTimeLeft() {
        if (cappingPlayer == null) {
            return  DurationFormatUtils.formatDuration(capTime, "mm:ss");
        } else {
            return DurationFormatUtils.formatDuration(time + capTime - System.currentTimeMillis(), "mm:ss");
        }
    }
    @Override
    public List<String> getScoreboardText() {
        List<String> toReturn = new ArrayList<>();

        for (String line : main.getScoreboardFile().getStringList("PLACE_HOLDER.KOTH")) {
            line = line.replace("%KOTH%", name);
            line = line.replace("%TIME%", getTimeLeft());

            toReturn.add(line);
        }

        return toReturn;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public static void load() {
        MongoCollection collection = main.getHorusDatabase().getKoths();
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            UUID uuid = UUID.fromString(document.getString("uuid"));
            String name = document.getString("name");

            JsonArray zoneArray = new JsonParser().parse(document.getString("zone")).getAsJsonArray();
            Location firstLocation = LocationSerialization.deserializeLocation(zoneArray.get(0).getAsString());
            Location secondLocation = LocationSerialization.deserializeLocation(zoneArray.get(1).getAsString());

            new KothEvent(uuid, name, new EventZone(firstLocation, secondLocation));
        }
    }

    public void save() {
        MongoCollection collection = main.getHorusDatabase().getKoths();

        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("name", name);

        JsonArray zoneArray = new JsonArray();
        zoneArray.add(new JsonPrimitive(LocationSerialization.serializeLocation(zone.getFirstLocation())));
        zoneArray.add(new JsonPrimitive(LocationSerialization.serializeLocation(zone.getSecondLocation())));

        document.put("zone", zoneArray.toString());

        collection.replaceOne(eq("uuid", uuid.toString()), document, new UpdateOptions().upsert(true));
    }

}
