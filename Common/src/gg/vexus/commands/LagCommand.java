package gg.vexus.commands;

import gg.vexus.utils.C;
import gg.vexus.utils.SystemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Owner on 02/07/2017.
 */
public class LagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender.hasPermission("common.lag")) {
            sender.sendMessage(C.c("&6Allocated Memory: &a" + SystemUtils.getMaximumMemory() + "MB"));
            sender.sendMessage(C.c("&6Total Memory: &a" + SystemUtils.getTotalMemory() + "MB"));
            sender.sendMessage(C.c("&6Free Memory: &a" + SystemUtils.getFreeMemory() + "MB"));
        }
        return false;
    }
}
