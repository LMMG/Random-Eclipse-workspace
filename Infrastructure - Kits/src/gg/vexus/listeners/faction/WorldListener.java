package gg.vexus.listeners.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.material.EnderChest;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import gg.mist.hcf.HCF;
import gg.vexus.Core;

public class WorldListener implements Listener
{

	public static final String DEFAULT_WORLD_NAME = "world";

	private final Core plugin;

	public WorldListener(Core plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			BlockState state = e.getClickedBlock().getState();
			if ((state instanceof Skull))
			{
				Skull s = (Skull) state;
				if (s.getSkullType() == SkullType.PLAYER)
				{
					p.sendMessage(ChatColor.YELLOW + "Head of " + ChatColor.WHITE + s.getOwner() + ChatColor.YELLOW + ".");
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		event.blockList().clear();
		if (event.getEntity() instanceof EnderDragon)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockChange(BlockFromToEvent event)
	{
		if (event.getBlock().getType() == Material.DRAGON_EGG)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityPortalEnter(EntityPortalEvent event)
	{
		if (event.getEntity() instanceof EnderDragon)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onWitherChangeBlock(EntityChangeBlockEvent event)
	{
		Entity entity = event.getEntity();
		if (entity instanceof Wither || entity instanceof EnderDragon)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockFade(BlockFadeEvent event)
	{
		switch (event.getBlock().getType())
		{
			case SNOW:
			case ICE:
				event.setCancelled(true);
				break;
			default:
				break;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(Bukkit.getWorld(DEFAULT_WORLD_NAME).getSpawnLocation().add(0.5, 0, 0.5));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerSpawn(PlayerSpawnLocationEvent event)
	{
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore())
		{
			HCF.getPlugin().getEconomyManager().addBalance(player.getUniqueId(), 300); // give
																				// player
																				// some
																				// starting
																				// money
			event.setSpawnLocation(Bukkit.getWorld(DEFAULT_WORLD_NAME).getSpawnLocation().add(0.5, 0, 0.5));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void death(PlayerDeathEvent event)
	{
		Player player = event.getEntity().getKiller();
		HCF.getPlugin().getEconomyManager().addBalance(player.getUniqueId(), 300);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		if (event.getInventory() instanceof EnderChest)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (event.getEntity() instanceof Squid)
		{
			event.setCancelled(true);
		}
	}
}