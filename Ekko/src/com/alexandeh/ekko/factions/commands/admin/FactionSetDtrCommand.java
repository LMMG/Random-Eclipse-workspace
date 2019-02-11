package com.alexandeh.ekko.factions.commands.admin;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

public class FactionSetDtrCommand extends FactionCommand {

    @Command(name = "f.setdtr", aliases = {"faction.setdtr", "factions.setdtr"}, permission = "ekko.setdtr", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (command.getArgs().length == 2) {
            String name = command.getArgs()[0];

            Faction faction = Faction.getByName(name);
            PlayerFaction playerFaction = null;

            if (faction instanceof PlayerFaction) {
                playerFaction = (PlayerFaction) faction;
            }

            if (faction == null || (!(faction instanceof PlayerFaction))) {
                playerFaction = PlayerFaction.getByPlayerName(name);

                if (playerFaction == null) {
                    sender.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                    return;
                }
            }

            playerFaction.setDeathsTillRaidable(BigDecimal.valueOf(Double.valueOf(command.getArgs()[1])));
            sender.sendMessage(langConfig.getString("ADMIN.SET_DTR").replaceAll("%FACTION%", playerFaction.getName()).replaceAll("%DTR%", playerFaction.getDeathsTillRaidable().doubleValue() + ""));
        } else {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.SET_DTR"));
        }
    }
}
