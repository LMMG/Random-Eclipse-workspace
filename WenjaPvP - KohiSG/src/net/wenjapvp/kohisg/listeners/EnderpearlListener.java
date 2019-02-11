package net.wenjapvp.kohisg.listeners;

import net.wenjapvp.kohisg.utils.TimeUtils;
import net.wenjapvp.kohisg.utils.Color;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EnderpearlListener implements Listener
{
	private final HashMap<Player, Long> enderpearlCooldown = new HashMap<>();

	public void setCooldown(Player player, long value)
	{
		enderpearlCooldown.put(player, System.currentTimeMillis() + value);
	}

	public void removeCooldown(Player player)
	{
		enderpearlCooldown.remove(player);
	}

	public boolean isCooldownActive(Player player)
	{
		if (!enderpearlCooldown.containsKey(player)) { return false; }
		return enderpearlCooldown.get(player) > System.currentTimeMillis();
	}

	public long getMillisecondLeft(Player player)
	{
		if (!isCooldownActive(player)) { return -1; }
		return enderpearlCooldown.get(player) - System.currentTimeMillis();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		removeCooldown(player);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player player = event.getPlayer();
		removeCooldown(player);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity().getPlayer();
		removeCooldown(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onProjectileLaunch(ProjectileLaunchEvent event)
	{
		Projectile projectile = event.getEntity();
		if (projectile instanceof EnderPearl)
		{
			EnderPearl enderPearl = (EnderPearl) projectile;
			ProjectileSource projectileSource = enderPearl.getShooter();
			if (projectileSource instanceof Player)
			{
				Player player = (Player) projectileSource;
				if (isCooldownActive(player))
				{
					event.setCancelled(true);
					return;
				}
				setCooldown(player, TimeUtils.parse("15s"));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			Material material = event.getMaterial();
			Player player = event.getPlayer();
			if (material == Material.ENDER_PEARL && player.getGameMode() != GameMode.CREATIVE)
			{
				if (isCooldownActive(player))
				{
					event.setUseItemInHand(Event.Result.DENY);
					player.sendMessage(Color.translate("&cYou still have a &e&lEnderpearl &ccooldown for another " + TimeUtils.LongCountdown.setFormat(getMillisecondLeft(player)) + "."));
				}
			}
		}
	}
}