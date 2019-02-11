package org.ipvp.hcf.eventgame.crate.types;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.ipvp.hcf.ConfigurationService;
import org.ipvp.hcf.eventgame.crate.EnderChestKey;

import com.doctordark.util.ItemBuilder;

public class Tier1
  extends EnderChestKey
{
  public Tier1()
  {
    super("Tier1", 3);
    setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).build(), 5);
    setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 8), 15);
    setupRarity(new ItemStack(Material.GOLD_BLOCK, 8), 15);
    setupRarity(new ItemStack(Material.EMERALD_BLOCK, 4), 15);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 5);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 5);
    setupRarity(new ItemStack(Material.GLOWSTONE, 16), 10);
    setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 5).enchant(Enchantment.DURABILITY, 3).build(), 5);
    setupRarity(new ItemStack(Material.SULPHUR, 16), 10);
    setupRarity(new ItemBuilder(Material.SKULL_ITEM, 2).data((short)1).build(), 6);
    setupRarity(new ItemBuilder(Material.GOLDEN_APPLE, 16).build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 1);
  }
  
  public ChatColor getColour()
  {
    return ChatColor.YELLOW;
  }
  
  public boolean getBroadcastItems()
  {
    return false;
  }
}

