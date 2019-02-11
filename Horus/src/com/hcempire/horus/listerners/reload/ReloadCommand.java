package com.hcempire.horus.listerners.reload;

import com.hcempire.horus.Horus;
import com.hcempire.horus.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Álvaro Mariano on 17/04/2017.
 */
public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("horus")) {
            if (!commandSender.hasPermission("horus.command.horus")) {
                commandSender.sendMessage(Color.translate("&cYou don't have permission."));
            } else {
                if (strings.length == 0) {
                    commandSender.sendMessage(Color.translate("&7&m---+------------------+---"));
                    commandSender.sendMessage(Color.translate("&7 * &eCoded by &aBizarreAlex"));
                    commandSender.sendMessage(Color.translate("&7 * &eModified by &aJavaEE"));
                    commandSender.sendMessage(Color.translate("&7&m---+------------------+---"));
                    return true;
                }
                if (strings.length >= 1) {
                    if (strings[0].equalsIgnoreCase("reload")) {
                        Horus.getInstance().reloadConfig();
                        commandSender.sendMessage(Color.translate("&eYou have reloaded the configuration file."));
                        return true;
                    }
                    if (strings[0].equalsIgnoreCase("save")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                        commandSender.sendMessage(Color.translate("&eYou have saved the configuration file and all the worlds."));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
