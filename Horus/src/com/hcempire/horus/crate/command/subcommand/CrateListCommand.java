package com.hcempire.horus.crate.command.subcommand;

import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.crate.Crate;
import com.hcempire.horus.util.HorusCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateListCommand extends HorusCommand {
    @Command(name = "crate.list", permission = "crate.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(ChatColor.GREEN + "Listing all registered crates:");
        for (Crate crate : Crate.getCrates()) {
            sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.GRAY + crate.getName());
        }

    }
}
