/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Ordering
 *  com.google.common.collect.TreeMultimap
 *  com.google.common.primitives.Ints
 *  net.minecraft.util.gnu.trove.map.TObjectIntMap
 *  net.minecraft.util.gnu.trove.map.TObjectShortMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectShortHashMap
 *  org.apache.commons.lang3.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionType
 */
package com.parapvp.util.itemdb;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.google.common.primitives.Ints;
import com.parapvp.util.itemdb.ItemData;
import com.parapvp.util.itemdb.ItemDb;
import com.parapvp.util.itemdb.ManagedFile;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.TObjectShortMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectShortHashMap;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class SimpleItemDb
implements ItemDb {
    private static final Comparator<String> STRING_LENGTH_COMPARATOR = (o1, o2) -> o1.length() - o2.length();
    private static final Pattern PARTS_PATTERN = Pattern.compile("[^a-z0-9]");
    private final TObjectIntMap<String> items = new TObjectIntHashMap();
    private final TreeMultimap<ItemData, String> names = TreeMultimap.create((Comparator)Ordering.allEqual(), STRING_LENGTH_COMPARATOR);
    private final Map<ItemData, String> primaryName = new HashMap<ItemData, String>();
    private final TObjectShortMap<String> durabilities = new TObjectShortHashMap();
    private final ManagedFile file;
    private final Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

    public SimpleItemDb(JavaPlugin plugin) {
        this.file = new ManagedFile("items.csv", plugin);
        this.reloadItemDatabase();
    }

    @Override
    public void reloadItemDatabase() {
        if (this.file.getFile() == null) {
            return;
        }
        List<String> lines = this.file.getLines();
        if (lines.isEmpty()) {
            return;
        }
        this.durabilities.clear();
        this.items.clear();
        this.names.clear();
        this.primaryName.clear();
        for (String line : lines) {
            String[] parts;
            Material material;
            if ((line = line.trim().toLowerCase(Locale.ENGLISH)).length() > 0 && line.charAt(0) == '#' || (parts = PARTS_PATTERN.split(line)).length < 2) continue;
            try {
                int numeric = Integer.parseInt(parts[1]);
                material = Material.getMaterial((int)numeric);
            }
            catch (IllegalArgumentException ex) {
                material = Material.getMaterial((String)parts[1]);
            }
            short data = parts.length > 2 && !parts[2].equals("0") ? Short.parseShort(parts[2]) : 0;
            String itemName = parts[0].toLowerCase(Locale.ENGLISH);
            this.durabilities.put(itemName, data);
            this.items.put(itemName, material.getId());
            ItemData itemData = new ItemData(material, data);
            if (this.names.containsKey(itemData)) {
                this.names.get(itemData).add(itemName);
                continue;
            }
            this.names.put(itemData, itemName);
            this.primaryName.put(itemData, itemName);
        }
    }

    @Override
    public ItemStack getPotion(String id) {
        return this.getPotion(id, 1);
    }

    @Override
    public ItemStack getPotion(String id, int quantity) {
        int length = id.length();
        if (length <= 1) {
            return null;
        }
        boolean splash = false;
        if (length > 1 && id.endsWith("s")) {
            id = id.substring(0, --length);
            splash = true;
            if (length <= 1) {
                return null;
            }
        }
        boolean extended = false;
        if (id.endsWith("e")) {
            id = id.substring(0, --length);
            extended = true;
            if (length <= 1) {
                return null;
            }
        }
        Integer level = Ints.tryParse((String)id.substring(length - 1, length));
        id = id.substring(0, --length);
        String lowerCase = id.toLowerCase(Locale.ENGLISH);
        PotionType type = null;
        switch (lowerCase) {
            case "hp": {
                type = PotionType.FIRE_RESISTANCE;
                break;
            }
            case "rp": {
                type = PotionType.REGEN;
                break;
            }
            case "dp": {
                type = PotionType.INSTANT_DAMAGE;
                break;
            }
            case "swp": {
                type = PotionType.SPEED;
                break;
            }
            case "slp": {
                type = PotionType.SLOWNESS;
                break;
            }
            case "strp": {
                type = PotionType.STRENGTH;
                break;
            }
            case "wp": {
                type = PotionType.WEAKNESS;
                break;
            }
            case "pp": {
                type = PotionType.POISON;
                break;
            }
            case "frp": {
                type = PotionType.FIRE_RESISTANCE;
                break;
            }
            case "invp": {
                type = PotionType.INVISIBILITY;
                break;
            }
            case "nvp": {
                type = PotionType.NIGHT_VISION;
                break;
            }
            default: {
                return null;
            }
        }
        if (level == null || level > type.getMaxLevel()) {
            return null;
        }
        Potion potion = new Potion(type);
        potion.setLevel(level.intValue());
        potion.setSplash(splash);
        potion.setHasExtendedDuration(extended);
        ItemStack result = potion.toItemStack(quantity);
        result.setDurability((short)(result.getDurability() + 8192));
        return result;
    }

    @Override
    public ItemStack getItem(String id) {
        ItemStack result = this.getItem(id, 1);
        if (result == null) {
            return null;
        }
        result.setAmount(result.getMaxStackSize());
        return result;
    }

    @Override
    public ItemStack getItem(String id, int quantity) {
        String itemName;
        ItemStack result = this.getPotion(id, quantity);
        if (result != null) {
            return result;
        }
        int itemId = 0;
        short metaData = 0;
        Matcher parts = this.splitPattern.matcher(id);
        if (parts.matches()) {
            itemName = parts.group(2);
            metaData = Short.parseShort(parts.group(3));
        } else {
            itemName = id;
        }
        Integer last = Ints.tryParse((String)itemName);
        if (last != null) {
            itemId = last;
        } else {
            last = Ints.tryParse((String)id);
            if (last != null) {
                itemId = last;
            } else {
                itemName = itemName.toLowerCase(Locale.ENGLISH);
            }
        }
        if (itemId < 1) {
            Material bMaterial;
            if (this.items.containsKey(itemName)) {
                itemId = this.items.get(itemName);
                if (this.durabilities.containsKey(itemName) && metaData == 0) {
                    metaData = this.durabilities.get(itemName);
                }
            } else if (Material.getMaterial((String)itemName.toUpperCase(Locale.ENGLISH)) != null) {
                bMaterial = Material.getMaterial((String)itemName.toUpperCase(Locale.ENGLISH));
                itemId = bMaterial.getId();
            } else {
                try {
                    bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemName.toLowerCase(Locale.ENGLISH));
                    itemId = bMaterial.getId();
                }
                catch (Exception ex) {
                    return null;
                }
            }
        }
        if (itemId < 1) {
            return null;
        }
        Material mat = Material.getMaterial((int)itemId);
        if (mat == null) {
            return null;
        }
        result = new ItemStack(mat);
        result.setAmount(quantity);
        result.setDurability(metaData);
        return result;
    }

    @Override
    public List<ItemStack> getMatching(Player player, String[] args) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        PlayerInventory inventory = player.getInventory();
        if (args.length < 1 || args[0].equalsIgnoreCase("hand")) {
            items.add(player.getItemInHand());
        } else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("invent") || args[0].equalsIgnoreCase("all")) {
            for (ItemStack stack : inventory.getContents()) {
                if (stack == null || stack.getType() == Material.AIR) continue;
                items.add(stack);
            }
        } else if (args[0].equalsIgnoreCase("blocks")) {
            for (ItemStack stack : inventory.getContents()) {
                if (stack == null || stack.getType() == Material.AIR || !stack.getType().isBlock()) continue;
                items.add(stack);
            }
        } else {
            items.add(this.getItem(args[0]));
        }
        if (items.isEmpty() || ((ItemStack)items.get(0)).getType() == Material.AIR) {
            return null;
        }
        return items;
    }

    @Override
    public String getName(ItemStack item) {
        return CraftItemStack.asNMSCopy((ItemStack)item).getName();
    }

    @Deprecated
    @Override
    public String getPrimaryName(ItemStack item) {
        ItemData itemData = new ItemData(item.getType(), item.getDurability());
        String name = this.primaryName.get(itemData);
        if (name == null && (name = this.primaryName.get(itemData = new ItemData(item.getType(), (short) 0))) == null) {
            return null;
        }
        return name;
    }

    @Override
    public String getNames(ItemStack item) {
        ItemData itemData = new ItemData(item.getType(), item.getDurability());
        NavigableSet nameList = this.names.get(itemData);
        if (nameList == null && (nameList = this.names.get((itemData = new ItemData(item.getType(), (short) 0)))) == null) {
            return null;
        }
        List list = new ArrayList(nameList);
        if (nameList.size() > 15) {
            list = list.subList(0, 14);
        }
        return StringUtils.join(list, (String)", ");
    }
}

