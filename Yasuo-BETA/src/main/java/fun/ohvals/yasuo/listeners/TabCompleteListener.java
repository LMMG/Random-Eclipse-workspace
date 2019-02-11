package fun.ohvals.yasuo.listeners;

import fun.ohvals.yasuo.Yasuo;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class TabCompleteListener implements Listener {

    private Yasuo yasuo = Yasuo.getYasuo();

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (yasuo.getProxy().getPlayer(e.getCursor()) == null) {
            e.setCancelled(true);
        }
    }
}
