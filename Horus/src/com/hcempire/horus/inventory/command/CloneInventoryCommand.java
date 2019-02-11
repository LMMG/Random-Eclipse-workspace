package com.hcempire.horus.inventory.command;

import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.profile.Profile;
import com.hcempire.horus.util.HorusCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CloneInventoryCommand extends HorusCommand {

    @Command(name = "cloneinventory", aliases = {"cloneinv", "copyinv", "copyinventory", "cpfrom"}, permission = "inventory.clone")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        Player toClone;
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/" + command.getLabel() + " <player>");
            return;
        } else {
            toClone = Bukkit.getPlayer(StringUtils.join(args));
            if (toClone == null) {
                player.sendMessage(ChatColor.RED + "No player named '" + StringUtils.join(args) + "' found.");
                return;
            }
        }

        player.getInventory().setContents(toClone.getInventory().getContents());
        player.getInventory().setArmorContents(toClone.getInventory().getArmorContents());
        player.sendMessage(ChatColor.RED + "Inventory successfully cloned");
    }
}
