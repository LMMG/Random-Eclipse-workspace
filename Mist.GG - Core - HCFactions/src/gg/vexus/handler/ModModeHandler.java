package gg.vexus.handler;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import gg.vexus.commands.ModModeCommand;


public class ModModeHandler implements Listener {
	private static Random random = new Random();

	@EventHandler
	public void join(PlayerJoinEvent e) {
	}

	@EventHandler
	public void noModFight(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		if (ModModeCommand.modmode.contains(((Player) e.getDamager()).getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void figh(EntityDamageEvent e) {
		if ((e.getEntity() instanceof Player)) {
			if (ModModeCommand.modmode.contains(((Player) e.getEntity()).getName())) {
				e.setCancelled(true);
			}
		} else {
		}
	}

	int clickcount = 0;

	@EventHandler
	public void invTake(InventoryClickEvent e) {
		if (e.getInventory() == null) {
			return;
		}
		if (e.getWhoClicked() == null) {
			return;
		}
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		if (ModModeCommand.modmode.contains(e.getWhoClicked().getName())) {
			e.setCancelled(true);
			if ((e.getCurrentItem() != null) && (e.getCurrentItem().hasItemMeta())
					&& (e.getCurrentItem().getItemMeta() != null)
					&& (e.getInventory().getTitle().contains("§cInventory of"))) {
				Player target = Bukkit.getPlayer(e.getInventory().getTitle().substring("§cInventory of §e".length()));
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Permanent Ban")) {
					if (ModModeCommand.modmode.contains(target.getName())) {
						((Player) e.getWhoClicked())
								.sendMessage("§cThat person is in mod mode the action will not be performed.");
						return;
					}
					((Player) e.getWhoClicked())
							.performCommand("ban " + target.getName() + " Cheating or Breaking our Terms of Service.");
					((Player) e.getWhoClicked())
							.sendMessage("§cPermanently banned player §e" + target.getName() + "§c.");
					return;
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§dPlayer admits Xray")) {
					if (ModModeCommand.modmode.contains(target.getName())) {
						((Player) e.getWhoClicked())
								.sendMessage("§cThat person is in mod mode the action will not be performed.");
						return;
					}
					((Player) e.getWhoClicked()).performCommand("tban" + target.getName() + " 7d Xray (Admitted)");
					((Player) e.getWhoClicked()).sendMessage(
							"§cTemporarily banned player §e" + target.getName() + "§c for 7d §eXray (admitted)§c.");
					return;
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cPlayer denies Xray")) {
					if (ModModeCommand.modmode.contains(target.getName())) {
						((Player) e.getWhoClicked())
								.sendMessage("§cThat person is in mod mode the action will not be performed.");
						return;
					}
					((Player) e.getWhoClicked()).performCommand("tban" + target.getName() + " 14d Xray");
					((Player) e.getWhoClicked())
							.sendMessage("§cTemporarily banned player §e" + target.getName() + "§c for 14d §eXray§c.");
					return;
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eClear inventory")) {
					this.clickcount += 1;
					if (this.clickcount == 1) {
						((Player) e.getWhoClicked()).sendMessage("§cClick on the item two more times to confirm.");
					}
					if (this.clickcount == 2) {
						((Player) e.getWhoClicked()).sendMessage("§cClick on the item one more times to confirm.");
					}
					if (this.clickcount >= 3) {
						if (ModModeCommand.modmode.contains(target.getName())) {
							((Player) e.getWhoClicked())
									.sendMessage("§cThat person is in mod mode the action will not be performed.");
							return;
						}
						target.getInventory().clear();
						((Player) e.getWhoClicked()).sendMessage("§cCleared §e" + target.getName() + "§c's inventory.");
						this.clickcount = 0;
						return;
					}
				}
			} else {
			}
		}
	}

	@EventHandler
	public void pickup(PlayerPickupItemEvent e) {
		if (e.getPlayer() == null) {
			return;
		}
		if (ModModeCommand.modmode.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		} else {
		}
	}

	@EventHandler
	public void pickup(PlayerDropItemEvent e) {
		if (e.getPlayer() == null) {
			return;
		}
		if (ModModeCommand.modmode.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		} else {
		}
	}

	@EventHandler
	public void placeBl(BlockPlaceEvent e) {
		if (e.getPlayer() == null) {
			return;
		}
		if (e.getBlock() == null) {
			return;
		}
		if (ModModeCommand.modmode.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		} else {
		}
	}

	@EventHandler
	public void breakBl(BlockBreakEvent e) {
		if (e.getPlayer() == null) {
			return;
		}
		if (e.getBlock() == null) {
			return;
		}
		if (ModModeCommand.modmode.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		} else {
		}
	}

	public int reverseNumber(int num, int min, int max) {
		return max + min - num;
	}

	@EventHandler
	public void rightClick(PlayerInteractEntityEvent e) {
		Inventory i = null;
		if (!(e.getRightClicked() instanceof Player)) {
			return;
		}
		Player player = e.getPlayer();
		Player target = (Player) e.getRightClicked();
		if (target.getInventory() == null) {
			return;
		}
		if ((ModModeCommand.modmode.contains(player.getName())) && ((target instanceof Player))
				&& ((player instanceof Player)) && (player.getItemInHand().getType() == Material.BOOK)
				&& (player.getItemInHand().hasItemMeta())) {
			if (i != null) {
				i.clear();
			}
			i = Bukkit.createInventory(target, 54, "§cInventory of §e" + target.getName());
			if (target.getInventory().getHelmet() != null) {
				i.setItem(0, target.getInventory().getHelmet());
			}
			if (target.getInventory().getChestplate() != null) {
				i.setItem(1, target.getInventory().getChestplate());
			}
			if (target.getInventory().getLeggings() != null) {
				i.setItem(2, target.getInventory().getLeggings());
			}
			if (target.getInventory().getBoots() != null) {
				i.setItem(3, target.getInventory().getBoots());
			}
			if (target.getItemInHand() != null) {
				i.setItem(4, target.getItemInHand());
			}
			int a = 0;
			for (int ix = 9; ix < target.getInventory().getSize() + 9; ix++) {
				i.setItem(ix, target.getInventory().getItem(ix - 9));
				a = ix;
			}
			ItemStack ist = new ItemStack(Material.BLAZE_POWDER);
			ItemMeta imt = ist.getItemMeta();
			imt.setDisplayName("§eClear inventory");
			ist.setItemMeta(imt);
			i.setItem(a + 1, ist);

			ItemStack is1 = new ItemStack(351, 1, (short) 14);
			ItemMeta im1 = is1.getItemMeta();
			im1.setDisplayName("§6Permanent Ban");
			is1.setItemMeta(im1);
			i.setItem(a + 9, is1);

			ItemStack is2 = new ItemStack(351, 1, (short) 13);
			ItemMeta im2 = is2.getItemMeta();
			im2.setDisplayName("§dPlayer admits to Xray");
			is2.setItemMeta(im2);
			i.setItem(a + 8, is2);

			ItemStack is3 = new ItemStack(351, 1, (short) 7);
			ItemMeta im3 = is3.getItemMeta();
			im3.setDisplayName("§cPlayer denies to Xray");
			is3.setItemMeta(im3);
			i.setItem(a + 7, is3);

			player.openInventory(i);
			player.sendMessage("§7Opening the inventory of §a" + target.getName() + "§7...");
			return;
		}
	}

	@EventHandler
	public void items(PlayerInteractEvent e) {
		if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (e.getClickedBlock() == null)) {
			return;
		}
		if ((e.getAction().equals(Action.LEFT_CLICK_BLOCK)) && (e.getClickedBlock() == null)) {
			return;
		}
		if (e.getPlayer().getInventory().getItemInHand() == null) {
			return;
		}
		if ((ModModeCommand.modmode.contains(e.getPlayer().getName())) && (e.getPlayer().getItemInHand() != null)
				&& (e.getPlayer().getItemInHand().getItemMeta() != null)
				&& (e.getPlayer().getItemInHand().hasItemMeta())) {
			if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
				if (e.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName()
						.contains("§aToggle vanish")) {
					VanishHandler.toggleVanish(e.getPlayer());
					return;
				}
				if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("§cRandom TP")) {
					players.clear();
					Player[] arrayOfPlayer;
					int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
					for (int i = 0; i < j; i++) {
						Player p = arrayOfPlayer[i];
						if ((!p.hasPermission("common.mod")) && (!p.hasPermission("common.mod")) && (!p.isOp())
								&& (!p.equals(e.getPlayer()))) {
							players.add(p);
						}
					}
					if (players.size() == 0) {
						e.getPlayer().sendMessage("§cThere aren't any players to teleport to.");
						return;
					}
					int player = random.nextInt(players.size());
					e.getPlayer().teleport((Entity) players.get(player));
					e.getPlayer().sendMessage(
							"§cRandomly teleported to §e" + ((Player) players.get(player)).getName() + "§c.");
					return;
				}
				if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("§cOnline staff")) {
					e.getPlayer().openInventory(setupStaffInv());
					return;
				}
				return;
			}
			return;
		}
	}

	private Inventory setupStaffInv() {
		int staffcount = 0;
		Player[] arrayOfPlayer1;
		int j = (arrayOfPlayer1 = Bukkit.getOnlinePlayers()).length;
		for (int i = 0; i < j; i++) {
			Player x = arrayOfPlayer1[i];
			if ((x.isOp()) || (x.hasPermission("common.mod"))) {
				staffcount++;
			}
		}
		Inventory i = null;
		if (staffcount > 27) {
			i = Bukkit.createInventory(null, 54, "§cStaff online");
		} else {
			i = Bukkit.createInventory(null, 27, "§cStaff online");
		}
		int cout = 0;
		Player[] arrayOfPlayer2;
		int m = (arrayOfPlayer2 = Bukkit.getOnlinePlayers()).length;
		for (int k = 0; k < m; k++) {
			Player x = arrayOfPlayer2[k];
			if ((x.isOp()) || (x.hasPermission("common.mod"))) {
				ItemStack staff = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
				SkullMeta staffm = (SkullMeta) staff.getItemMeta();
				staffm.setOwner(x.getName());
				staffm.setDisplayName("§c" + x.getName());
				staff.setItemMeta(staffm);
				cout++;
				if (cout > 54) {
					return i;
				}
				i.setItem(cout - 1, staff);
			}
		}
		return i;
	}

	private static ArrayList<Player> players = new ArrayList();
	private static ItemStack vanish = new ItemStack(Material.FEATHER);
	private static ItemMeta vanishm = vanish.getItemMeta();

	public static void put(Player p) {
		vanishm.setDisplayName("§aToggle vanish");
		vanish.setItemMeta(vanishm);

		p.getInventory().clear();
		if ((!p.getGameMode().equals(GameMode.CREATIVE)) && (p.hasPermission("common.mod"))) {
			p.setGameMode(GameMode.CREATIVE);
		}
		ItemStack randomTp = new ItemStack(351, 1, (short) 12);
		ItemMeta randomTpMeta = randomTp.getItemMeta();
		randomTpMeta.setDisplayName("§cRandom TP");
		randomTp.setItemMeta(randomTpMeta);

		p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));

		ItemStack invinspect = new ItemStack(340);
		ItemMeta invinspectm = invinspect.getItemMeta();
		invinspectm.setDisplayName("§cInventory Inspect");
		invinspect.setItemMeta(invinspectm);

		ItemStack toolcompass = new ItemStack(Material.COMPASS);
		ItemMeta toolcompassm = toolcompass.getItemMeta();
		toolcompassm.setDisplayName("§cTeleport Compass");
		toolcompass.setItemMeta(toolcompassm);

		ItemStack staff = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta staffrm = (SkullMeta) staff.getItemMeta();
		staffrm.setOwner(p.getName());
		staffrm.setDisplayName("§cOnline staff");
		staff.setItemMeta(staffrm);

		p.getInventory().setItem(0, randomTp);
		p.getInventory().setItem(1, invinspect);
		p.getInventory().setItem(6, staff);
		p.getInventory().setItem(7, toolcompass);
		p.getInventory().setItem(8, vanish);
	}

	public static void remove(Player p) {
		if (!p.getGameMode().equals(GameMode.SURVIVAL)) {
			p.setGameMode(GameMode.SURVIVAL);
		}
		p.getInventory().clear();

		p.getInventory().setLeggings(null);
	}
}
