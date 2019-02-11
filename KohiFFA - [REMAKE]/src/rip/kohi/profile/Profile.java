package rip.kohi.profile;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Owner on 31/08/2017.
 */
public class Profile implements Listener {

    @EventHandler
    public void onProfileLoad(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.sendMessage(ChatColor.GREEN + "Your profile has loaded!");
    }
}
