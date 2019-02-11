package rip.kohi.utils;

import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

public class IconMenuUtil implements Listener
{
	private String name;
	private int size;
	private OptionClickEventHandler handler;
	private Plugin plugin;
	private String[] optionNames;
	private ItemStack[] optionIcons;

	public IconMenuUtil(final String name, final int size, final OptionClickEventHandler handler, final Plugin plugin)
	{
		this.name = name;
		this.size = size;
		this.handler = handler;
		this.plugin = plugin;
		this.optionNames = new String[size];
		this.optionIcons = new ItemStack[size];
		plugin.getServer().getPluginManager().registerEvents((Listener) this, plugin);
	}

	public IconMenuUtil setOption(final int position, final ItemStack icon, final String name, final String... info)
	{
		this.optionNames[position] = name;
		this.optionIcons[position] = this.setItemNameAndLore(icon, name, info);
		return this;
	}

	public IconMenuUtil setOption(final int position, final ItemStack icon)
	{
		this.optionNames[position] = icon.getItemMeta().getDisplayName();
		this.optionIcons[position] = this.setItemNameAndLore(icon, icon.getItemMeta().getDisplayName(), new String[0]);
		return this;
	}

	public void open(final Player player)
	{
		final Inventory inventory = Bukkit.createInventory((InventoryHolder) player, this.size, this.name);
		for (int i = 0; i < this.optionIcons.length; ++i)
		{
			if (this.optionIcons[i] != null)
			{
				inventory.setItem(i, this.optionIcons[i]);
			}
		}
		player.openInventory(inventory);
	}

	public void destroy()
	{
		HandlerList.unregisterAll((Listener) this);
		this.handler = null;
		this.plugin = null;
		this.optionNames = null;
		this.optionIcons = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClick(final InventoryClickEvent event)
	{
		if (event.getInventory().getTitle().equals(this.name))
		{
			event.setCancelled(true);
			final int slot = event.getRawSlot();
			if (slot >= 0 && slot < this.size && this.optionNames[slot] != null)
			{
				final Plugin plugin = this.plugin;
				final OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, this.optionNames[slot]);
				this.handler.onOptionClick(e);
				if (e.willClose())
				{
					final Player p = (Player) event.getWhoClicked();
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (Runnable) new Runnable()
					{
						@Override
						public void run()
						{
							p.closeInventory();
						}
					}, 1L);
				}
				if (e.willDestroy())
				{
					this.destroy();
				}
			}
		}
	}

	private ItemStack setItemNameAndLore(final ItemStack item, final String name, final String[] lore)
	{
		final ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore((List) Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}

	public class OptionClickEvent
	{
		private Player player;
		private int position;
		private String name;
		private boolean close;
		private boolean destroy;

		public OptionClickEvent(final Player player, final int position, final String name)
		{
			this.player = player;
			this.position = position;
			this.name = name;
			this.close = true;
			this.destroy = false;
		}

		public Player getPlayer()
		{
			return this.player;
		}

		public int getPosition()
		{
			return this.position;
		}

		public String getName()
		{
			return this.name;
		}

		public boolean willClose()
		{
			return this.close;
		}

		public boolean willDestroy()
		{
			return this.destroy;
		}

		public void setWillClose(final boolean close)
		{
			this.close = close;
		}

		public void setWillDestroy(final boolean destroy)
		{
			this.destroy = destroy;
		}
	}

	public interface OptionClickEventHandler
	{
		void onOptionClick(final OptionClickEvent p0);
	}
}
