package gg.vexus.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Owner on 03/09/2017.
 */
public class PlayerListeners implements Listener {


    @EventHandler
    public void onPlayerList(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lWARNING &7Butterfly clicking is bannable!"));
    }

    @EventHandler
    public void onPlayerJoim(PlayerJoinEvent e) {
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
}
