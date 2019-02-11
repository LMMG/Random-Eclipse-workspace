package gg.vexus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import gg.vexus.Core;
import net.md_5.bungee.api.ChatColor;

public class FoundDiamondsListener implements Listener
{

	@EventHandler
	public void onPlayer(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		if ((e.getBlock().getType() == Material.DIAMOND_ORE) && (!e.getBlock().hasMetadata("FD")))
		{
			if (player.getGameMode() == GameMode.CREATIVE) { return; }
			if (e.isCancelled()) { return; }
			int d = 0;
			for (int x = -5; x < 5; x++)
			{
				for (int y = -5; y < 5; y++)
				{
					for (int z = -5; x < 5; z++)
					{
						Block block = e.getBlock().getRelative(x, y, z);
						if ((block.getType() == Material.DIAMOND_ORE) && (!block.hasMetadata("FD")))
						{
							d++;
							block.setMetadata("FD", new FixedMetadataValue(Core.getCore(), Boolean.valueOf(true)));
						}
					}
				}
			}
			Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.GRAY + " has found " + ChatColor.WHITE + d + ChatColor.AQUA + " diamonds.");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		if (b.getType() == Material.DIAMOND_ORE)
		{
			b.setMetadata("FD", new FixedMetadataValue(Core.getCore(), Boolean.valueOf(true)));
		}
	}
}
