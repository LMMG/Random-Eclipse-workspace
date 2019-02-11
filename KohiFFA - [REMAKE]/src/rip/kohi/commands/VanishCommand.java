package rip.kohi.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.kohi.events.VanishEvent;
import rip.kohi.utils.C;
import rip.kohi.utils.Notify;

/**
 * Created by Owner on 31/08/2017.
 */
public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!commandSender.hasPermission("kohiffa.vanish")) {
            commandSender.sendMessage(ChatColor.RED + "You can't use this command");
            return true;
        }
        Player player = (Player) commandSender;
        boolean vanished = VanishEvent.toggleVanish(player);

        String colour = (vanished ? "&a" : "&c");
        player.sendMessage(C.c(colour + "You are " + (vanished ? "now vanished" : "no longer vanished")));
        Notify.alert(commandSender, "Set their visibility to " + (vanished ? "vanished" : "visible"));
        return false;
    }
}
