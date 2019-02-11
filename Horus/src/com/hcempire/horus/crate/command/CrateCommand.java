package com.hcempire.horus.crate.command;

import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.crate.command.subcommand.*;
import com.hcempire.horus.util.HorusCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand extends HorusCommand {

    public CrateCommand() {
        new CrateCreateCommand();
        new CrateDeleteCommand();
        new CrateItemsCommand();
        new CrateKeyCommand();
        new CrateListCommand();
    }

    @Command(name = "crate", permission = "crate.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender player = command.getSender();
        player.sendMessage(ChatColor.RED + "/create list");
        player.sendMessage(ChatColor.RED + "/crate create <name>");
        player.sendMessage(ChatColor.RED + "/crate delete <name>");
        player.sendMessage(ChatColor.RED + "/crate items <name>");
        player.sendMessage(ChatColor.RED + "/crate key <name> <amount> <player>");
    }
}
