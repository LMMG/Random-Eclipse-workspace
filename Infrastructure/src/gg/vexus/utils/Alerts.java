package gg.vexus.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Alerts {

    private static final String PREFIX = "&7&o[{SENDER}: ";
    private static final String SUFFIX = "&7&o]";

    public static void alert(CommandSender sender, String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("common.sensitivealerts")) {
//				if(sender.isOp() && !p.isOp()) continue;
//

                if (sender instanceof Player) {
                    if (((Player) sender).getUniqueId().equals(p.getUniqueId())) continue;
                }
                p.sendMessage(C.c(PREFIX.replace("{SENDER}", (sender instanceof Player ? sender.getName() : "System")) + message + SUFFIX));
            }
        }
    }

    public static void alert(String sender, String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("common.sensitivealerts")) {
//				if(sender.isOp() && !p.isOp()) continue;

                p.sendMessage(C.c(PREFIX.replace("{SENDER}", sender) + message + SUFFIX));
            }
        }
    }
}
