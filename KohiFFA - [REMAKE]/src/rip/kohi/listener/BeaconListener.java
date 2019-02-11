package rip.kohi.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BeaconListener implements Listener
{

	private Inventory inv;

	public boolean isSword(ItemStack item)
	{
		return item.getType().equals(Material.WOOD_SWORD) 
				|| item.getType().equals(Material.STONE_SWORD) 
				|| item.getType().equals(Material.IRON_SWORD) 
				|| item.getType().equals(Material.DIAMOND_SWORD) 
				|| item.getType().equals(Material.GOLD_SWORD);
	}
	
	@EventHandler
	public void onBeaconOpen(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (((e.getAction() == Action.RIGHT_CLICK_BLOCK 
				&& e.getClickedBlock().getType() == Material.BEACON) 
				|| (e.getAction() == Action.LEFT_CLICK_BLOCK 
				&& e.getClickedBlock().getType() == Material.BEACON)) 
				&& e.getItem() != null && this.isSword(e.getItem())) {
			List<String> first = new ArrayList<String>();
			first.add("§a§o§mEpic");
            first.add("§b§o§mAwesome");
            first.add("§c§o§mCool");
            first.add("§d§o§mLegendary");
            first.add("§e§o§mSick");
            final List<String> last = new ArrayList<String>();
            last.add("§2§o§mKiller");
            last.add("§3§o§mSwinger");
            last.add("§4§o§mSword");
            last.add("§5§o§mBouncer");
            last.add("§6§o§mKicker");
            final Random random = new Random();
            final String name = String.valueOf(first.get(random.nextInt(first.toArray().length))) + " " + last.get(random.nextInt(last.toArray().length));
            final ItemStack item = player.getItemInHand();
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            player.setItemInHand(item);
            player.playSound(player.getLocation(), Sound.ANVIL_USE, 10.0f, 1.0f);
            player.sendMessage("§6Your sword name has been changed correctly!");
            e.setCancelled(true);
		}
	}
}
