package fun.ohvals.yasuo.listeners;

import fun.ohvals.yasuo.Yasuo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class StaffActionListener implements Listener {

    private Yasuo yasuo = Yasuo.getYasuo();

    @EventHandler
    public void onJoin(ServerConnectEvent e) {
        if (!e.getPlayer().hasPermission("yasuo.staffjoin")) {
            return;
        }
        for (ProxiedPlayer player : e.getTarget().getPlayers()) {
            if (player.hasPermission("yasuo.staffjoin")) {
                player.sendMessage(yasuo.getLangConfig().getString("STAFFJOIN_FORMAT").replace("%PLAYER%", e.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onLeave(ServerDisconnectEvent e) {
        if (!e.getPlayer().hasPermission("yasuo.staffleave")) {
            return;
        }
        for (ProxiedPlayer player : e.getTarget().getPlayers()) {
            if (player.hasPermission("yasuo.staffleave")) {
                player.sendMessage(yasuo.getLangConfig().getString("STAFFLEAVE_FORMAT").replace("%PLAYER%", e.getPlayer().getName()));
            }
        }
    }

}
