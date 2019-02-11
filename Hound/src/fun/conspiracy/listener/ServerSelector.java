package fun.conspiracy.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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

import fun.conspiracy.Hound;
import fun.conspiracy.utils.ItemUtil;
import net.md_5.bungee.api.ChatColor;

public class ServerSelector implements Listener {

	static List<String> serverSelectorLore = new ArrayList<String>();

	static {
	}

	public static Inventory inv = Bukkit.createInventory(null, 27,
			ChatColor.translateAlternateColorCodes('&', Hound.lang.getString("SELECTOR.GUI.NAME")));
	public static ItemStack selector = ItemUtil.get(Material.COMPASS, 1, (short) 0,
			ChatColor.translateAlternateColorCodes('&', Hound.config.getString("ITEM.SERVERSELECTOR")), null);
	public static ItemStack glass1 = ItemUtil.get(Material.STAINED_GLASS_PANE, 1, (short) 0, "", null);
	public static ItemStack uhc = ItemUtil.get(Material.FISHING_ROD, 1, (short) 0, "&cUHC", null);
	public static ItemStack uhcf = ItemUtil.get(Material.DIAMOND_HELMET, 1, (short) 0, "&cUHCF", null);
	public static ItemStack uhcfkitmap = ItemUtil.get(Material.DIAMOND_SWORD, 1, (short) 0, "&cUHCF KitMap", null);

	public ServerSelector() {
		inv.setItem(0, glass1);
		inv.setItem(1, glass1);
		inv.setItem(2, glass1);
		inv.setItem(3, glass1);
		inv.setItem(4, glass1);
		inv.setItem(5, glass1);
		inv.setItem(6, glass1);
		inv.setItem(7, glass1);
		inv.setItem(8, glass1);
		inv.setItem(7, glass1);
		inv.setItem(9, glass1);
		inv.setItem(10, glass1);
		inv.setItem(12, glass1);
		inv.setItem(14, glass1);
		inv.setItem(16, glass1);
		inv.setItem(17, glass1);
		inv.setItem(18, glass1);
		inv.setItem(19, glass1);
		inv.setItem(20, glass1);
		inv.setItem(21, glass1);
		inv.setItem(22, glass1);
		inv.setItem(23, glass1);
		inv.setItem(24, glass1);
		inv.setItem(25, glass1);
		inv.setItem(26, glass1);

		inv.setItem(13, uhc);
		inv.setItem(11, uhcf);
		inv.setItem(15, uhcfkitmap);
	}

	@EventHandler
	public void InventoryClick(InventoryClickEvent e) {
		e.setCancelled(true);
		if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) {
			return;
		}
		if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
			e.setCancelled(false);
		}
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if (item.isSimilar(uhc)) {
			Bukkit.dispatchCommand(p, "play HCFactions");
			p.closeInventory();
		} else if (item.isSimilar(uhcf)) {
			Bukkit.dispatchCommand(p, "play kitmap");
			p.closeInventory();
		} else if (item.isSimilar(uhcfkitmap)) {
			Bukkit.dispatchCommand(p, "play blitz");
			p.closeInventory();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(selector))) {
			Player p = e.getPlayer();
			p.openInventory(inv);
			e.setCancelled(true);
		}
	}
}
