package me.kairos.ipunish.commands;

import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PunishCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public PunishCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.PLAYER_ONLY_COMMAND);
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration configuration = plugin.getConfig();

        if (args.length > 0) {
            String playerName;
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target != null) {
                playerName = target.getName();
            } else {
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);

                if (!offlineTarget.hasPlayedBefore()) {
                    sender.sendMessage(Utils.getPlayerNotFoundMessage(args[0]));
                    return true;
                }

                playerName = offlineTarget.getName();
            }

            Inventory inventory = Bukkit.createInventory(player, 45, ChatColor.RED + "Punish " + playerName);
            for (String string : configuration.getConfigurationSection("items").getKeys(false)) {
                ItemStack itemStack = new ItemStack(Material.valueOf(configuration.get("items." + string + ".item_type").toString()));
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', configuration.getString("items." + string + ".item_name").replace("%PLAYER%", playerName)));
                List<String> loreList = new ArrayList<>();
                for (String lore : configuration.getStringList("items." + string + ".item_lore")) {
                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore.replace("%PLAYER%", playerName)));
                }

                meta.setLore(loreList);
                itemStack.setItemMeta(meta);
                itemStack.setDurability((byte) configuration.getInt("items." + string + ".item_data"));
                inventory.setItem(configuration.getInt("items." + string + ".item_position"), itemStack);
            }

            player.openInventory(inventory);
            return true;
        }

        player.sendMessage(ChatColor.RED + "/" + label + " <playerName>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}
