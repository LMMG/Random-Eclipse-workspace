package org.hcgames.icefyre.game.arena;

import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaCommand extends BaseCommand {

    @Override
    @Command(name = "arena", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/arena <name> <set1/set2>");
            return;
        }

        if (args.length == 2) {
            Arena arena = Arena.getByName(args[0]);

            if (arena == null) {
                player.sendMessage(ChatColor.RED + "No arena with the name '" + args[0] + "' found.");
                return;
            }

            if (args[1].equalsIgnoreCase("set1")) {
                arena.getLocations()[0] = player.getLocation();
                player.sendMessage(ChatColor.RED + "You have set the first spawn for the arena named '" + arena.getName() + "'.");
                return;
            }

            if (args[1].equalsIgnoreCase("set2")) {
                arena.getLocations()[1] = player.getLocation();
                player.sendMessage(ChatColor.RED + "You have set the second spawn for the arena named '" + arena.getName() + "'.");
            }
        }
    }
}
