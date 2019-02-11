package support.evocative.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import support.evocative.ComboFFA;

/**
 * Created by Owner on 01/09/2017.
 */
public class PlayerloginKitListener implements Listener {

    /**
     * TODO: Make the kit for death login, May be buggy working on it
     *
     * @param player
     */

    public void playerGiveKit(Player player) {
        player.getInventory().clear();

        ItemStack EnchantedGoldenApple = new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1);

        ItemStack dSword = new ItemStack(Material.DIAMOND_SWORD);
        dSword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        dSword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        dSword.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack ePearl = new ItemStack(Material.ENDER_PEARL, 16);

        ItemStack dHelm = new ItemStack(Material.DIAMOND_HELMET);
        dHelm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        dHelm.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack dChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        dChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        dChestplate.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack dLeggins = new ItemStack(Material.DIAMOND_LEGGINGS);
        dLeggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        dLeggins.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack dBoots = new ItemStack(Material.DIAMOND_BOOTS);
        dBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        dBoots.addEnchantment(Enchantment.DURABILITY, 3);

        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 64);

        player.getInventory().setHelmet(dHelm);
        player.getInventory().setChestplate(dChestplate);
        player.getInventory().setLeggings(dLeggins);
        player.getInventory().setBoots(dBoots);

        player.getInventory().setItem(0, dSword);
        player.getInventory().setItem(1, ePearl);
        player.getInventory().setItem(2, EnchantedGoldenApple);
        player.getInventory().setItem(8, steak);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.getInventory().clear();
        playerGiveKit(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999999, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
    }

    @EventHandler
    public void oPlayerDeath(PlayerDeathEvent e ){
        e.getDrops().clear();
    }

    @EventHandler
    public void PlayerDeath(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        player.getInventory().clear();
        playerGiveKit(player);
    }

    @EventHandler
    public void onSpawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        ComboFFA.getInstance().getServer().getScheduler().scheduleAsyncDelayedTask(ComboFFA.getInstance(), new Runnable() {
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999999, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
            }
        }, 20);
    }
}
