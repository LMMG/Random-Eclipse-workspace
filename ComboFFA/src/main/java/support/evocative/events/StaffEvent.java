package support.evocative.events;

import javafx.print.PageLayout;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

/**
 * Created by Owner on 01/09/2017.
 */
public class StaffEvent implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(player.hasPermission("comboffa.staffalert")) {
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInitialising Staff Account!"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aStaff Account Enabled!"));
        }
    }
}
