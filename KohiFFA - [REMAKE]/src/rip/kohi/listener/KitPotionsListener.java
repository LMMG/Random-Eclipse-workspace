package rip.kohi.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class KitPotionsListener implements Listener
{

	public static Inventory inv;

	@EventHandler
	public void onBrewing(PlayerInteractEvent e)
	{
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BREWING_STAND) {
            e.setCancelled(true);
            p.openInventory(this.brewing(p));
            p.playSound(p.getLocation(), Sound.DRINK, 10.0f, 1.0f);
            return;
        }
	}

	public Inventory brewing(Player p)
	{
		inv = Bukkit.createInventory(p, 54, "6Potions");
		ItemStack PotionHeal_FullInv = new ItemStack(373, 1, (short) 16421);
		ItemStack PotionHeal = new ItemStack(373, 1, (short) 16421);
		ItemStack PotionPoison = new ItemStack(373, 1, (short) 16388);
		ItemStack PotionSlow = new ItemStack(373, 1, (short) 16394);
		ItemStack PotionSpeed_II = new ItemStack(373, 1, (short) 8226);
		ItemStack PotionSpeed_I = new ItemStack(373, 1, (short) 8258);
		ItemStack PotionFire = new ItemStack(373, 1, (short) 8259);
		inv.setItem(0, PotionHeal_FullInv);
		inv.setItem(18, PotionHeal);
		inv.setItem(19, PotionHeal);
		inv.setItem(20, PotionHeal);
		inv.setItem(27, PotionHeal);
		inv.setItem(28, PotionHeal);
		inv.setItem(29, PotionHeal);
		inv.setItem(3, PotionPoison);
		inv.setItem(4, PotionPoison);
		inv.setItem(12, PotionSlow);
		inv.setItem(13, PotionSlow);
		inv.setItem(5, PotionSpeed_II);
		inv.setItem(14, PotionSpeed_II);
		inv.setItem(23, PotionSpeed_II);
		inv.setItem(32, PotionSpeed_II);
		inv.setItem(8, PotionSpeed_I);
		inv.setItem(26, PotionFire);
		inv.setItem(35, PotionFire);
		return inv;
	}

	@EventHandler
	public void onBrewingClick(final InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		PlayerInventory inv = p.getInventory();
		if (e.getInventory().getTitle().contains("§6Kit potion") && e.getRawSlot() == 0)
		{
			e.setCancelled(true);
			p.sendMessage("§9All your inventory has been filled with potions.");
			final ItemStack PotionHeal = new ItemStack(373, 1, (short) 16421);
			for (int i = 0; i < 36; ++i)
			{
				inv.addItem(new ItemStack[] {
						PotionHeal
				});
			}
		}
	}
}
