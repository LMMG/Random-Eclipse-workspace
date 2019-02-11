package com.hcempire.horus.misc.commands;

import com.hcempire.horus.Horus;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class HelpCommand implements Listener {

    public HelpCommand() {
        Bukkit.getPluginManager().registerEvents(this, Horus.getInstance());
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/help")) {

            if (!event.getMessage().equalsIgnoreCase("/help") && event.getMessage().toCharArray()[5] != ' ') {
                return;
            }

            event.getPlayer().sendMessage(Horus.getInstance().getLangFile().getStringList("HELP").toArray(new String[Horus.getInstance().getLangFile().getStringList("HELP").size()]));
            event.setCancelled(true);
        }
    }
}
