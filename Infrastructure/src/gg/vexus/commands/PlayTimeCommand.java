package gg.vexus.commands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jason on 19/07/2016.
 */
public class PlayTimeCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command playtime, String commandLabel, String[] args)
    {
        Player player = (Player)sender;
        if ((playtime.getName().equalsIgnoreCase("playtime")) && ((sender instanceof Player)) &&
                (args.length > 1))
        {
            sender.sendMessage("§cUsage: /playtime <player>");
            return true;
        }
        if (args.length == 0)
        {
            player.sendMessage("§cUsage: /playtime <player>");
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if ((args.length == 1) &&
                (target == null))
        {
            player.sendMessage("§cPlayer not found.");
            return true;
        }
        int statistic = target.getStatistic(Statistic.PLAY_ONE_TICK);
        int days = statistic / 1728000;
        int hours = statistic % 1728000 / 20 / 3600;
        int minutes = statistic % 72000 / 20 / 60;
        int seconds = statistic % 1200 / 20;
        player.sendMessage("§7§m--------------------------------------------------");
        player.sendMessage("                       §7Playtime of " + target.getDisplayName() + "§7:");
        player.sendMessage("                  §f" + days + " §7days " + "§f" + hours + " §7hours " + "§f" + minutes + " §7minutes " + "§f" + seconds + " §7seconds.");
        player.sendMessage("§7§m--------------------------------------------------");
        return true;
    }
}