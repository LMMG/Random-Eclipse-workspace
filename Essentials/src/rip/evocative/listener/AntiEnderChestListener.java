package rip.evocative.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import rip.evocative.Essentials;

public class AntiEnderChestListener implements Listener
{
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if (Essentials.config.getBoolean("ANTIENDERCHEST.MODULE"))
		{
			Player p = e.getPlayer();
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENDER_CHEST)
			{
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Ender chests are disabled.");
			}
		}
	}

	@EventHandler
	public void onCraft(PrepareItemCraftEvent e)
	{
		if (Essentials.config.getBoolean("ANTIENDERCHEST.MODULE"))
		{
			if (e.getRecipe().getResult().getType() == Material.ENDER_CHEST)
			{
				e.getRecipe().getResult().setType(Material.AIR);
			}
		}
	}
}
