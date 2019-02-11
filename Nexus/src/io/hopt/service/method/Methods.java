package io.hopt.service.method;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Owner on 09/06/2017.
 */
public class Methods {

    public static void onChangelog(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 *** &eChangelog&7 ***"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7* &eAdded new scoreboardAPI"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7* &eWorking on new server selector system"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7* &eHub utils / developers API is being worked on"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7* &eAdding listeners like No Break etc"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7* &eAdded No-Chat in hub"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7* &eAdded Alot more listeners to make it more solid"));
    }
}
