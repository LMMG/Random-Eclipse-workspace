package org.hcgames.icefyre.party.command.subcommand.leader;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

public class PartyLeaderCommand extends BaseCommand {
    @Command(name = "party.leader", aliases = {"team.leader", "t.leader", "p.leader"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        String[] args = command.getArgs();
        Party party = Party.getByPlayer(player);

        if (args.length == 0 || profile == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEADER.USAGE"));
            return;
        }

        if (party == null) {
            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
            return;
        }

        if (!party.isLeader(player)) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEADER.NOT_LEADER"));
            return;
        }

        String name = StringUtils.join(args);
        Player toLeader = Bukkit.getPlayer(name);

        if (toLeader == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEADER.NOT_ONLINE").replace("%NAME%", name));
            return;
        }

        if (toLeader.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEADER.LEADER_SELF"));
            return;
        }

        if (!party.getMembers().contains(Profile.getByPlayer(toLeader))) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.LEADER.NOT_IN_PARTY").replace("%PLAYER%", toLeader.getName()));
            return;
        }

        Profile toLeaderProfile = Profile.getByPlayer(toLeader);
        if (toLeaderProfile != null) {
            party.getMembers().remove(Profile.getByPlayer(toLeader));
            party.getMembers().add(profile);
            party.setLeader(toLeaderProfile);
            profile.getInvitedPlayers().clear();
            main.getLobby().setupPlayer(player);
            main.getPartyHandler().setupProfile(toLeaderProfile);
            party.sendMessage(langConfig.getString("PARTY.COMMAND.LEADER.ANNOUNCEMENT").replace("%PLAYER%", toLeader.getName()).replace("%LEADER%", player.getName()));
        }
    }
}
