package support.evocative.profile.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Owner on 01/09/2017.
 */
public class ProfileListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.GREEN + "Loading Your Profile!");

        player.sendMessage(ChatColor.YELLOW + "Remember this server is in development so there may be bugs!");
    }
}
