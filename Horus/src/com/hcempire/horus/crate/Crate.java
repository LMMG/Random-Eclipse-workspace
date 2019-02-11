package com.hcempire.horus.crate;

import com.alexandeh.ekko.utils.ItemBuilder;
import com.hcempire.horus.Horus;
import com.hcempire.horus.util.InventorySerialisation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Crate {

    private static Horus main = Horus.getInstance();
    private static Set<Crate> crates = new HashSet<>();

    @Getter private final String name;
    @Getter private final List<ItemStack> items;

    public Crate(String name) {
        this.name = name;
        this.items = new ArrayList<>();

        crates.add(this);
    }

    public ItemStack getKey(int amount) {
        return new ItemBuilder(Material.TRIPWIRE_HOOK).name(ChatColor.BLUE + name + " Key").amount(amount).lore(ChatColor.GOLD + "Reclaim this at spawn.").build();
    }

    public void save() {
        MongoCollection collection = main.getHorusDatabase().getCrates();

        Document document = new Document();
        document.put("name", name);
        document.put("items", InventorySerialisation.itemStackArrayToJson(items.toArray(new ItemStack[items.size()])));

        collection.replaceOne(eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public static void load() {
        MongoCollection collection = main.getHorusDatabase().getCrates();
        MongoCursor cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            String name = document.getString("name");
            List<ItemStack> items;

            try {
                items = new ArrayList<>(Arrays.asList(InventorySerialisation.itemStackArrayFromJson(document.getString("items"))));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            Crate crate = new Crate(name);
            crate.getItems().addAll(items);
        }
    }

    public static Crate getByName(String name) {
        for (Crate crate : crates) {
            if (crate.getName().equalsIgnoreCase(name)) {
                return crate;
            }
        }
        return null;
    }

    public static Crate getByKey(ItemStack itemStack) {
        for (Crate crate : crates) {
            if (crate.getKey(1).isSimilar(itemStack)) {
                return crate;
            }
        }
        return null;
    }

    public static Set<Crate> getCrates() {
        return crates;
    }
}
