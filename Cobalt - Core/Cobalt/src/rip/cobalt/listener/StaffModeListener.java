package rip.cobalt.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.commands.staff.Staffmode;

public class StaffModeListener implements Listener
{
	/*
	 * 
	 * COMPASS BOOK - Inspection FEATHER - Vanish WORLDEDITWAND - worldedit
	 * IRON_FENCE - freeze
	 */

	Staffmode staff;

	public ItemStack Compass = new ItemStack(Material.COMPASS, 1);
	public ItemStack Book = new ItemStack(Material.BOOK, 1);
	public ItemStack Feather = new ItemStack(Material.FEATHER, 1);
	public ItemStack Worldedit = new ItemStack(Material.WOOD_AXE, 1);
	public ItemStack Freeze = new ItemStack(Material.IRON_FENCE, 1);

	public void GiveCompass(Player p, int i)
	{
		ItemMeta Compassm = Compass.getItemMeta();
		Compassm.setDisplayName("here goes display name");
		Compass.setItemMeta(Compassm);
		p.getInventory().setItem(i, Compass);
	}

	public void GiveBook(Player p, int i)
	{
		ItemMeta Bookm = Book.getItemMeta();
		Bookm.setDisplayName("here goes display name");
		Compass.setItemMeta(Bookm);
		p.getInventory().setItem(i, Book);
	}

	public void GiveFeather(Player p, int i)
	{
		ItemMeta Featherm = Compass.getItemMeta();
		Featherm.setDisplayName("here goes display name");
		Compass.setItemMeta(Featherm);
		p.getInventory().setItem(i, Feather);
	}

	public void GiveWorldedit(Player p, int i)
	{
		ItemMeta Worldeditm = Worldedit.getItemMeta();
		Worldeditm.setDisplayName("here goes display name");
		Compass.setItemMeta(Worldeditm);
		p.getInventory().setItem(i, Worldedit);
	}

	public void GiveFreeze(Player p, int i)
	{
		ItemMeta Compassm = Compass.getItemMeta();
		Compassm.setDisplayName("here goes display name");
		Compass.setItemMeta(Compassm);
		p.getInventory().setItem(i, Freeze);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		if (player.hasPermission("core.staff"))
		{
			staff.staffmode.add(player.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e)
	{
		Player player = e.getPlayer();
		if (staff.staffmode.contains(player.getUniqueId()))
		{
			e.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't break blocks in staffmode");
		}
		else
		{}
	}

	@EventHandler
	public void onPlayerBlockBreal(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		if (staff.staffmode.contains(player.getUniqueId()))
		{
			e.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't break blocks in staffmode");
		}
		else
		{}
	}
}
