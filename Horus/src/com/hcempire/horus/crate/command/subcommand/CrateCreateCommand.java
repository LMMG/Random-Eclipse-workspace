package com.hcempire.horus.crate.command.subcommand;

import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.crate.Crate;
import com.hcempire.horus.util.HorusCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class CrateCreateCommand extends HorusCommand {
    @Command(name = "crate.create", permission = "crate.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /crate crate <name>");
            return;
        }

        String name = StringUtils.join(args);
        Crate crate = Crate.getByName(name);

        if (crate != null) {
            player.sendMessage(ChatColor.RED + "A crate named '" + crate.getName() + "' already exists.");
            return;
        }

        crate = new Crate(name);
        player.sendMessage(ChatColor.RED + "Crate named '" + crate.getName() + "' successfully created.");
    }
}
