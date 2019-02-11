package gg.vexus.listeners;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gg.vexus.Core;

import java.util.ArrayList;

/**
 * Created by Owner on 02/07/2017.
 */
public class InventoryListener implements Listener {

	/***
	 * TODO: Update More efficient Thread Friendly way of getting Ores "Maybe
	 * Redis", "Maybe SQL", "Or mongo"
	 * 
	 * @param e
	 */

	@EventHandler
	public void br(BlockBreakEvent e) {
		Player player = e.getPlayer();
		ItemStack stack = player.getItemInHand();
		if (stack.getType() == Material.DIAMOND_PICKAXE || stack.getType() == Material.IRON_PICKAXE
				|| stack.getType() == Material.GOLD_PICKAXE || stack.getType() == Material.WOOD_PICKAXE
				|| stack.getType() == Material.HOPPER_MINECART) {
			ItemMeta sm = stack.getItemMeta();
			if (sm.hasLore()) {
				sm.getLore().clear();
			}

			ArrayList<String> lore = new ArrayList<String>();
			
			lore.add("§7§ §bDiamond Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
			lore.add("§7§ §aEmerald Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
			lore.add("§7§ §7Iron Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
			lore.add("§7§ §eGold Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
			lore.add("§7§ §cRedstone Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
			lore.add("§7§ §8Coal Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
			lore.add("§7§ §9Lapis Ore: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
			sm.setLore(lore);
			stack.setItemMeta(sm);
		}
	}
}
