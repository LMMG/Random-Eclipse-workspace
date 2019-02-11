package fun.ohvals.yasuo.listeners;

import fun.ohvals.yasuo.Yasuo;
import fun.ohvals.yasuo.profile.Profile;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class ProfileCheckListener implements Listener {

    private Yasuo yasuo = Yasuo.getYasuo();

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (yasuo.getProfileManager().getByUuid(player.getUniqueId()) == null) {
            new Profile(player.getUniqueId());
        }
        Profile profile = yasuo.getProfileManager().getByUuid(player.getUniqueId());
        profile.addIp(player.getAddress().getHostString());
        profile.addName(player.getName());
        profile.save();
    }

}
