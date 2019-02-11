package rip.kohi.commands;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.kohi.KohiFFA;

/**
 * Created by Owner on 31/08/2017.
 */
public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("stats")) {
            if (!(commandSender instanceof Player)) {
                return true;
            }
            Player player = (Player) commandSender;
            if (!player.hasPermission("kohiffa.stats")) {
                player.sendMessage("NAY");
                return true;
            }
            player.sendMessage(ChatColor.RED + "Kills: " + player.getStatistic(Statistic.PLAYER_KILLS));
            player.sendMessage(ChatColor.RED + "Deaths: " + player.getStatistic(Statistic.DEATHS));
        }
        return false;
    }
}
