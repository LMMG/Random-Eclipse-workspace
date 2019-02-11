package fun.ohvals.yasuo.listeners;

import fun.ohvals.yasuo.Yasuo;
import fun.ohvals.yasuo.commands.StaffChatCommand;
import fun.ohvals.yasuo.profile.Profile;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class StaffChatListener implements Listener {

    private Yasuo yasuo = Yasuo.getYasuo();

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        Profile profile = yasuo.getProfileManager().getByUuid(player.getUniqueId());
        if (!e.isCommand()) {
            if (profile.isUsingStaffchat()) {
                if (profile.isInStaffchat()) {
                    StaffChatCommand.sendStaffMessage(player, e.getMessage());
                    e.setCancelled(true);
                } else {
                    player.sendMessage(yasuo.getLangConfig().getString("STAFFCHAT_NOT_IN"));
                }
            }
        }
    }
}
