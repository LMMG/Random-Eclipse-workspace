package com.hcempire.horus.crate.command.subcommand;

import com.alexandeh.ekko.utils.ItemBuilder;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.crate.Crate;
import com.hcempire.horus.util.HorusCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class CrateKeyCommand extends HorusCommand {
    @Command(name = "crate.key", aliases = {"key"}, permission = "crate.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length <= 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /crate key <name> <amount> <player>");
            return;
        }

        Crate crate = Crate.getByName(args[0]);
        if (crate == null) {
            sender.sendMessage(ChatColor.RED + "A crate named '" + args[0] + "' does not exist.");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (Exception exception) {
            sender.sendMessage(ChatColor.RED + "Invalid amount");
            return;
        }

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Invalid amount");
            return;
        }

        Player toGive;
        if (args.length == 2) {
            if (sender instanceof Player) {
                toGive = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /crate key <name> <amount> <player>");
                return;
            }
        } else {
            toGive = Bukkit.getPlayer(args[2]);
        }

        if (toGive == null) {
            sender.sendMessage(ChatColor.RED + "Invalid player.");
            return;
        }


        sender.sendMessage(ChatColor.GOLD + "You have successfully given " + ChatColor.YELLOW + amount + ChatColor.GOLD + " crate key" + (amount == 1 ? "" : "s") + " to " + ChatColor.YELLOW + toGive.getName() + ChatColor.GOLD + ".");
        toGive.getInventory().addItem(crate.getKey(amount));
    }
}
