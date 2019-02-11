package org.hcgames.icefyre.party.command.subcommand;

import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

import java.util.ArrayList;
import java.util.List;

public class PartyJoinCommand extends BaseCommand {
    @Command(name = "party.join", aliases = {"team.join", "t.join", "p.join"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        String[] args = command.getArgs();
        Party party = Party.getByPlayer(player);

        if (args.length == 0 || profile == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.USAGE"));
            return;
        }

        if (party != null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.IN_PARTY"));
            return;
        }

        String name = StringUtils.join(args);
        Player toJoin = Bukkit.getPlayer(name);

        if (toJoin == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.NOT_ONLINE").replace("%NAME%", name));
            return;
        }

        Profile toJoinProfile = Profile.getByPlayer(toJoin);
        if (toJoinProfile == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.NOT_ONLINE").replace("%NAME%", name));
            return;
        }

        if (toJoin.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.JOIN_SELF"));
            return;
        }

        if (!toJoinProfile.getInvitedPlayers().contains(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.NOT_INVITED").replace("%PLAYER%", toJoin.getName()));
            return;
        }

        if (profile.getState() != ProfileState.IN_LOBBY) {
            player.sendMessage(langConfig.getString("DUEL.COMMAND.NOT_IN_SPAWN"));
            return;
        }

        if (toJoinProfile.getState() != ProfileState.IN_LOBBY) {
            player.sendMessage(langConfig.getString("DUEL.COMMAND.PLAYER_NOT_IN_SPAWN").replace("%PLAYER%", toJoin.getName()));
            return;
        }

        toJoinProfile.getInvitedPlayers().remove(player.getUniqueId());

        Party toJoinParty = Party.getByPlayer(toJoin);
        if (toJoinParty == null) {
            toJoinParty = new Party(toJoinProfile);
            main.getPartyHandler().setupProfile(toJoinProfile);
        }

        toJoinParty.getMembers().add(profile);
        toJoinParty.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.ANNOUNCEMENT").replace("%PLAYER%", player.getName()));

        main.getFightHandler().setupPlayer(toJoinParty.getLeader().getPlayer());

        for (Profile member : toJoinParty.getMembers()) {
            main.getFightHandler().setupPlayer(member.getPlayer());
        }
    }
}
