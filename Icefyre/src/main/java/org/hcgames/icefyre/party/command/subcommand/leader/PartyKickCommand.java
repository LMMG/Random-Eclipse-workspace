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

public class PartyKickCommand extends BaseCommand {
    @Command(name = "party.kick", aliases = {"team.kick", "t.kick", "p.kick"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        String[] args = command.getArgs();
        Party party = Party.getByPlayer(player);

        if (args.length == 0 || profile == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.KICK.USAGE"));
            return;
        }

        if (party == null) {
            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
            return;
        }

        if (!party.isLeader(player)) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.KICK.NOT_LEADER"));
            return;
        }

        String name = StringUtils.join(args);
        Player toKick = Bukkit.getPlayer(name);

        if (toKick == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.KICK.NOT_ONLINE").replace("%NAME%", name));
            return;
        }

        if (toKick.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.KICK.KICK_SELF"));
            return;
        }

        if (!party.getMembers().contains(Profile.getByPlayer(toKick))) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.KICK.NOT_IN_PARTY").replace("%PLAYER%", toKick.getName()));
            return;
        }

        Profile toKickProfile = Profile.getByPlayer(toKick);
        if (toKickProfile != null) {
            party.getMembers().remove(Profile.getByPlayer(toKick));

            main.getLobby().setupPlayer(player);
            main.getFightHandler().setupPlayer(player);

            party.sendMessage(langConfig.getString("PARTY.COMMAND.KICK.ANNOUNCEMENT").replace("%PLAYER%", toKick.getName()).replace("%LEADER%", player.getName()));

            if (party.getMembers().size() == 0) {
                Party.getParties().remove(party);
                main.getLobby().setupPlayer(party.getLeader().getPlayer());
                main.getFightHandler().setupPlayer(party.getLeader().getPlayer());
            }
        }
    }
}
