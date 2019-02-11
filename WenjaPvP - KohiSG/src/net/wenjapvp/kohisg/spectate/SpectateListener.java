package net.wenjapvp.kohisg.spectate;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.wenjapvp.kohisg.KohiSG;

public class SpectateListener implements Listener
{
	private final SpectateManager spectateManager = KohiSG.getInstance().getSpectateManager();

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(InventoryClickEvent event)
	{
		if (spectateManager.isSpectating((Player) event.getWhoClicked()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (spectateManager.isSpectating(player))
		{
			// if (event.hasItem())
			// {
			// String title = ItemStackUtils.getTitle(event.getItem());
			// if (title.equals(ItemConstant.NEXT_RANDOM))
			// {
			// List<Player> players = new
			// ArrayList<>(Collections2.filter((Collection)new
			// ArrayList(Bukkit.getOnlinePlayers()), new Predicate<Player>()
			// {
			// @Override
			// public boolean apply(Player player)
			// {
			// return !spectateManager.isSpectating(player);
			// }
			// }));
			// if (!players.isEmpty())
			// {
			// Player rand = (Player)players.get(new
			// Random().nextInt(players.size()));
			// event.getPlayer().teleport(rand);
			// }
			// }
			// }
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDropItem(PlayerPickupItemEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player && spectateManager.isSpectating((Player) event.getEntity()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerEditBookEvent(PlayerEditBookEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	private Player getPlayer(Entity entity)
	{
		if (entity instanceof Player) { return (Player) entity; }
		if (entity instanceof Projectile)
		{
			Projectile projectile = (Projectile) entity;
			if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) { return (Player) projectile.getShooter(); }
		}
		return null;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		Player damager = getPlayer(event.getDamager());
		Player damagee = getPlayer(event.getEntity());
		if (damager != null && spectateManager.isSpectating(damager))
		{
			event.setCancelled(true);
		}
		if (damagee != null && spectateManager.isSpectating(damagee))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			spectateManager.unSpectate(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerKick(PlayerKickEvent event)
	{
		if (spectateManager.isSpectating(event.getPlayer()))
		{
			spectateManager.unSpectate(event.getPlayer());
		}
	}
}
