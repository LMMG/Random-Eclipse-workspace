package gg.vexus.listeners;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class KitMapSigns implements Listener
{
    ItemStack healthpot;
    ItemStack speedpot;
    ItemStack frespot;
    
    public KitMapSigns() {
        this.healthpot = new ItemStack(Material.POTION, 1, (short)16421);
        this.speedpot = new ItemStack(Material.POTION, 1, (short)8226);
        this.frespot = new ItemStack(Material.POTION, 1, (short)8259);
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST)) {
            final BlockState state = e.getClickedBlock().getState();
            if (state instanceof Sign) {
                final Sign s = (Sign)state;
                if (s.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[Kit]")) {
                    final String kit = s.getLine(1);
                    if (kit.contains("Diamond")) {
                        this.giveDiamondKit(p);
                        return;
                    }
                    if (kit.contains("Archer")) {
                        this.giveArcherKit(p);
                        return;
                    }
                    if (kit.contains("Bard")) {
                        this.giveBardKit(p);
                    }
                    if (kit.contains("Builder")) {
                        this.builderKit(p);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onSign(final SignChangeEvent e) {
        final Player p = e.getPlayer();
        if (p.hasPermission("common.createkitmapsign") && e.getLine(0).equalsIgnoreCase("[KitMap]")) {
            e.setLine(0, ChatColor.GOLD + "[Kit]");
            e.setLine(1, ChatColor.AQUA + e.getLine(1));
            p.sendMessage(ChatColor.GREEN + "Successfully created a kit sign!");
            p.sendMessage(ChatColor.GREEN + "Kit: " + e.getLine(1));
        }
    }
    
    public void giveDiamondKit(final Player p) {
        final ItemStack diamondhelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        diamondhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        diamondhelmet.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack diamondchestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        diamondchestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        diamondchestplate.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack diamondleggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        diamondleggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        diamondleggings.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack diamondboots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        diamondboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        diamondboots.addEnchantment(Enchantment.DURABILITY, 3);
        diamondboots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, this.healthpot);
        pi.setItem(3, this.healthpot);
        pi.setItem(4, this.healthpot);
        pi.setItem(5, this.healthpot);
        pi.setItem(6, this.healthpot);
        pi.setItem(7, this.speedpot);
        pi.setItem(8, new ItemStack(Material.BAKED_POTATO, 64));
        pi.setItem(9, this.healthpot);
        pi.setItem(10, this.healthpot);
        pi.setItem(11, this.healthpot);
        pi.setItem(12, this.healthpot);
        pi.setItem(13, this.healthpot);
        pi.setItem(14, this.healthpot);
        pi.setItem(15, this.healthpot);
        pi.setItem(16, this.frespot);
        pi.setItem(17, this.speedpot);
        pi.setItem(18, this.healthpot);
        pi.setItem(19, this.healthpot);
        pi.setItem(20, this.healthpot);
        pi.setItem(21, this.healthpot);
        pi.setItem(22, this.healthpot);
        pi.setItem(23, this.healthpot);
        pi.setItem(24, this.healthpot);
        pi.setItem(25, this.speedpot);
        pi.setItem(26, this.speedpot);
        pi.setItem(27, this.healthpot);
        pi.setItem(28, this.healthpot);
        pi.setItem(29, this.healthpot);
        pi.setItem(30, this.healthpot);
        pi.setItem(31, this.healthpot);
        pi.setItem(32, this.healthpot);
        pi.setItem(33, this.healthpot);
        pi.setItem(34, this.speedpot);
        pi.setItem(35, this.speedpot);
        pi.setBoots(diamondboots);
        pi.setLeggings(diamondleggings);
        pi.setChestplate(diamondchestplate);
        pi.setHelmet(diamondhelmet);
        p.updateInventory();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have &aequipped &ethe &bDiamond &ekit."));
    }
    
    public void giveBardKit(final Player p) {
        final ItemStack goldhelmet = new ItemStack(Material.GOLD_HELMET, 1);
        goldhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        goldhelmet.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack goldchestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        goldchestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        goldchestplate.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack goldleggings = new ItemStack(Material.GOLD_LEGGINGS, 1);
        goldleggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        goldleggings.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack goldboots = new ItemStack(Material.GOLD_BOOTS, 1);
        goldboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        goldboots.addEnchantment(Enchantment.DURABILITY, 3);
        goldboots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, new ItemStack(Material.SUGAR, 32));
        pi.setItem(3, new ItemStack(Material.BLAZE_POWDER, 32));
        pi.setItem(4, new ItemStack(Material.FEATHER, 32));
        pi.setItem(5, new ItemStack(Material.IRON_INGOT, 32));
        pi.setItem(6, new ItemStack(Material.GHAST_TEAR, 32));
        pi.setItem(7, this.healthpot);
        pi.setItem(8, new ItemStack(Material.BAKED_POTATO, 64));
        pi.setItem(9, this.healthpot);
        pi.setItem(10, this.healthpot);
        pi.setItem(11, this.healthpot);
        pi.setItem(12, this.healthpot);
        pi.setItem(13, this.healthpot);
        pi.setItem(14, this.healthpot);
        pi.setItem(15, this.healthpot);
        pi.setItem(16, this.frespot);
        pi.setItem(17, this.healthpot);
        pi.setItem(18, this.healthpot);
        pi.setItem(19, this.healthpot);
        pi.setItem(20, this.healthpot);
        pi.setItem(21, this.healthpot);
        pi.setItem(22, this.healthpot);
        pi.setItem(23, this.healthpot);
        pi.setItem(24, this.healthpot);
        pi.setItem(25, this.healthpot);
        pi.setItem(26, this.healthpot);
        pi.setItem(27, this.healthpot);
        pi.setItem(28, this.healthpot);
        pi.setItem(29, this.healthpot);
        pi.setItem(30, this.healthpot);
        pi.setItem(31, this.healthpot);
        pi.setItem(32, this.healthpot);
        pi.setItem(33, this.healthpot);
        pi.setItem(34, this.healthpot);
        pi.setItem(35, this.healthpot);
        pi.setBoots(goldboots);
        pi.setLeggings(goldleggings);
        pi.setChestplate(goldchestplate);
        pi.setHelmet(goldhelmet);
        p.updateInventory();
    }
    
    public void giveArcherKit(final Player p) {
        final ItemStack leatherhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
        leatherhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherhelmet.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack leatherchestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        leatherchestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherchestplate.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack leatherleggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        leatherleggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherleggings.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack leatherboots = new ItemStack(Material.LEATHER_BOOTS, 1);
        leatherboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherboots.addEnchantment(Enchantment.DURABILITY, 3);
        leatherboots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, bow);
        pi.setItem(3, new ItemStack(Material.SUGAR, 64));
        pi.setItem(4, this.healthpot);
        pi.setItem(5, this.healthpot);
        pi.setItem(6, this.healthpot);
        pi.setItem(7, this.healthpot);
        pi.setItem(8, new ItemStack(Material.BAKED_POTATO, 64));
        pi.setItem(9, new ItemStack(Material.ARROW, 1));
        pi.setItem(10, this.healthpot);
        pi.setItem(11, this.healthpot);
        pi.setItem(12, this.healthpot);
        pi.setItem(13, this.healthpot);
        pi.setItem(14, this.healthpot);
        pi.setItem(15, this.healthpot);
        pi.setItem(16, this.healthpot);
        pi.setItem(17, this.frespot);
        pi.setItem(18, this.healthpot);
        pi.setItem(19, this.healthpot);
        pi.setItem(20, this.healthpot);
        pi.setItem(21, this.healthpot);
        pi.setItem(22, this.healthpot);
        pi.setItem(23, this.healthpot);
        pi.setItem(24, this.healthpot);
        pi.setItem(25, this.healthpot);
        pi.setItem(26, this.healthpot);
        pi.setItem(27, this.healthpot);
        pi.setItem(28, this.healthpot);
        pi.setItem(29, this.healthpot);
        pi.setItem(30, this.healthpot);
        pi.setItem(31, this.healthpot);
        pi.setItem(32, this.healthpot);
        pi.setItem(33, this.healthpot);
        pi.setItem(34, this.healthpot);
        pi.setItem(35, this.healthpot);
        pi.setBoots(leatherboots);
        pi.setLeggings(leatherleggings);
        pi.setChestplate(leatherchestplate);
        pi.setHelmet(leatherhelmet);
        p.updateInventory();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have &aequipped &ethe &6Archer &ekit."));
    }
    
    public void builderKit(Player player) {
    	final ItemStack leatherhelmet = new ItemStack(Material.IRON_HELMET, 1);
        leatherhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherhelmet.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack leatherchestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        leatherchestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherchestplate.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack leatherleggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        leatherleggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherleggings.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack leatherboots = new ItemStack(Material.IRON_BOOTS, 1);
        leatherboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leatherboots.addEnchantment(Enchantment.DURABILITY, 3);
        leatherboots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        final ItemStack pic = new ItemStack(Material.DIAMOND_PICKAXE);
        pic.addEnchantment(Enchantment.DIG_SPEED, 5);
        final PlayerInventory pi = player.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setBoots(leatherboots);
        pi.setLeggings(leatherleggings);
        pi.setChestplate(leatherchestplate);
        pi.setHelmet(leatherhelmet);
        player.updateInventory();
    }
}
