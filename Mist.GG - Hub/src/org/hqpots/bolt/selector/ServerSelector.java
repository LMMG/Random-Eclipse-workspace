package org.hqpots.bolt.selector;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.hqpots.bolt.Bolt;
import org.hqpots.bolt.listeners.ItemStackBuilder;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerSelector implements Listener, PluginMessageListener
{
	static List<String> serverSelectorLore = new ArrayList<>();
	static List<String> hardcoreFactionsLore = new ArrayList<>();
	static List<String> developmentLore = new ArrayList<>();
	static List<String> kitsLore = new ArrayList<>();
	static List<String> practiceLore = new ArrayList<>();
	static List<String> kohisgLore = new ArrayList<>();
	static List<String> none = new ArrayList<>();
	@SuppressWarnings({
			"deprecation",
			"unused"
	})
	private static ItemStack somethhing = new ItemStack(351, 1, (short) 8);
	public static int kits = 0;
	public static int pra1c = 0;
	public static int hcf1 = 0;

	public void getCount(String server)
	{
		try
		{
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("PlayerCount");
			out.writeUTF(server);

			Bukkit.getServer().sendPluginMessage(Bolt.getInstance(), "BungeeCord", out.toByteArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if (!channel.equals("BungeeCord")) { return; }
		try
		{
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String command = in.readUTF();

			if (command.equals("PlayerCount"))
			{
				String subchannel = in.readUTF();
				if (subchannel.equals("hcf"))
				{
					int playercount = in.readInt();
					hcf1 = playercount;
				}
				if (subchannel.equals("kits"))
				{
					int playercount = in.readInt();
					kits = playercount;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static
	{
		practiceLore.add(ChatColor.translateAlternateColorCodes('&', "&aOnline: &6" + pra1c));

		hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&aOnline: &6" + hcf1));
		
		kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&aOnline: &6" + kits));
	}

	public static ItemStack selector = ItemStackBuilder.get(Material.WATCH, 1, (short) 0, "&aServer Selector", null);
	public static ItemStack prac = ItemStackBuilder.get(Material.FISHING_ROD, 1, (short) 0, "&6Practice &c(Season I)", practiceLore);
	public static ItemStack kitmap = ItemStackBuilder.get(Material.DIAMOND_SWORD, 1, (short)0, "&6Kits &c(Season II)", kitsLore);
	public static ItemStack hcf = ItemStackBuilder.get(Material.BOW, 1, (short)0, "&6HCF &c(Map II)", hardcoreFactionsLore);
	public static Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&6Server Selector"));

	public ServerSelector()
	{
		getCount("kits");
		getCount("ALL");
		getCount("practice");
		getCount("hcf");
		inv.setItem(8, prac);
		inv.setItem(4, hcf);
		inv.setItem(0, kitmap);
	}

	@EventHandler
	public void InventoryClick(InventoryClickEvent e)
	{
		e.setCancelled(true);
		if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) { return; }
		if (e.getInventory().getType().equals(InventoryType.PLAYER))
		{
			e.setCancelled(false);
		}
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if (item.isSimilar(prac))
		{
			p.closeInventory();
		}
		else if(item.isSimilar(kitmap)) {
			Bukkit.dispatchCommand(p, "play kits");
			p.sendMessage(ChatColor.GREEN + "Your are being send to Kits");
			p.closeInventory();
		}
		else if(item.isSimilar(hcf)) {
			Bukkit.dispatchCommand(p, "play hcf");
			p.sendMessage(ChatColor.GREEN + "Your are being send to HCF");
			p.closeInventory();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(selector)))
		{
			Player p = e.getPlayer();
			p.openInventory(inv);
			e.setCancelled(true);
		}
	}
}
