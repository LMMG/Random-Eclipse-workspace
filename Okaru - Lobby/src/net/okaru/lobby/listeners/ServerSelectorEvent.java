package net.okaru.lobby.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.okaru.lobby.Lobby;
import net.okaru.lobby.server.Server;
import net.okaru.lobby.utils.ColorUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerSelectorEvent implements Listener {
	private final Lobby plugin = Lobby.getInstance();
	
    public static ItemStack getSelector() {
        ItemStack compass = new ItemStack(Material.WATCH, 1);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Server Selector"));
        compass.setItemMeta(compassMeta);
        return compass;
    }
	
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            Player player = (Player)event.getWhoClicked();
            if (event.getCurrentItem().isSimilar(this.getSelector())) {
                event.setCancelled(true);
            }
            if (event.getClickedInventory().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "&cSelect your desired gamemode"))) {
                event.setCancelled(true);
                if (event.getRawSlot() == 4) {
                    player.sendMessage(ChatColor.WHITE + "We are now attempting to connect you to " + ChatColor.GOLD + "Kits" + ChatColor.WHITE + ".");
                    event.getWhoClicked().closeInventory();
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try
                    {
                    	out.writeUTF("Connect");
                    	out.writeUTF("Kits");
                    }
                    catch (IOException error)
                    {
                    error.printStackTrace();	
                    }
                    player.sendPluginMessage(Lobby.getInstance(), "BungeeCord", b.toByteArray());
                    }
                 }
            }
      }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack;
        Player player;
        Action action = event.getAction();
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && (itemStack = (player = event.getPlayer()).getItemInHand()) != null && itemStack.getType() == Material.WATCH) {
            this.openServerSelectorMenu(player);
        }
    }
    
    public static void openServerSelectorMenu(Player player) {
        Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)player, 9, new ColorUtils().translateFromString("&cSelect your desired gamemode"));
        //ItemStack fishingRod = new ItemStack(Material.FISHING_ROD, 1);
        //ItemMeta fishingRodMeta = fishingRod.getItemMeta();
        //fishingRodMeta.setDisplayName(new ColorUtils().translateFromString("&cHardcore Factions"));
        //if (Server.getByName("Factions").isOffline()) {
        //    fishingRodMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&cHardcore Factions is currently offline.")));
        //} else {
        //    fishingRodMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&fPlayers&7: &c" + Server.getByName("Factions").getOnlinePlayers() + "/" + Server.getByName("Factions").getMaxPlayers() + "", " ", "&eClick to join the server.")));
        //}
        //fishingRod.setItemMeta(fishingRodMeta);
        //ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD, 1);
        //ItemMeta diamondSwordMeta = diamondSword.getItemMeta();
        //diamondSwordMeta.setDisplayName(new ColorUtils().translateFromString("&cPractice"));
        //if (Server.getByName("Practice").isOffline()) {
        //    diamondSwordMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&ePractice is currently offline.")));
        //} else {
        //    diamondSwordMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&fPlayers&7: &c" + Server.getByName("Practice").getOnlinePlayers() + "/" + Server.getByName("Practice").getMaxPlayers() + "", " ", "&eClick to join the server.")));
        //}
        //diamondSword.setItemMeta(diamondSwordMeta);
        ItemStack healthPotion = new ItemStack(Material.POTION, 1, (short) 16421);
        ItemMeta healthPotionMeta = healthPotion.getItemMeta();
        healthPotionMeta.setDisplayName(new ColorUtils().translateFromString("&cKits"));
        if (Server.getByName("Kits").isOffline()) {
            healthPotionMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&eKits is currently offline.")));
        } else {
            //ealthPotionMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&fPlayers&7: &c" + Server.getByName("Kits").getOnlinePlayers() + "/" + Server.getByName("Kits").getMaxPlayers() + "", " ", "&eClick to join the server.")));
        	  healthPotionMeta.setLore(new ColorUtils().translateFromArray(Arrays.asList("&7&m------------------------------------------", "&6Description&7:", "&fKits is a variant of Hardcore Factions of which", "&fthe map never ends, is in a state of constant PVP", "&fand has many fast paced Events.", "", "&6Online&7:&f " + Server.getByName("Kits").getOnlinePlayers() + "/" + Server.getByName("Kits").getMaxPlayers(), "", "&7&oThis server requires a 1.7.* client to play!", "&7&m------------------------------------------")));
        }
        healthPotion.setItemMeta(healthPotionMeta);
        //inventory.setItem(2, fishingRod);
        //inventory.setItem(4, diamondSword);
        inventory.setItem(4, healthPotion);
        player.openInventory(inventory);
    }
}