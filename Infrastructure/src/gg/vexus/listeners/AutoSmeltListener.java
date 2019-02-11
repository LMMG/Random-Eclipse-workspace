package gg.vexus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class AutoSmeltListener implements Listener
{

	@EventHandler
	public void onPlayerBreak(BlockBreakEvent e)
	{
		if (e.getPlayer().hasPermission("common.autosmelt"))
		{

			if (e.getBlock().getType() == Material.GOLD_ORE)
			{
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				Bukkit.getWorld(e.getPlayer().getWorld().getName()).dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
				return;
			}
			if (e.getBlock().getType() == Material.IRON_ORE)
			{
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				Bukkit.getWorld(e.getPlayer().getWorld().getName()).dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, 1));
				return;
			}
		}
	}
}
