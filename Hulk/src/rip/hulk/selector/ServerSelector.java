package rip.hulk.selector;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import pw.deathstreams.scythe.sync.property.SynchronizableProperty;
import rip.hulk.Hulk;
import rip.hulk.utils.ItemStackBuilder;

public class ServerSelector implements Listener
{
	static List<String> serverSelectorLore = new ArrayList<>();
	static List<String> hardcoreFactionsLore = new ArrayList<>();
	static List<String> developmentLore = new ArrayList<>();
	static List<String> kitsLore = new ArrayList<>();
	static List<String> practiceLore = new ArrayList<>();
	static List<String> kohisgLore = new ArrayList<>();
	static List<String> none = new ArrayList<>();
	@SuppressWarnings({
			"deprecation",
			"unused"
	}) private static ItemStack somethhing = new ItemStack(351, 1, (short) 8);
	public static int kits = 0;
	public static int pra1c = 0;
	public static int hcf1 = 0;

	static
	{
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Players Online:&a"));
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Online Status:&a"));
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', " "));
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&aDescription -"));
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Fun Hardcore gamemode that contains DTR"));
		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Players Online:&a"));
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Online Status:&a"));
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', " "));
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&aDescription -"));
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Fun Hardcore gamemode that contains Bard,Archer, Diamond"));
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Players Online:&a"));
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Online Status:&a"));
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', " "));
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&aDescription -"));
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&a * &7Fun Hardcore gamemode that contains elo and team fights"));
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
	}

	public static ItemStack selector = ItemStackBuilder.get(Material.WATCH, 1, (short) 0, "&aServer Selector", null);
	public static ItemStack prac = ItemStackBuilder.get(Material.FISHING_ROD, 1, (short) 0, "&aPractice &7(Season I)", practiceLore);
	public static ItemStack kitmap = ItemStackBuilder.get(Material.DIAMOND_SWORD, 1, (short) 0, "&aKits &7(Season I)", kitsLore);
	public static ItemStack hcf = ItemStackBuilder.get(Material.BOW, 1, (short) 0, "&aHCF &7(Map I)", hardcoreFactionsLore);
	public static Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&aServer Selector"));

	public ServerSelector()
	{
		inv.setItem(10, prac);
		inv.setItem(13, hcf);
		inv.setItem(16, kitmap);
	}

	@EventHandler
	public void InventoryClick(InventoryClickEvent e)
	{
		e.setCancelled(true);
		if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) { return; }
		if (e.getInventory().getType().equals(InventoryType.PLAYER))
		{
			e.setCancelled(false);
		}
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if (item.isSimilar(prac))
		{
			p.sendMessage(ChatColor.GREEN + "Your are being send to Practice");
			p.closeInventory();
		}
		else if (item.isSimilar(kitmap))
		{
			Bukkit.dispatchCommand(p, "play kits");
			p.sendMessage(ChatColor.GREEN + "Your are being send to Kits");
			p.closeInventory();
		}
		else if (item.isSimilar(hcf))
		{
			Bukkit.dispatchCommand(p, "play hcf");
			p.sendMessage(ChatColor.GREEN + "Your are being send to HCF");
			p.closeInventory();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(selector)))
		{
			Player p = e.getPlayer();
			p.openInventory(inv);
			e.setCancelled(true);
		}
	}
}
