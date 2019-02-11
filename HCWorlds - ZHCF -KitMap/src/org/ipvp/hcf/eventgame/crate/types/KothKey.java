package org.ipvp.hcf.eventgame.crate.types;

import com.doctordark.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.ipvp.hcf.ConfigurationService;
import org.ipvp.hcf.eventgame.crate.EnderChestKey;
import org.ipvp.hcf.listener.Crowbar;

public class KothKey extends EnderChestKey
{
    public KothKey() {
        super("Koth", 6);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.FIRE_ASPECT, 1).enchant(Enchantment.DAMAGE_ALL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL)).displayName(ChatColor.RED + "KoTH Fire").build(), 3);
        this.setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 16), 15);
        this.setupRarity(new ItemStack(Material.GOLD_BLOCK, 16), 15);
        this.setupRarity(new ItemStack(Material.IRON_BLOCK, 16), 15);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KoTH Helmet").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KoTH Chestplate").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KoTH Leggings").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KoTH Boots").build(), 1);
        this.setupRarity(new ItemBuilder(Material.GOLD_HELMET).enchant(Enchantment.DURABILITY, 4).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "Bard Helmet").build(), 1);
        this.setupRarity(new ItemBuilder(Material.GOLD_CHESTPLATE).enchant(Enchantment.DURABILITY, 4).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "Bard Chestplate").build(), 1);
        this.setupRarity(new ItemBuilder(Material.GOLD_LEGGINGS).enchant(Enchantment.DURABILITY, 4).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "Bard Leggings").build(), 1);
        this.setupRarity(new ItemBuilder(Material.GOLD_BOOTS).enchant(Enchantment.DURABILITY, 4).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "Bard Boots").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.LOOT_BONUS_MOBS, 5).displayName(ChatColor.RED + "KOTH Looting").build(), 7);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.LOOT_BONUS_BLOCKS, 4).displayName(ChatColor.RED + "KOTH Fortune").build(), 5);
        this.setupRarity(new ItemBuilder(Material.SKULL_ITEM, 2).data((short)1).build(), 6);
        this.setupRarity(new ItemStack(Material.BEACON), 2);
        this.setupRarity(new ItemStack(Material.NETHER_STAR), 3);
        this.setupRarity(new Crowbar().getItemIfPresent(), 1);
        this.setupRarity(new ItemBuilder(Material.GOLDEN_APPLE).data((short)1).build(), 3);
        this.setupRarity(new ItemBuilder(Material.GOLDEN_APPLE, 3).data((short)1).build(), 3);
        this.setupRarity(new ItemBuilder(Material.GOLDEN_APPLE, 5).data((short)1).build(), 1);
        this.setupRarity(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.ARROW_DAMAGE)).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.ARROW_INFINITE, 1).displayName(ChatColor.RED + "KOTH Bow").build(), 3);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KOTH Helmet").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KOTH Chestplate").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KOTH Leggings").build(), 1);
        this.setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, (int) ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).displayName(ChatColor.RED + "KOTH Boots").build(), 1);
    }
    
    @Override
    public ChatColor getColour() {
        return ChatColor.YELLOW;
    }
    
    @Override
    public boolean getBroadcastItems() {
        return true;
    }
}
