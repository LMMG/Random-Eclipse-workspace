package net.okaru.lobby.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.okaru.lobby.Lobby;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerMobEvent implements Listener {
	
	@EventHandler
	public void onPlayerMobClickEvent(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.IRON_GOLEM) {
			ServerSelectorEvent.openServerSelectorMenu(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerMobClickEvent1(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.VILLAGER) {
			event.getPlayer().sendMessage(ChatColor.RED + "This server is currency offline, please check back later.");
		}
	}
	
	@EventHandler
	public void onPlayerMobClickEvent2(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.ZOMBIE) {
            event.getPlayer().sendMessage(ChatColor.WHITE + "We are now attempting to connect you to " + ChatColor.GOLD + "Kits" + ChatColor.WHITE + ".");
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try
            {
            	out.writeUTF("Connect");
            	out.writeUTF("Kits");
            }
            catch (IOException error)
            {
            error.printStackTrace();	
            }
            event.getPlayer().sendPluginMessage(Lobby.getInstance(), "BungeeCord", b.toByteArray());
            }
		}
}
