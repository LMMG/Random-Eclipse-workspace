package com.hcempire.horus.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class InventorySerialisation {

    public static String itemStackArrayToJson(ItemStack[] items) throws IllegalStateException {
        return GsonFactory.getCompactGson().toJson(items);
    }

    public static ItemStack[] itemStackArrayFromJson(String data) throws IOException {
        return GsonFactory.getCompactGson().fromJson(data, new TypeToken<ItemStack[]>() {}.getType());
    }
}
