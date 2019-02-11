package us.kirai.bunkers.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastUtils
{
    public static void broadcastToPerm(final String msg, final String permission) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(permission) || p.isOp()) {
                p.sendMessage(msg);
            }
        }
    }
}
