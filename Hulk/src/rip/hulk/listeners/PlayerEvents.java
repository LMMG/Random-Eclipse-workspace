package rip.hulk.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import rip.hulk.selector.ServerSelector;
import rip.hulk.utils.ItemStackBuilder;

public class PlayerEvents implements Listener
{

	static List<String> staffitemLore = new ArrayList<String>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();

		player.getInventory().clear();
		player.setWalkSpeed(0.5F);
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().setItem(4, ServerSelector.selector);

		if (player.hasPermission("core.staff"))
		{
			loadStaffItems(player);
		}
	}

	public void loadStaffItems(Player player)
	{
		ItemStack staffitem = ItemStackBuilder.get(Material.CHAINMAIL_LEGGINGS, 0, (short)0, ChatColor.translateAlternateColorCodes('&', "Staff Leggins"), staffitemLore);
		
		player.getInventory().setLeggings(staffitem);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event)
	{
		if ((event.getEntity() instanceof Player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (!event.getPlayer().isOp())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (!event.getPlayer().isOp())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void bucketFill(PlayerBucketEmptyEvent event)
	{
		if (!event.getPlayer().isOp())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void bucketEmpty(PlayerBucketFillEvent event)
	{
		if (!event.getPlayer().isOp())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPickItem(PlayerPickupItemEvent e)
	{
		e.setCancelled(true);
		e.getItem().remove();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		e.setQuitMessage(null);
	}

	@EventHandler
	public void onJoindsa(PlayerJoinEvent e)
	{
		e.setJoinMessage(null);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e)
	{
		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e)
	{
		e.setCancelled(true);
	}

	@EventHandler
	public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if ((event.getDamager() instanceof Player))
		{
			Player damager = (Player) event.getDamager();
			if ((event.getEntity() instanceof Player))
			{
				Player damagee = (Player) event.getEntity();
				damager.hidePlayer(damagee);
				damager.sendMessage(ChatColor.GREEN + "Pop...");
			}
		}
	}
}
