package net.nersa.kitmap.listener;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.command.ChestCommand;
import net.nersa.player.KillReward;
import net.nersa.utils.ItemBuilder;
import net.nersa.utils.MessageUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class KillstreakListener implements Listener {

	public static KillReward[] killstreakRewards;

	public static HashMap<UUID, Integer> kills = new HashMap<>();

	public static void setupItems() {
		killstreakRewards = new KillReward[11];

		killstreakRewards[0] = new KillReward(new ItemBuilder(Material.GOLDEN_APPLE, ChatColor.YELLOW + "Golden Apple", 3, "").getItem());
		killstreakRewards[1] = new KillReward(new ItemBuilder(Material.POTION, ChatColor.WHITE + "Splash Potion of Poison", 1, (short) 16452).getItem(), new ItemBuilder(Material.POTION, ChatColor.WHITE + "Splash Potion of Slowness", 1, (short) 16458).getItem());
		killstreakRewards[2] = new KillReward(null);
		killstreakRewards[3] = new KillReward(new ItemBuilder(Material.POTION, ChatColor.WHITE + "Splash Potion of Healing", 5, (short) 16421).getItem());
		killstreakRewards[4] = new KillReward(null);
		killstreakRewards[5] = new KillReward(null);

		ItemStack fireSword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta fireMeta = fireSword.getItemMeta();
		fireMeta.setDisplayName("&c&lKillstreaek Fire");
		fireSword.setItemMeta(fireMeta);
		fireSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		fireSword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

		killstreakRewards[6] = new KillReward(new ItemBuilder(Material.DIAMOND_SWORD, ChatColor.RED + "Killstreak Fire").getItem());
		killstreakRewards[7] = new KillReward(null);

		ItemStack sharpSword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta sharpMeta = sharpSword.getItemMeta();
		sharpMeta.setDisplayName("&c&lKillstreak Sharp");
		sharpSword.setItemMeta(fireMeta);
		sharpSword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		sharpSword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

		killstreakRewards[8] = new KillReward(sharpSword);
		killstreakRewards[9] = new KillReward(null);
		killstreakRewards[10] = new KillReward(new ItemBuilder(Material.getMaterial(322), ChatColor.YELLOW + "God Apple", 1, (short) 1).getItem());
	}

	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (kills.containsKey(e.getPlayer().getUniqueId())) {
			kills.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void kill(PlayerDeathEvent e) {
		if (e.getEntity() == null) {
			return;
		}

		if (e.getEntity().getKiller() == null) {
			return;
		}

		if (!(e.getEntity() instanceof Player) || !(e.getEntity().getKiller() instanceof Player)) {
			return;
		}

		if (kills.containsKey(e.getEntity().getUniqueId()) && kills.get(e.getEntity().getUniqueId()) != null && kills.get(e.getEntity().getUniqueId()) >= 3) {
			Bukkit.getServer().broadcastMessage(MessageUtils.color("&f" + e.getEntity().getKiller().getName() + " &ehas ended &f" + e.getEntity().getName() + "&e's killstreak of &c" + kills.get(e.getEntity().getUniqueId()) + "&e!"));
			kills.remove(e.getEntity().getUniqueId());
		}

		if (kills.containsKey(e.getEntity().getKiller().getUniqueId())) {
			kills.put(e.getEntity().getKiller().getUniqueId(), kills.get(e.getEntity().getKiller().getUniqueId()) + 1);
		} else {
			kills.put(e.getEntity().getKiller().getUniqueId(), 1);
		}

		if (kills.get(e.getEntity().getKiller().getUniqueId()) % 5 == 0) {
			Bukkit.getServer().broadcastMessage(MessageUtils.color("&f" + e.getEntity().getKiller().getName() + "&e has gotten a killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e!"));
			if (killstreakRewards[(kills.get(e.getEntity().getKiller().getUniqueId()) / 5)] != null) {

				if ((killstreakRewards[(kills.get(e.getEntity().getKiller().getUniqueId()) / 5)].getItems()) == null) {
					int ks = kills.get(e.getEntity().getKiller().getUniqueId());
					if (ks == 10) {
						e.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5, 2));
						e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded a &c" + "Strength 2 potion effect for 5 seconds" + "&e."));
					} else if (ks == 20) {
						HCF.getInstance().getEconomyManager().addBalance(e.getEntity().getKiller().getUniqueId(), 15000);
						e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded &c" + "15.000 tokens" + "&e."));
					} else if (ks == 35) {
						HCF.getInstance().getEconomyManager().addBalance(e.getEntity().getKiller().getUniqueId(), 25000);
						e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded &c" + "25.000 tokens" + "&e."));
					} else if (ks == 45) {
						e.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3, 5));
						e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded a &c" + "Strength 5 potion effect for 3 seconds" + "&e."));
					} else if (ks == 25) {
						e.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3, 3));
						e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded a &c" + "Strength 3 potion effect for 3 seconds" + "&e."));
					}
					return;
				}

				boolean empty = false;

				for (ItemStack i : e.getEntity().getKiller().getInventory().getContents()) {
					if (i == null) {
						empty = true;
						break;
					}
				}

				if (empty) {
					e.getEntity().getKiller().getInventory().addItem(killstreakRewards[(kills.get(e.getEntity().getKiller().getUniqueId()) / 5)].getItems());
				} else {
					if (ChestCommand.chests.containsKey(e.getEntity().getKiller().getUniqueId().toString())) {
						ChestCommand.chests.get(e.getEntity().getKiller().getUniqueId().toString()).addItem(killstreakRewards[(kills.get(e.getEntity().getKiller().getUniqueId()) / 5)].getItems());
					} else {
						Inventory inv = ChestCommand.generateInventory(e.getEntity().getKiller());
						inv.addItem(killstreakRewards[(kills.get(e.getEntity().getKiller().getUniqueId()) / 5)].getItems());
						ChestCommand.chests.put(e.getEntity().getKiller().getUniqueId().toString(), inv);
					}
					e.getEntity().getKiller().sendMessage(MessageUtils.color("&eYour killstreak reward(s) have been put in your vault as your inventory is full."));
				}
				for (ItemStack ss : killstreakRewards[(kills.get(e.getEntity().getKiller().getUniqueId()) / 5)].getItems()) {
					e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded a &c" + ss.getItemMeta().getDisplayName() + "&e."));
				}
			}
		} else if (kills.get(e.getEntity().getKiller().getUniqueId()) == 3) {
			Bukkit.getServer().broadcastMessage(MessageUtils.color("&f" + e.getEntity().getKiller().getName() + "&e has gotten a killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e!"));
			if (killstreakRewards[0] != null) {
				boolean empty = false;

				for (ItemStack i : e.getEntity().getKiller().getInventory().getContents()) {
					if (i == null) {
						empty = true;
						break;
					}
				}

				if (empty) {
					e.getEntity().getKiller().getInventory().addItem(killstreakRewards[0].getItems());
				} else {
					if (ChestCommand.chests.containsKey(e.getEntity().getKiller().getUniqueId().toString())) {
						ChestCommand.chests.get(e.getEntity().getKiller().getUniqueId().toString()).addItem(killstreakRewards[0].getItems());
					} else {
						Inventory inv = ChestCommand.generateInventory(e.getEntity().getKiller());
						inv.addItem(killstreakRewards[0].getItems());
						ChestCommand.chests.put(e.getEntity().getKiller().getUniqueId().toString(), inv);
					}

					e.getEntity().getKiller().sendMessage(MessageUtils.color("&eYour killstreak reward(s) have been put in your vault as your inventory is full."));
				}
				for (ItemStack ss : killstreakRewards[0].getItems()) {
					e.getEntity().getKiller().sendMessage(MessageUtils.color("&eFor your killstreak of &c" + kills.get(e.getEntity().getKiller().getUniqueId()) + "&e you have been rewarded a &c" + ss.getItemMeta().getDisplayName() + "&e."));
				}
			}
		}
	}

}