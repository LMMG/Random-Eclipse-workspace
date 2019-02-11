package org.hcgames.icefyre.party.command.subcommand.leader;

import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

import java.util.ArrayList;

public class PartyInviteCommand extends BaseCommand {
    @Command(name = "party.invite", aliases = {"team.invite", "t.invite", "p.invite"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        String[] args = command.getArgs();
        Party party = Party.getByPlayer(player);

        if (args.length == 0 || profile == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.USAGE"));
            return;
        }

        if (party != null && !party.isLeader(player)) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.NOT_LEADER"));
            return;
        }

        String name = StringUtils.join(args);
        Player invited = Bukkit.getPlayer(name);

        if (invited == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.NOT_ONLINE").replace("%NAME%", name));
            return;
        }

        if (invited.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.INVITE_SELF"));
            return;
        }

        if (party != null && party.getMembers().contains(Profile.getByPlayer(invited))) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.ALREADY_IN_PARTY").replace("%PLAYER%", invited.getName()));
            return;
        }

        if (profile.getInvitedPlayers().contains(invited.getUniqueId())) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.ALREADY_INVITED").replace("%PLAYER%", invited.getName()));
            return;
        }

        profile.getInvitedPlayers().add(invited.getUniqueId());
        if (party != null) {
            party.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.ANNOUNCEMENT").replace("%PLAYER%", player.getName()).replace("%INVITED%", invited.getName()));
        } else {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.INVITE.ANNOUNCEMENT").replace("%PLAYER%", player.getName()).replace("%INVITED%", invited.getName()));
        }

        new FancyMessage().text(langConfig.getString("PARTY.COMMAND.INVITE.INVITED").replace("%PLAYER%", player.getName())).tooltip(langConfig.getString("PARTY.COMMAND.INVITE.INVITED_TOOLTIP").replace("%PLAYER%", player.getName())).command("/party join " + player.getName().toLowerCase()).send(invited);

    }
}
