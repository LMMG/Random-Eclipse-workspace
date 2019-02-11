package gg.vexus.commands.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.util.chat.Lang;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class FlyCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player.");
                return true;
            }
            if (!Essentials.hasPermission(sender, "fly")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                if (!p.getAllowFlight()) {
                    p.setAllowFlight(true);
                    p.sendMessage(ChatColor.GREEN + "You can now fly!");
                }
                else {
                    p.setAllowFlight(false);
                    p.sendMessage(ChatColor.RED + "You can no longer fly!");
                }
                return true;
            }
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    p.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                if (target.getAllowFlight()) {
                    target.setAllowFlight(false);
                    target.sendMessage(ChatColor.RED + "You can no longer fly!");
                    p.sendMessage(ChatColor.RED + target.getName() + " can no longer fly!");
                    return true;
                }
                target.setAllowFlight(true);
                target.sendMessage(ChatColor.GREEN + "You can now fly!");
                p.sendMessage(ChatColor.GREEN + target.getName() + " can now fly!");
            }
        }
        return false;
    }
}
