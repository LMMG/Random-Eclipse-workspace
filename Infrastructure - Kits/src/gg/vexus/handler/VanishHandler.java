package gg.vexus.handler;

import gg.vexus.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.spigotmc.CustomTimingsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VanishHandler implements Listener {

    private static final CustomTimingsHandler handler = new CustomTimingsHandler("dCommon (vanish)");

    private static final List<UUID> VANISHED = new ArrayList<>();

    public static boolean isVanished(Player player) {
        return VANISHED.contains(player.getUniqueId());
    }

    public static void vanish(Player player) {
        if (isVanished(player))
            return;
        VANISHED.add(player.getUniqueId());

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.hasPermission("common.vanish")) {
                p.hidePlayer(player);
            } else {
                p.showPlayer(player);
                player.showPlayer(p);
            }
        }

        if (!player.hasMetadata("invisible")) {
            player.setMetadata("invisible", new FixedMetadataValue(Core.getCore(), true));
        }

    }

    public static void unvanish(Player player) {
        if (!isVanished(player))
            return;
        VANISHED.remove(player.getUniqueId());

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.showPlayer(player);
        }

        if (player.hasMetadata("invisible")) {
            player.removeMetadata("invisible", Core.getCore());
        }

    }

    public static boolean toggleVanish(Player player) {
        if (isVanished(player)) {
            unvanish(player);
        } else
            vanish(player);

        return isVanished(player);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("common.vanish.see")) {
            for (UUID uuid : VANISHED) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline())
                    p.hidePlayer(player);
            }
        }
    }

}