/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.parapvp.util.itemdb;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemDb {
    public void reloadItemDatabase();

    public ItemStack getPotion(String var1);

    public ItemStack getPotion(String var1, int var2);

    public ItemStack getItem(String var1);

    public ItemStack getItem(String var1, int var2);

    public String getName(ItemStack var1);

    @Deprecated
    public String getPrimaryName(ItemStack var1);

    public String getNames(ItemStack var1);

    public List<ItemStack> getMatching(Player var1, String[] var2);
}

