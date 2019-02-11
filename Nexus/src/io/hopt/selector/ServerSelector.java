package io.hopt.selector;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.hopt.Nexus;
import io.hopt.utils.ItemStackBuilder;
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

import java.util.ArrayList;
import java.util.List;

public class ServerSelector implements Listener {
    public static int Online = 1;
    public static int HCFactions = 0;
    public static int Kits = 0;

    static List<String> serverSelectorLore = new ArrayList<>();
    static List<String> hardcoreFactionsLore = new ArrayList<>();
    static List<String> kitsLore = new ArrayList<>();
    static List<String> potsLore = new ArrayList<>();
    static List<String> none = new ArrayList<>();

    static {
        serverSelectorLore.add(ChatColor.translateAlternateColorCodes('&', "&b&lRIGHT CLICK &7to open the server selector"));

        hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Create a team of up to 30 players and try to become the"));
        hardcoreFactionsLore.add(ChatColor.translateAlternateColorCodes('&', "&7most powerful team on the server and show off your glory"));
        hardcoreFactionsLore.add("");
        hardcoreFactionsLore.add(ChatColor.RED + "Conquer the world with your team, become the best.");
        hardcoreFactionsLore.add(" ");
        hardcoreFactionsLore.add(ChatColor.AQUA + "Map Kit: " + ChatColor.WHITE + "Protection 1, Sharpness 1");
        hardcoreFactionsLore.add(ChatColor.AQUA + "Faction Limit:" + ChatColor.WHITE + " 30");
        hardcoreFactionsLore.add(" ");
        hardcoreFactionsLore.add(ChatColor.AQUA + "Players: " + ChatColor.WHITE + HCFactions + "/250");

        kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Create a team of up to 30 players and try to become the"));
        kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7most powerful team on the server and show off your glory"));
        kitsLore.add("");
        kitsLore.add(ChatColor.RED + "Get the Most Kills and be the best team");
        kitsLore.add(" ");
        kitsLore.add(ChatColor.AQUA + "Map Kit: " + ChatColor.WHITE + "Protection 1, Sharpness 1");
        kitsLore.add(ChatColor.AQUA + "Faction Limit:" + ChatColor.WHITE + " 30");
        kitsLore.add(" ");
        kitsLore.add(ChatColor.AQUA + "Players: " + ChatColor.WHITE + Kits + "/250");

        potsLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------"));
        potsLore.add(ChatColor.translateAlternateColorCodes('&', "&cServer Offline"));
        potsLore.add(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------"));

        none.add("");
    }

    /**
     * TODO:
     *
     * Make code more efficient
     * Update code
     *
     */

    public static ItemStack glass1 = ItemStackBuilder.get(Material.STAINED_GLASS_PANE, 1, (short) 0, "", none);
    public static ItemStack selector = ItemStackBuilder.get(Material.COMPASS, 1, (short) 0, "&bServer Selector", serverSelectorLore);
    public static ItemStack factions = ItemStackBuilder.get(Material.FISHING_ROD, 1, (short) 0, "&bHCFactions", hardcoreFactionsLore);
    public static ItemStack kitmap = ItemStackBuilder.get(Material.DIAMOND_HELMET, 1, (short) 0, "&bKitMap", kitsLore);
    public static ItemStack pots = ItemStackBuilder.get(Material.DIAMOND_SWORD, 1, (short) 0, "&bPractice", potsLore);
    public static ItemStack leave = ItemStackBuilder.get(Material.INK_SACK, 1, (short) 1, "&cLeave Server", null);
    public static Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&bServer Selector"));

    /**
     * TODO:
     *
     * Make code more efficient
     * Update code
     *
     */

    public ServerSelector() {
        inv.setItem(0, glass1);
        inv.setItem(1, glass1);
        inv.setItem(2, glass1);
        inv.setItem(3, glass1);
        inv.setItem(4, glass1);
        inv.setItem(5, glass1);
        inv.setItem(6, glass1);
        inv.setItem(7, glass1);
        inv.setItem(8, glass1);
        inv.setItem(7, glass1);
        inv.setItem(9, glass1);
        inv.setItem(10, glass1);
        inv.setItem(12, glass1);
        inv.setItem(14, glass1);
        inv.setItem(16, glass1);
        inv.setItem(17, glass1);
        inv.setItem(18, glass1);
        inv.setItem(19, glass1);
        inv.setItem(20, glass1);
        inv.setItem(21, glass1);
        inv.setItem(22, glass1);
        inv.setItem(23, glass1);
        inv.setItem(24, glass1);
        inv.setItem(25, glass1);
        inv.setItem(26, leave);

        inv.setItem(13, factions);
        inv.setItem(11, kitmap);
        inv.setItem(15, pots);
    }

    /**
     * TODO: Redo this method
     */

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) {
            return;
        }
        if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
            e.setCancelled(false);
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (item.isSimilar(factions)) {
            Bukkit.dispatchCommand(p, "play HCF");
            p.closeInventory();
        } else if (item.isSimilar(kitmap)) {
            Bukkit.dispatchCommand(p, "play Kitmap");
            p.closeInventory();
        } else if (item.isSimilar(pots)) {
            Bukkit.dispatchCommand(p, "play Practice");
            p.closeInventory();
        } else if (item.isSimilar(glass1)) {
            p.closeInventory();
        }
        else if(item.isSimilar(leave))
        {
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + e.getWhoClicked() + "§cThanks for joining");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(selector))) {
            Player p = e.getPlayer();
            p.openInventory(inv);
            e.setCancelled(true);
        }
    }

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
            else if (command.equals("PlayerCount")) {
                String subchannel = in.readUTF();
                if (subchannel.equals("HCF")) {
                    int playercount = in.readInt();
                    HCFactions = playercount;
                } else if(subchannel.equals("KITMAP")) {
                	int playercount = in.readInt();
                	Kits = playercount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
