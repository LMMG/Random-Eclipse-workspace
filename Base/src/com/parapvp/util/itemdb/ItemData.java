/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 */
package com.parapvp.util.itemdb;

import com.parapvp.base.BasePlugin;
import com.parapvp.util.itemdb.ItemDb;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemData
implements ConfigurationSerializable {
    private final Material material;
    private final short itemData;

    public ItemData(MaterialData data) {
        this(data.getItemType(), data.getData());
    }

    public ItemData(ItemStack stack) {
        this(stack.getType(), stack.getData().getData());
    }

    public ItemData(Material material, short itemData) {
        this.material = material;
        this.itemData = itemData;
    }

    public ItemData(Map<String, Object> map) {
        Object object = map.get("itemType");
        if (!(object instanceof String)) {
            throw new AssertionError((Object)"Incorrectly configurised");
        }
        this.material = Material.getMaterial((String)((String)object));
        object = map.get("itemData");
        if (object instanceof Short) {
            this.itemData = (Short)object;
            return;
        }
        throw new AssertionError((Object)"Incorrectly configurised");
    }

    public static ItemData fromItemName(String string) {
        ItemStack stack = BasePlugin.getPlugin().getItemDb().getItem(string);
        return new ItemData(stack.getType(), stack.getData().getData());
    }

    public static ItemData fromStringValue(String value) {
        int firstBracketIndex = value.indexOf(40);
        if (firstBracketIndex == -1) {
            return null;
        }
        int otherBracketIndex = value.indexOf(41);
        if (otherBracketIndex == -1) {
            return null;
        }
        String itemName = value.substring(0, firstBracketIndex);
        String itemData = value.substring(firstBracketIndex + 1, otherBracketIndex);
        Material material = Material.getMaterial((String)itemName);
        return new ItemData(material, Short.parseShort(itemData));
    }

    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("itemType", this.material.name());
        map.put("itemData", this.itemData);
        return map;
    }

    public Material getMaterial() {
        return this.material;
    }

    @Deprecated
    public short getItemData() {
        return this.itemData;
    }

    public String getItemName() {
        return BasePlugin.getPlugin().getItemDb().getName(new ItemStack(this.material, 1, this.itemData));
    }

    public String toString() {
        return String.valueOf(this.material.name()) + "(" + String.valueOf(this.itemData) + ")";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ItemData itemData1 = (ItemData)o;
        return this.itemData == itemData1.itemData && this.material == itemData1.material;
    }

    public int hashCode() {
        int result = this.material != null ? this.material.hashCode() : 0;
        result = 31 * result + this.itemData;
        return result;
    }
}

