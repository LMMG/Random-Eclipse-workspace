/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.parapvp.util;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    private ItemStack stack;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (byte) 0);
    }

    public ItemBuilder(ItemStack stack) {
        Preconditions.checkNotNull((Object)stack, (Object)"ItemStack cannot be null");
        this.stack = stack;
    }

    public ItemBuilder(Material material, int amount, byte data) {
        Preconditions.checkNotNull((Object)material, (Object)"Material cannot be null");
        Preconditions.checkArgument((boolean)(amount > 0), (Object)"Amount must be positive");
        this.stack = new ItemStack(material, amount, (short)data);
    }

    public ItemBuilder displayName(String name) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder loreLine(String line) {
        boolean hasLore;
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        List lore = (hasLore = this.meta.hasLore()) ? this.meta.getLore() : new ArrayList();
        lore.add(hasLore ? lore.size() : 0, line);
        this.lore(line);
        return this;
    }

    public /* varargs */ ItemBuilder lore(String ... lore) {
        if (this.meta == null) {
            this.meta = this.stack.getItemMeta();
        }
        this.meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        return this.enchant(enchantment, level, true);
    }

    public ItemBuilder enchant(Enchantment enchantment, int level, boolean unsafe) {
        if (unsafe && level >= enchantment.getMaxLevel()) {
            this.stack.addUnsafeEnchantment(enchantment, level);
        } else {
            this.stack.addEnchantment(enchantment, level);
        }
        return this;
    }

    public ItemBuilder data(short data) {
        this.stack.setDurability(data);
        return this;
    }

    public ItemStack build() {
        if (this.meta != null) {
            this.stack.setItemMeta(this.meta);
        }
        return this.stack;
    }
}

