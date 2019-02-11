package net.wenjapvp.kohisg.listeners;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChatFormat implements Listener {

    @EventHandler
    public void onAysnc(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!player.isOp()) {
            e.setFormat(
                    ChatColor.translateAlternateColorCodes('&', "&A" + player.getName() + "&7: &F" + e.getMessage()));
        }

        if (player.isOp()) {
            e.setFormat(ChatColor.translateAlternateColorCodes('&',
                    "&4" + player.getName() + "&7: " + ChatColor.GREEN + e.getMessage()));
        }
    }
}
