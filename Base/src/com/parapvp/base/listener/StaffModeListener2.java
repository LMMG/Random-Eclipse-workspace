package com.parapvp.base.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.module.StaffMode.StaffMode;

import net.md_5.bungee.api.ChatColor;

public class StaffModeListener2 implements Listener {

	private BasePlugin plugin;

	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("listener.staffmode") && (StaffMode.staffmode.contains(p))) {
			StaffMode.staffmode.remove(p);
		}
	}
	
	@EventHandler 
	public void onDead(PlayerDeathEvent e) { 
		Player p = e.getEntity();
		if(p.hasPermission("listener.staffmode") && (StaffMode.staffmode.contains(p))) {
			StaffMode.staffmode.remove(p);
			p.sendMessage(ChatColor.YELLOW + "Staff mode has been disabled because u died.");
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (StaffMode.staffmode.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (StaffMode.staffmode.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (StaffMode.staffmode.contains(p)) {
			p.sendMessage(ChatColor.RED + "You cannot place blocks in staff mode.");
			e.setCancelled(true);
		}

	}
	
	  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	  {
	    Entity entity = event.getEntity();
            Entity dam = event.getDamager();
	    if (!(entity instanceof Player))
	    {
	      return;
	    }
            if(!(dam instanceof Player)){
              return;
            }
            Player damager = (Player) dam;
            if(StaffMode.staffmode.contains(damager.getName())){
            	event.setCancelled(true);
          }
	}

	@EventHandler
	public void onBlockDestroy(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (StaffMode.staffmode.contains(p)) {
			p.sendMessage(ChatColor.RED + "You cannot destory blocks in staff mode.");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerHurt(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (StaffMode.staffmode.contains(p)) {
			p.sendMessage(ChatColor.RED + "You cannot damage entities while in staffmode.");
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerHurtNatural(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (StaffMode.staffmode.contains(p)) {
			e.setCancelled(true);
			return;
		}
	}

	// Items
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) && (e.getItem().getType() == Material.WATCH)) {
			if (StaffMode.staffmode.contains(p) && p.hasPermission("listener.staffmode")) {
				int randomIndex = new Random().nextInt(Bukkit.getServer().getOnlinePlayers().length);
				int currentpos = 0;
				for (Player a : Bukkit.getServer().getOnlinePlayers()) {
					if (currentpos++ == randomIndex) {
						if (a.equals(p)) {
							currentpos--;
							continue;
						}
						p.teleport(a);
						p.sendMessage(ChatColor.YELLOW + "You have been teleported to " + ChatColor.GOLD.toString()
								+ ChatColor.BOLD + a.getName());
					}
				}
			}
		}
	}

	@EventHandler
	public void onSpecialItemUse(PlayerInteractEvent event) {
		Player p = event.getPlayer();
			if (p.getItemInHand() == null) {
				return;
			}
			if ((event.getAction().equals(Action.LEFT_CLICK_AIR)) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))|| (event.getAction().equals(Action.RIGHT_CLICK_AIR))
					|| (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
				if (p.getItemInHand().getType().equals(Material.BOOK)) {
					p.sendMessage(ChatColor.RED + "Development");
				}
			}
		}
	
	@EventHandler
	public void onSpecialVaniosh(PlayerInteractEvent event) {
		Player p = event.getPlayer();
			if (p.getItemInHand() == null) {
				return;
			}
			if ((event.getAction().equals(Action.LEFT_CLICK_AIR)) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))|| (event.getAction().equals(Action.RIGHT_CLICK_AIR))
					|| (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
				if (p.getItemInHand().getType().equals(Material.NETHER_STAR)) {
					Bukkit.dispatchCommand(p, "vanish");
				}
			}
		}

	public static void setStaffInventory(Player p) {

		// Compass
		ItemStack item1 = new ItemStack(Material.COMPASS);
		ItemMeta meta1 = item1.getItemMeta();
		meta1.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Compass");
		item1.setItemMeta(meta1);

		// Random Teleport (Watch/Clock)
		ItemStack item2 = new ItemStack(Material.WATCH);
		ItemMeta meta2 = item2.getItemMeta();
		meta2.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Random Teleport");
		item2.setItemMeta(meta2);

		// Inventory Inspector (Book)
		ItemStack item3 = new ItemStack(Material.BOOK);
		ItemMeta meta3 = item2.getItemMeta();
		meta3.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Inventory Inspector");
		item3.setItemMeta(meta3);
		
		// Inventory Inspector (Book)
				ItemStack item4 = new ItemStack(Material.NETHER_STAR);
				ItemMeta meta4 = item4.getItemMeta();
				meta4.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Vanish Tool");
				item4.setItemMeta(meta4);

		p.getInventory().clear();
		p.getInventory().setItem(0, item1);
		p.getInventory().setItem(1, item2);
		p.getInventory().setItem(7, item4);
		p.getInventory().setItem(8, item3);
		p.updateInventory();
	}

	public static void resetPlayerInventory(Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.updateInventory();
	}
}