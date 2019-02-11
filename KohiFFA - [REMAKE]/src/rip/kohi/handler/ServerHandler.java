package rip.kohi.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import rip.kohi.utils.C;
import rip.kohi.utils.Messages;
import rip.kohi.utils.SystemUtil;

/**
 * Created by Owner on 31/08/2017.
 */
public class ServerHandler implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("serverhandler")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Messages.CONS);
                return true;
            }
            Player player = (Player) commandSender;
            if (!player.hasPermission("kohiffa.serverhandler")) {
                player.sendMessage(Messages.PERM);
                return true;
            }
            if(args.length == 0) {
                player.sendMessage(C.c("&6&lPlayers: " + Bukkit.getOnlinePlayers().size()));
                player.sendMessage(C.c("&6Allocated Memory: &a" + SystemUtil.getMaximumMemory() + "MB"));
                player.sendMessage(C.c("&6Total Memory: &a" + SystemUtil.getTotalMemory() + "MB"));
                player.sendMessage(C.c("&6Free Memory: &a" + SystemUtil.getFreeMemory() + "MB"));
            }
        }
        return false;
    }

    @EventHandler
    public void onJoinRank(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if ((p.hasPermission("command.allow.donator"))) {
            if (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers()) {
                e.allow();
            }
        } else if ((!p.hasPermission("command.alertssee"))
                && (Bukkit.getOnlinePlayers().size() == Bukkit.getMaxPlayers())) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.translateAlternateColorCodes('&',
                    "&cThe server is currently full, buy a reserved slot @ &lstore.kohiffa.net"));
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("command.alertssee")) {
                    staff.sendMessage(ChatColor.RED
                            + "You should probably increase the slots as there are people trying to log in, but the server is full");
                }
            }
        }
    }
}
