package com.hcempire.horus.economysign;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.utils.ItemBuilder;
import com.hcempire.horus.Horus;
import com.hcempire.horus.util.ItemNames;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomySignListeners implements Listener {

    private static Horus main = Horus.getInstance();
    private static Economy economy = Ekko.getInstance().getEconomy();
    private Map<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            EconomySign sign = EconomySign.getByBlock(block);

            if (sign != null) {
                event.setCancelled(true);

                if (hasCooldown(player)) {
                    return;
                }

                cooldown.put(player.getUniqueId(), System.currentTimeMillis());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getWorld().equals(block.getWorld()) && player.getLocation().distance(block.getLocation()) < 30) {
                            player.sendSignChange(block.getLocation(), new String[]{sign.getSign().getLine(0), sign.getSign().getLine(1), sign.getSign().getLine(2), sign.getSign().getLine(3)});
                        }
                    }
                }.runTaskLater(main, 30);

                if (sign.getType() == EconomySignType.BUY) {

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(main.getLangFile().getString("ECONOMY.SIGN.ERROR.INVENTORY_FULL"));
                        player.sendSignChange(block.getLocation(), new String[]{main.getLangFile().getString("ECONOMY.SIGN.INVENTORY_FULL"), sign.getSign().getLine(1), sign.getSign().getLine(2), sign.getSign().getLine(3)});
                        return;
                    }

                    if (economy.getBalance(player) < sign.getPrice()) {
                        player.sendMessage(main.getLangFile().getString("ECONOMY.SIGN.ERROR.NOT_ENOUGH_MONEY"));
                        player.sendSignChange(block.getLocation(), new String[]{main.getLangFile().getString("ECONOMY.SIGN.CANNOT_AFFORD"), sign.getSign().getLine(1), sign.getSign().getLine(2), sign.getSign().getLine(3)});
                        return;
                    }

                    economy.withdrawPlayer(player, sign.getPrice());
                    player.getInventory().addItem(new ItemBuilder(sign.getItemStack()).amount(sign.getAmount()).build());
                    player.sendSignChange(block.getLocation(), new String[]{main.getLangFile().getString("ECONOMY.SIGN.BOUGHT_TEXT"), sign.getSign().getLine(1), sign.getSign().getLine(2), sign.getSign().getLine(3)});
                    player.updateInventory();
                    return;
                }

                if (sign.getType() == EconomySignType.SELL) {

                    if (!(player.getInventory().contains(sign.getItemStack().getType()))) {
                        player.sendMessage(main.getLangFile().getString("ECONOMY.SIGN.ERROR.NOT_ENOUGH"));
                        player.sendSignChange(block.getLocation(), new String[]{main.getLangFile().getString("ECONOMY.SIGN.NOT_ENOUGH_TOP"), sign.getSign().getLine(1), main.getLangFile().getString("ECONOMY.SIGN.NOT_ENOUGH_BOTTOM"), ""});
                        return;
                    }

                    double pricePerItem = (((double)sign.getPrice()) / ((double)sign.getAmount()));

                    int toSell = 0;
                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (itemStack == null) continue;
                        if (toSell >= sign.getAmount()) break;
                        if (toSell + itemStack.getAmount() >= sign.getAmount()) { toSell = sign.getAmount(); break; }
                        toSell += itemStack.getAmount();
                    }

                    player.sendSignChange(block.getLocation(), new String[]{main.getLangFile().getString("ECONOMY.SIGN.SOLD"), sign.getSign().getLine(1), sign.getSign().getLine(2), sign.getSign().getLine(3)});
                    player.getInventory().removeItem(new ItemBuilder(sign.getItemStack()).amount(toSell).build());
                    economy.depositPlayer(player, (int) Math.floor(pricePerItem * toSell));
                    player.updateInventory();
                }
            }

        }
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("economy.sign.create")) {
            String[] lines = event.getLines();
            String typeLine = lines[0];

            EconomySignType type;
            if (typeLine.equalsIgnoreCase("[Buy]")) {
                type = EconomySignType.BUY;
            } else if (typeLine.equalsIgnoreCase("[Sell]")) {
                type = EconomySignType.SELL;
            } else {
                return;
            }

            String itemStackName;
            try {
                if (lines[1].equalsIgnoreCase("crowbar")) {
                    itemStackName = "Crowbar";
                } else if (lines[1].equalsIgnoreCase("portal frame")) {
                    itemStackName = "Portal Frame";
                } else if (lines[1].equalsIgnoreCase("cow egg")) {
                    itemStackName = "Cow Egg";
                } else {
                    itemStackName = ItemNames.lookup(new ItemStack(Material.valueOf(lines[1].replace(" ", "_").toUpperCase())));
                }

            } catch (Exception ex) {
                player.sendMessage(ChatColor.RED + "Invalid material.");
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(lines[2].replaceAll("[^0-9]", ""));
            } catch (Exception ex) {
                player.sendMessage(ChatColor.RED + "Invalid quantity.");
                return;
            }

            if (amount <= 0) {
                player.sendMessage(ChatColor.RED + "Amount must be greater than 0.");
                return;
            }

            int price;
            try {
                price = Integer.parseInt(lines[3].replaceAll("[^0-9]", ""));
            } catch (Exception ex) {
                player.sendMessage(ChatColor.RED + "Invalid price.");
                return;
            }

            if (price <= 0) {
                player.sendMessage(ChatColor.RED + "Price must be greater than $0.");
                return;
            }

            int pos = 0;
            for (String line : main.getLangFile().getStringList("ECONOMY.SIGN." + type.name() + "_TEXT")) {
                event.setLine(pos, line.replace("%ITEM%", itemStackName).replace("%AMOUNT%", amount + "").replace("%PRICE%", price + ""));
                pos++;
            }
        }
    }

    public boolean hasCooldown(Player player) {
        return cooldown.containsKey(player.getUniqueId()) && ((System.currentTimeMillis() - cooldown.get(player.getUniqueId())) < 1500);
    }



}
