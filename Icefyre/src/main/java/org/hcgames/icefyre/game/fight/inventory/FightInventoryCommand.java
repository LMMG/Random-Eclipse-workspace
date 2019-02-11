package org.hcgames.icefyre.game.fight.inventory;

import org.bukkit.entity.Player;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

import java.util.UUID;

public class FightInventoryCommand extends BaseCommand {
    @Command(name = "_", aliases = {"inventory"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(langConfig.getString("FIGHT.INVENTORY.COMMAND.USAGE"));
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(args[0]);
        } catch (Exception ignored) {
            player.sendMessage(langConfig.getString("FIGHT.INVENTORY.COMMAND.USAGE"));
            return;
        }

        for (Profile profile : Profile.getProfiles()) {
            if (profile.getPlayer().getUniqueId().equals(uuid) && profile.getFightInventory() != null) {
                player.openInventory(profile.getFightInventory().get());
                return;
            }
        }

        player.sendMessage(langConfig.getString("FIGHT.INVENTORY.COMMAND.INVALID"));
    }
}
