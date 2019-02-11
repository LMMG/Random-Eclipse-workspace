package me.kairos.ipunish.listeners;

import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.profiles.Profile;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

    private final IPunish plugin;

    public PlayerListeners(IPunish plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            Inventory inventory = event.getClickedInventory();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                if (inventory.getTitle().contains("Punish")) {
                    event.setCancelled(true);
                    String playerName = inventory.getTitle().substring(9, inventory.getTitle().length());
                    FileConfiguration configuration = plugin.getConfig();
                    for (String string : configuration.getConfigurationSection("items").getKeys(false)) {
                        if (Material.getMaterial(configuration.getString("items." + string + ".item_type")) == event.getCurrentItem().getType()) {
                            ItemStack item = event.getCurrentItem();
                            if (item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', configuration.getString("items." + string + ".item_name").replace("%PLAYER%", playerName)))) {
                                Bukkit.dispatchCommand(player, configuration.getString("items." + string + ".item_command").replace("%PLAYER%", playerName));
                                player.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (profile.getMuteRemaining() == Profile.PunishmentStatus.PERMANENT.getId()) {
            // permanent mute
            return;
        }

        if (profile.getMuteRemaining() != Profile.PunishmentStatus.NONE.getId()) {
            event.setCancelled(true);
            player.sendMessage(plugin.getConfiguration().getMuteMessage().replace("%TIME%",
                    DurationFormatUtils.formatDurationWords(profile.getMuteRemaining(), true, true)).replace("%REASON%", profile.getMuteReason()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return; // Player couldn't join anyway, don't load profile.
        }

        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().loadOrSaveProfile(player.getUniqueId(), event.getAddress());

        if (profile.getBanRemaining() == Profile.PunishmentStatus.PERMANENT.getId()) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.getConfiguration().getBanMessage().replace("%REASON%", profile.getBanReason()));
            return;
        }

        if (profile.getBanRemaining() != Profile.PunishmentStatus.NONE.getId()) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.getConfiguration().getTempbanMessage().replace("%REASON%", profile.getBanReason()).
                    replace("%TIME%", DurationFormatUtils.formatDurationWords(profile.getBanRemaining(), true, true)));
        }
    }
}
