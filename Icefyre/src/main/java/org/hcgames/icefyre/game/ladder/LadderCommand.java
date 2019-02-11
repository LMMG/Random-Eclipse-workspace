package org.hcgames.icefyre.game.ladder;

import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LadderCommand extends BaseCommand {

    @Override
    @Command(name = "ladder", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/ladder <name> <kit/setkit>");
            return;
        }

        if (args.length == 2) {
            Ladder ladder = Ladder.getByName(args[0]);

            if (ladder == null) {
                player.sendMessage(ChatColor.RED + "No ladder with the name '" + args[0] + "' found.");
                return;
            }

            if (args[1].equalsIgnoreCase("setkit")) {
                ladder.setDefaultKit(new Kit("Default " + ladder.getName() + " Kit", ladder, Arrays.asList(player.getInventory().getContents()), Arrays.asList(player.getInventory().getArmorContents())));
                player.sendMessage(ChatColor.RED + "You have successfully updated the default kit for the ladder named '" + ladder.getName() + "'.");
                return;
            }

            if (args[1].equalsIgnoreCase("kit")) {
                Kit kit = ladder.getDefaultKit();

                if (kit == null) {
                    player.sendMessage(ChatColor.RED + "That ladder does not have a default kit!");
                    return;
                }

                kit.apply(player);
                player.sendMessage(ChatColor.RED + "The default kit for the ladder named '" + ladder.getName() + "' has been applied!");
            }
        }
    }
}
