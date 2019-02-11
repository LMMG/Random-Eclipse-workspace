package gg.mist.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
public class StaffActionListener implements Listener
{
	
    @EventHandler
    public void onJoin(ServerConnectEvent e) {
        if (!e.getPlayer().hasPermission("mod")) {
            return;
        }
        for (ProxiedPlayer player : e.getTarget().getPlayers()) {
            if (player.hasPermission("mod")) {
            	final ProxiedPlayer commandSender = e.getPlayer();
            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&e&l" + player.getServer().getInfo().getName() + "&7) &a" + player.getName() + "joined the server."));
            }
        }
    }
}
