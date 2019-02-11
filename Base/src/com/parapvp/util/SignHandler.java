package com.parapvp.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SignHandler implements Listener
{
	private final Multimap<UUID, SignChange> signUpdateMap;
	private final JavaPlugin plugin;

	public SignHandler(JavaPlugin plugin)
	{
		this.signUpdateMap = HashMultimap.create();
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerQuitEvent event)
	{
		cancelTasks(event.getPlayer(), null, false);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		cancelTasks(event.getPlayer(), null, false);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event)
	{
		cancelTasks(event.getPlayer(), null, false);
	}

	public boolean showLines(final Player player, Sign sign, String[] newLines, long ticks, boolean forceChange)
	{
		final String[] lines = sign.getLines();
		if (Arrays.equals(lines, newLines)) { return false; }
		Collection<SignChange> signChanges = getSignChanges(player);
		Iterator<SignChange> iterator = signChanges.iterator();
		while (iterator.hasNext())
		{
			SignChange signChange = (SignChange) iterator.next();
			if (signChange.sign.equals(sign))
			{
				if ((!forceChange) && (Arrays.equals(signChange.newLines, newLines))) { return false; }
				signChange.runnable.cancel();
				iterator.remove();
				break;
			}
		}
		final Location location = sign.getLocation();
		player.sendSignChange(location, newLines);
		final SignChange signChange;
		if (signChanges.add(signChange = new SignChange(sign, newLines)))
		{
			final Block block = sign.getBlock();
			final BlockState previous = block.getState();
			BukkitRunnable runnable = new BukkitRunnable()
			{
				public void run()
				{
					if ((SignHandler.this.signUpdateMap.remove(player.getUniqueId(), signChange)) && (previous.equals(block.getState())))
					{
						player.sendSignChange(location, lines);
					}
				}
			};
			runnable.runTaskLater(this.plugin, ticks);
			signChange.runnable = runnable;
		}
		return true;
	}

	public Collection<SignChange> getSignChanges(Player player)
	{
		return this.signUpdateMap.get(player.getUniqueId());
	}

	public void cancelTasks(Sign sign)
	{
		Iterator<SignChange> iterator = this.signUpdateMap.values().iterator();
		while (iterator.hasNext())
		{
			SignChange signChange = (SignChange) iterator.next();
			if ((sign == null) || (signChange.sign.equals(sign)))
			{
				signChange.runnable.cancel();
				signChange.sign.update();
				iterator.remove();
			}
		}
	}

	public void cancelTasks(Player player, Sign sign, boolean revertLines)
	{
		UUID uuid = player.getUniqueId();
		Iterator<Map.Entry<UUID, SignChange>> iterator = this.signUpdateMap.entries().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<UUID, SignChange> entry = (Map.Entry) iterator.next();
			if (((UUID) entry.getKey()).equals(uuid))
			{
				SignChange signChange = (SignChange) entry.getValue();
				if ((sign == null) || (signChange.sign.equals(sign)))
				{
					if (revertLines)
					{
						player.sendSignChange(signChange.sign.getLocation(), signChange.sign.getLines());
					}
					signChange.runnable.cancel();
					iterator.remove();
				}
			}
		}
	}

	private static final class SignChange
	{
		public final Sign sign;
		public final String[] newLines;
		public BukkitRunnable runnable;

		public SignChange(Sign sign, String[] newLines)
		{
			this.sign = sign;
			this.newLines = newLines;
		}
	}
}
