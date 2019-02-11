package net.nersa.lobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import net.nersa.lobby.Lobby;
import net.nersa.lobby.listeners.player.PlayerListeners;
import net.nersa.lobby.manager.CommandManager;
import net.nersa.lobby.selector.ServerSelector;
import net.nersa.lobby.server.PlayerEvents;

public class ListenerManager {

    public ListenerManager(Lobby bolt) {
        PluginManager manager = Bukkit.getServer().getPluginManager();

        manager.registerEvents(new PlayerEvents(), bolt);
        manager.registerEvents(new ServerSelector(), bolt);
        manager.registerEvents(new CommandManager(), bolt);
        manager.registerEvents(new PlayerListeners(), bolt);
    }
}
