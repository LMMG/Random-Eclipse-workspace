package net.okaru.queue.queue;

import net.okaru.queue.oQueue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueListeners
implements Listener {
    private oQueue main;

    public QueueListeners(oQueue main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Queue queue = Queue.getByPlayer(event.getPlayer());
        if (queue != null) {
            this.main.getPublisher().write(queue.getName() + ";returned;" + player.getUniqueId().toString());
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        new BukkitRunnable(){

            @Override
			public void run() {
                Queue queue = Queue.getByPlayer(event.getPlayer());
                if (queue != null) {
                    queue.removePlayer(event.getPlayer(), false);
                }
            }
        }.runTaskAsynchronously(this.main);
    }

}

