package net.nersa.player;

import lombok.Getter;
import lombok.Setter;
import net.nersa.kitmap.HCF;
import net.nersa.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerData {

    @Getter private Player player;
    private PlayerConfig playerConfig;

    @Getter private Set<UUID> factionChatSpying = new HashSet<>();

    @Getter @Setter private boolean hidingScoreboard;
    @Getter @Setter private boolean hidingChat;
    @Getter @Setter private boolean hidingMessages;
    @Getter @Setter private boolean hidingDuels;
    @Getter @Setter private boolean showClaimMap;

    @Getter @Setter private int kills;
    @Getter @Setter private int deaths;

    @Getter @Setter private long lastFactionLeaveMillis;

    public PlayerData(Player player) {
        this.player = player;
        this.playerConfig = new PlayerConfig(new File(HCF.getInstance().getDataFolder() + "/playerdata/"), player.getUniqueId().toString() + ".yml");

        this.hidingChat = false;
        this.hidingMessages = false;
        this.hidingScoreboard = false;
        this.hidingDuels = false;

        this.kills = 0;
        this.deaths = 0;

        this.load();
    }
    
    /*public void openSettings() {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.LIGHT_PURPLE + "Your Settings");

        inventory.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE, ChatColor.RED + "", 1, (byte)14, new String[0]).getItem());
        inventory.setItem(1, new ItemBuilder(Material.BOOK, (this.hidingMessages ? ChatColor.GREEN + "Show Messages" : (ChatColor.RED + "Hide Messages"))).getItem());
        inventory.setItem(2, new ItemBuilder(Material.BOOK, (this.hidingChat ? ChatColor.GREEN + "Show Chat" : (ChatColor.RED + "Hide Chat"))).getItem());
        inventory.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE, ChatColor.RED + "", 1, (byte)14, new String[0]).getItem());
        inventory.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE, ChatColor.RED + "", 1, (byte)14, new String[0]).getItem());
        inventory.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE, ChatColor.RED + "", 1, (byte)14, new String[0]).getItem());
        inventory.setItem(6, new ItemBuilder(Material.BOOK, (this.hidingDuels ? ChatColor.GREEN + "Show Duels" : (ChatColor.RED + "Hide Duels"))).getItem());
        inventory.setItem(7, new ItemBuilder(Material.BOOK, (this.hidingScoreboard ? ChatColor.GREEN + "Show Scoreboard" : (ChatColor.RED + "Hide Scoreboard"))).getItem());
        inventory.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE, ChatColor.RED + "", 1, (byte)14, new String[0]).getItem());

        this.player.openInventory(inventory);
    }*/

    public void load() {
        this.hidingMessages = playerConfig.getConfig().contains("hiding-msgs") && playerConfig.getConfig().getBoolean("hiding-msgs");
        this.hidingChat = playerConfig.getConfig().contains("hiding-chat") && playerConfig.getConfig().getBoolean("hiding-chat");
        this.hidingDuels = playerConfig.getConfig().contains("hiding-duels") && playerConfig.getConfig().getBoolean("hiding-duels");
        this.kills = playerConfig.getConfig().getInt("kills");
        this.deaths = playerConfig.getConfig().getInt("deaths");
    }

    public void save() {
        HCF.getInstance().getStorageBackend().saveProfile(player);

        playerConfig.getConfig().set("hiding-msgs", this.hidingMessages);
        playerConfig.getConfig().set("hiding-chat", this.hidingChat);
        playerConfig.getConfig().set("hiding-duels", this.hidingDuels);
        playerConfig.getConfig().set("kills", this.kills);
        playerConfig.getConfig().set("deaths", this.deaths);
        playerConfig.save();
    }

}