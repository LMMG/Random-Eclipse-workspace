package rip.evocative.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import rip.evocative.Essentials;

public class FoundDiamondsListener implements Listener
{
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		if (Essentials.config.getBoolean("FOUNDDIAMONDS.MODULE"))
		{
			Player p = e.getPlayer();
			if (e.getBlock().getType() == Material.DIAMOND_ORE && !e.getBlock().hasMetadata("FD"))
			{
				if (p.getGameMode() == GameMode.CREATIVE) { return; }
				if (e.isCancelled()) { return; }
				int d = 0;
				for (int x = -5; x < 5; ++x)
				{
					for (int y = -5; y < 5; ++y)
					{
						for (int z = -5; z < 5; ++z)
						{
							final Block b = e.getBlock().getRelative(x, y, z);
							if (b.getType() == Material.DIAMOND_ORE && !b.hasMetadata("FD"))
							{
								++d;
								b.setMetadata("FD", new FixedMetadataValue(Essentials.getInstance(), true));
							}
						}
					}
				}
				Bukkit.broadcastMessage(ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has found " + ChatColor.WHITE + d + ChatColor.AQUA + " diamonds.");
			}
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent e)
	{
		if (Essentials.config.getBoolean("FOUNDDIAMONDS.MODULE"))
		{
			final Block b = e.getBlock();
			if (b.getType() == Material.DIAMOND_ORE)
			{
				b.setMetadata("FD", new FixedMetadataValue(Essentials.getInstance(), true));
			}
		}
	}
}