package net.rifthq.typa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.managers.MySQL;
import net.rifthq.typa.utils.Lists;

public class PlayerLoginListeners implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStaffJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!event.getPlayer().hasPermission("typa.force")) {
			return;
		}
		if (MySQL.dataExists(event.getPlayer().getUniqueId(), event.getPlayer())) {
			Lists.toLogin.add(player.getName());
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 9999));
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 9999));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease authenticate via /login!"));
			return;
		}
		if (!MySQL.dataExists(event.getPlayer().getUniqueId(), event.getPlayer())) {
			Lists.toLogin.add(player.getName());
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 9999));
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 9999));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cIt seems your a new staffmember, please register your account to our authtication system via /register"));
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStaffIPJoin(PlayerJoinEvent event) {
		
	}
	
}