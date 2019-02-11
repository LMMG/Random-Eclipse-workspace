package gg.vexus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class AntiAbuseListeners implements Listener
{

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if ((p.getGameMode() == GameMode.CREATIVE) && (!p.hasPermission("common.vanish")))
		{
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().clear();
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e)
	{
		Player p = (Player) e.getWhoClicked();
		if ((e.getCurrentItem() != null) && (e.getCurrentItem().containsEnchantment(Enchantment.DAMAGE_ALL)) && (e.getCurrentItem().getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 5))
		{
			p.sendMessage(ChatColor.RED + "Nope, you are not using this item today.");
			p.getInventory().removeItem(new ItemStack[] {
					p.getItemInHand()
			});
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if ((e.getDamager() instanceof Player))
		{
			Player p = (Player) e.getDamager();
			if ((p.getItemInHand() != null) && (p.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) && (p.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 5))
			{
				p.sendMessage(ChatColor.RED + "Nope, you are not using this item today.");
				p.getInventory().removeItem(new ItemStack[] {
						p.getItemInHand()
				});
				e.setCancelled(true);
			}
		}
	}
}
