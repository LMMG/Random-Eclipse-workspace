package org.hcgames.icefyre.party;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.hcgames.icefyre.Icefyre;

public class PartyListeners implements Listener {

    private static Icefyre main = Icefyre.getInstance();

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("@")) {
            Player player = event.getPlayer();
            Party party = Party.getByPlayer(player);
            if (party != null) {
                party.sendMessage(main.getLangFile().getString("PARTY.CHAT." + (party.isLeader(player) ? "LEADER" : "MEMBER")).replace("%PLAYER%", player.getName()).replace("%MESSAGE%", event.getMessage().substring(1, event.getMessage().length())));
                event.setCancelled(true);
            }
        }
    }

}
