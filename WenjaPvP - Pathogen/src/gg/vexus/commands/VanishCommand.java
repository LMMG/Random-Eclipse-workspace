package gg.vexus.commands;

import gg.vexus.handler.VanishHandler;
import gg.vexus.utils.Alerts;
import gg.vexus.utils.C;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("common.vanish")) {
            sender.sendMessage(ChatColor.RED + "You can't use this command!");
            return true;
        }
        Player p = (Player) sender;
        boolean vanished = VanishHandler.toggleVanish(p);

        String colour = (vanished ? "&a" : "&c");
        p.sendMessage(C.c(colour + "You are " + (vanished ? "now vanished" : "no longer vanished")));
        Alerts.alert(sender, "Set their visibility to " + (vanished ? "vanished" : "visible"));
        return true;
    }
}