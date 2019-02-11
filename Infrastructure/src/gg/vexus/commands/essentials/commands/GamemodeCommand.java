package gg.vexus.commands.essentials.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.Alerts;
import gg.vexus.utils.StringRegister;

public class GamemodeCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player!");
                return true;
            }
            if (!Essentials.hasPermission(sender, "gamemode")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /gamemode <creative|survival|adventure>");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    p.sendMessage(ChatColor.BLUE + "You are now in creative mode!");
                    p.setGameMode(GameMode.CREATIVE);
                    return true;
                }
                if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    p.sendMessage(ChatColor.BLUE + "You are now in survival mode!");
                    p.setGameMode(GameMode.SURVIVAL);
                    return true;
                }
                if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    p.sendMessage(ChatColor.BLUE + "You are now in adventure mode!");
                    p.setGameMode(GameMode.ADVENTURE);
                    return true;
                }
                p.sendMessage(ChatColor.RED + args[0] + " is not a valid gamemode.");
                return true;
            }
            else if (args.length == 2) {
                if (!Essentials.hasPermission(sender, "gamemode.others")) {
                    sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                    return true;
                }
                if (Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[1]);
                if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                    target.sendMessage(ChatColor.BLUE + "You are now in creative mode!");
                    target.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(ChatColor.BLUE + target.getName() + " is now in creative mode!");
                    Alerts.alert(sender, " Set Their gamemode to Creative");
                    return true;
                }
                if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
                    target.sendMessage(ChatColor.BLUE + "You are now in survival mode!");
                    target.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(ChatColor.BLUE + target.getName() + " is now in survival mode!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
                    target.sendMessage(ChatColor.BLUE + "You are now in adventure mode!");
                    target.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(ChatColor.BLUE + target.getName() + " is now in adventure mode!");
                    return true;
                }
                p.sendMessage(ChatColor.RED + args[0] + " is not a valid gamemode.");
                return true;
            }
        }
        return false;
    }
}
