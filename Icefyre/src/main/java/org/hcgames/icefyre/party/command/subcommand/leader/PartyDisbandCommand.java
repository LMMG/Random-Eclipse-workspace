package org.hcgames.icefyre.party.command.subcommand.leader;

import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

import java.util.ArrayList;

public class PartyDisbandCommand extends BaseCommand {
    @Command(name = "party.disband", aliases = {"team.disband", "t.disband", "p.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        Party party = Party.getByPlayer(player);

        if (party == null) {
            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
            return;
        }

        if (!party.isLeader(player)) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEAVE.NOT_LEADER"));
            return;
        }

        if (profile != null) {
            Party.getParties().remove(party);
            party.getMembers().add(party.getLeader());
            party.getLeader().getInvitedPlayers().clear();
            for (Profile member : new ArrayList<>(party.getMembers())) {
                member.getPlayer().sendMessage(langConfig.getString("PARTY.DELETED"));
                main.getFightHandler().setupPlayer(member.getPlayer());
                main.getLobby().setupPlayer(member.getPlayer());
            }
        }
    }
}
