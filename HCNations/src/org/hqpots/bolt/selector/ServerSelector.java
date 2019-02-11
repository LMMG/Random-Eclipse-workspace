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
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.hqpots.bolt.listeners.ItemStackBuilder;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;


public class ServerSelector implements Listener,  PluginMessageListener
{
    static List<String> serverSelectorLore = new ArrayList<>();
    static List<String> hardcoreFactionsLore = new ArrayList<>();
    static List<String> developmentLore = new ArrayList<>();
    static List<String> kitsLore = new ArrayList<>();
    static List<String> practiceLore = new ArrayList<>();
    static List<String> kohisgLore = new ArrayList<>();
    private static ItemStack somethhing = new ItemStack(351, 1, (short)8);
    public static int Online = 0;
    
    static {
        serverSelectorLore.add(ChatColor.translateAlternateColorCodes('&', "&b&lRIGHT CLICK &eto play a server"));

        hardcoreFactionsLore.add("");
        hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&7DTR, Deathban, and Hardcore"));
        hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Map started on: 7th January"));
        hardcoreFactionsLore.add("");
        hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to connect]"));


        kohisgLore.add("");
        kohisgLore.add(ChatColor.translateAlternateColorCodes('&', "&cDevelopment"));
        kohisgLore.add("");
        kohisgLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to connect]"));
        
        kitsLore.add("");
        kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7DTR, Non-Deathban, and Hardcore"));
        kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Map started on: 22th December"));
        kitsLore.add("");
        kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&aOnline: " + Online));
        kitsLore.add("");
        kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to connect]"));
    }

    public static ItemStack selector = ItemStackBuilder.get(Material.COMPASS, 1, (short)0, "&aServer Selector", serverSelectorLore);
    public static ItemStack ddd = ItemStackBuilder.get(Material.COMPASS, 1, (short)0, "&bLobbies", developmentLore);
    public static ItemStack factions = ItemStackBuilder.get(Material.FISHING_ROD, 1, (short)0, "&aHardcore Factions", hardcoreFactionsLore);
    public static ItemStack kitmap = ItemStackBuilder.get(Material.DIAMOND_SWORD, 1, (short)0, "&aKitMap", kitsLore);
    public static ItemStack kohiSG = ItemStackBuilder.get(Material.EXP_BOTTLE, 1, (short)0, "&aBunkers", kohisgLore);
    public static ItemStack development = ItemStackBuilder.get(Material.EXP_BOTTLE, 1, (short)0, "&aUp And Coming Gamemode", null);
    public static ItemStack creative = ItemStackBuilder.get(Material.EXP_BOTTLE, 1, (short)0, "&aCreative", kohisgLore);

    public static Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&aServer Selector"));

    public ServerSelector()
    {
        inv.setItem(4, factions);
        inv.setItem(2, kitmap);
        inv.setItem(6, kohiSG);
        inv.setItem(8, development);
        inv.setItem(0, creative);
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e)
    {
        e.setCancelled(true);
        if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) {
            return;
        }
        if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
            e.setCancelled(false);
        }
        Player p = (Player)e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if ((item.getType().equals(ddd)) && (e.getSlot() + 1 == item.getAmount()))
        {
            PlayerEvents.sendPlayerToServer(p, "dev");
            p.closeInventory();
        }
        else if (item.isSimilar(factions))
        {
        	p.sendMessage("");
            PlayerEvents.sendPlayerToServer(p, "hcf");
            p.sendMessage("");
            p.closeInventory();
        }
        else if (item.isSimilar(kitmap)) {
        	Bukkit.dispatchCommand(p, "play kits");
        	p.closeInventory();
        } else if(item.isSimilar(kohiSG)) {
            PlayerEvents.sendPlayerToServer(p, "kohisg");
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
    
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		try {
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String command = in.readUTF();

			if (command.equals("PlayerCount")) {
				String subchannel = in.readUTF();
				if (subchannel.equals("ALL")) {
					int playercount = in.readInt();
					Online = playercount;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
