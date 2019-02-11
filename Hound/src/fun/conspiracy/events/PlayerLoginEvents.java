package fun.conspiracy.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fun.conspiracy.listener.ServerSelector;

public class PlayerLoginEvents implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		

        player.getInventory().clear();
        player.setWalkSpeed(0.5F);
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().setItem(4, ServerSelector.selector);
	}

}
