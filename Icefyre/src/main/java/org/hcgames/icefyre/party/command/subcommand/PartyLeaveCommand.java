package org.hcgames.icefyre.party.command.subcommand;

import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

import java.util.ArrayList;
import java.util.Arrays;

public class PartyLeaveCommand extends BaseCommand {
    @Command(name = "party.leave", aliases = {"team.leave", "t.leave", "p.leave"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        Party party = Party.getByPlayer(player);

        if (party == null) {
            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
            return;
        }

        if (party.isLeader(player)) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEAVE.IS_LEADER"));
            return;
        }

        if (profile != null) {
            party.sendMessage(langConfig.getString("PARTY.COMMAND.LEAVE.ANNOUNCEMENT").replace("%PLAYER%", player.getName()));
            party.getMembers().remove(profile);
            main.getLobby().setupPlayer(player);
            main.getFightHandler().setupPlayer(player);

            if (party.getMembers().size() == 0) {
                Party.getParties().remove(party);
                main.getLobby().setupPlayer(party.getLeader().getPlayer());
                main.getFightHandler().setupPlayer(party.getLeader().getPlayer());
            }

        }
    }
}
