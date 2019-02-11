package net.nersa.kitmap.listener.fixes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DupeListener implements Listener {

	@EventHandler
	public void playerPlaceItem(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getBlock().getType().equals(Material.CHEST)) {
			return;
		}
		if (event.getItemInHand().hasItemMeta()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You aren't allowed to place renamed chests!");
			return;
		}
	}

	@EventHandler
	public void playerPlaceItem1(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getBlock().getType().equals(Material.TRAPPED_CHEST)) {
			return;
		}
		if (event.getItemInHand().hasItemMeta()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You aren't allowed to place renamed chests!");
			return;
		}
	}

	@EventHandler
	public void playerPlaceItem2(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getBlock().getType().equals(Material.HOPPER)) {
			return;
		}
		if (event.getItemInHand().hasItemMeta()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You aren't allowed to place renamed hopper!");
			return;
		}
	}

	@EventHandler
	public void playerPlaceItem3(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getBlock().getType().equals(Material.HOPPER_MINECART)) {
			return;
		}
		if (event.getItemInHand().hasItemMeta()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You aren't allowed to place renamed hoppers!");
			return;
		}
	}

	@EventHandler
	public void playerPlaceItem4(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getBlock().getType().equals(Material.STORAGE_MINECART)) {
			return;
		}
		if (event.getItemInHand().hasItemMeta()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You aren't allowed to place renamed chests!");
			return;
		}
	}
	
	@EventHandler
	public void playerPlaceItem5(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getBlock().getType().equals(Material.TRIPWIRE_HOOK)) {
			return;
		}
		if (event.getItemInHand().hasItemMeta()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You aren't allowed to place renamed tripwire hooks!");
			return;
		}
	}

	@EventHandler
	public void onRename(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.CHEST)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a chest!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameChestHopper(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.HOPPER)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a hopper!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameChestTraped(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.TRAPPED_CHEST)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a chest!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameChestDropper(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.DROPPER)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a dropper!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameChestDis(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.DISPENSER)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a dispenser!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameChestHopper1(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.HOPPER_MINECART)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a hopper!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameChestChest1(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.STORAGE_MINECART)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a chest!");
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onRenameTripwireHook(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						if (!item.getType().equals(Material.TRIPWIRE_HOOK)) {
							return;
						}
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename a tripwire hook!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onRenameAllItems(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		Inventory inv = event.getInventory();
		if ((inv instanceof AnvilInventory)) {
			InventoryView view = event.getView();
			int rawSlot = event.getRawSlot();
			if (rawSlot == view.convertSlot(rawSlot)) {
				if (rawSlot == 2) {
					ItemStack item = event.getCurrentItem();
					if (item != null) {
						ItemMeta meta = item.getItemMeta();
						if (meta != null) {
							if (meta.hasDisplayName()) {
								String displayName = meta.getDisplayName();
								if (!displayName.toLowerCase().contains("reward")) {
									return;
								}
								event.setCancelled(true);
								player.sendMessage(ChatColor.RED + "You can't rename an item with the word reward!");
							}
						}
					}
				}
			}
		}
	}
}
