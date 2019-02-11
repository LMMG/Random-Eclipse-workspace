package rip.kohi.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import rip.kohi.KohiFFA;
import rip.kohi.events.StaffModeEvent;
import rip.kohi.events.VanishEvent;

import java.util.ArrayList;

/**
 * Created by Owner on 31/08/2017.
 */
public class StaffModeCommand implements CommandExecutor {

    public static ArrayList<String> staffMode = new ArrayList<String>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("staffmode")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "NAY");
                return true;
            }
            Player player = (Player) commandSender;

            if (!player.hasPermission("kohiffa.staffmode")) {
                player.sendMessage(ChatColor.RED + "NAY");
            }

            if (args.length == 0) {
                if (staffMode.contains(String.valueOf(player.getUniqueId()))) {
                    staffMode.remove(String.valueOf(player.getUniqueId()));
                    VanishEvent.unvanish(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', KohiFFA.PREFIX + "&7 has now been removed from staffmode!"));
                    StaffModeEvent.remove(player);
                    return true;
                }
                staffMode.add(String.valueOf(player.getUniqueId()));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', KohiFFA.PREFIX + "&7 has now been added to staffmode!"));
                VanishEvent.vanish(player);
                StaffModeEvent.put(player);
                return true;
            }
            player.sendMessage(ChatColor.RED + "This is a toggle command not a arguement command!");
            return true;
        }
        return false;
    }
}
