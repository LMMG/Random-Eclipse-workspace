package org.hcgames.icefyre.party.command.subcommand;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

import java.util.ArrayList;
import java.util.List;

public class PartyInfoCommand extends BaseCommand {
    @Command(name = "party.info", aliases = {"team.info", "t.info", "p.info", "t.i", "p.i"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            Party party = Party.getByPlayer(player);

            if (party == null) {
                player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
                return;
            }

            player.sendMessage(getInformation(party));
            return;
        }

        String name = StringUtils.join(args);
        Player toView = Bukkit.getPlayer(name);

        if (toView == null) {
            player.sendMessage(langConfig.getString("PARTY.COMMAND.JOIN.NOT_ONLINE").replace("%NAME%", name));
            return;
        }

        Party party = Party.getByPlayer(toView);

        if (party == null) {
            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY_OTHER").replace("%PLAYER%", toView.getName()));
            return;
        }

        player.sendMessage(getInformation(party));
    }

    private String[] getInformation(Party party) {
        List<String> toReturn = new ArrayList<>();

        String members = "";
        for (Profile member : party.getMembers()) {
            members = members + member.getName() + ", ";
        }
        if (members.endsWith(", ")) {
            members = members.substring(0, members.length() - 2);
        }

        for (String message : main.getLangFile().getStringList("PARTY.COMMAND.INFO.MESSAGE")) {
            if (message.contains("%MEMBERS%")) {
                if (!(members.isEmpty())) {
                    toReturn.add(message.replace("%MEMBERS%", members));
                }
            } else {
                toReturn.add(message.replace("%LEADER%", party.getLeader().getName()));
            }
        }

        return toReturn.toArray(new String[toReturn.size()]);
    }
}
