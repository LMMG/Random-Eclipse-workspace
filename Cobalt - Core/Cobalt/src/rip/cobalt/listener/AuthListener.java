package rip.cobalt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.commands.staff.Authentication;

public class AuthListener implements Listener {
	
	@EventHandler
	public void Move(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(Authentication.AuthenticationPhase.contains(p.getUniqueId())) {
			e.setTo(e.getFrom());
			p.sendMessage(ChatColor.RED + "Please Authenticate to be able to move!");
		}
	}
}
