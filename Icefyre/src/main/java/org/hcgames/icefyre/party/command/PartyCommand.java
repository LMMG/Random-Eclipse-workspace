package org.hcgames.icefyre.party.command;

import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.command.subcommand.PartyInfoCommand;
import org.hcgames.icefyre.party.command.subcommand.leader.PartyDisbandCommand;
import org.hcgames.icefyre.party.command.subcommand.leader.PartyInviteCommand;
import org.hcgames.icefyre.party.command.subcommand.PartyJoinCommand;
import org.hcgames.icefyre.party.command.subcommand.PartyLeaveCommand;
import org.hcgames.icefyre.party.command.subcommand.leader.PartyKickCommand;
import org.hcgames.icefyre.party.command.subcommand.leader.PartyLeaderCommand;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

public class PartyCommand extends BaseCommand {

    public PartyCommand() {
        new PartyLeaveCommand();
        new PartyDisbandCommand();
        new PartyInviteCommand();
        new PartyJoinCommand();
        new PartyInfoCommand();
        new PartyKickCommand();
        new PartyLeaderCommand();
    }

    @Command(name = "party", aliases = {"team", "p", "t"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (String message : langConfig.getStringList("PARTY.HELP")) {
            player.sendMessage(message);
        }

    }
}
