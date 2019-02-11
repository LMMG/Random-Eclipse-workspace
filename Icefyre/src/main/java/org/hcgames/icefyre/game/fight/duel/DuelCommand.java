package org.hcgames.icefyre.game.fight.duel;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.game.fight.FightType;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.fight.PartyFight;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;

public class DuelCommand extends BaseCommand {

    @Command(name = "duel", aliases = {"duelaccept", "duelparty"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        String[] args = command.getArgs();

        if (command.getLabel().equalsIgnoreCase("duelaccept")) {

            if (args.length != 1) {
                player.sendMessage(langConfig.getString("DUEL.COMMAND.ACCEPT_USAGE"));
                return;
            }

            Player recipient = Bukkit.getPlayer(args[0]);
            if (recipient == null) {
                player.sendMessage(langConfig.getString("DUEL.COMMAND.INVALID_PLAYER").replace("%NAME%", args[0]));
                return;
            }

            Profile recipientProfile = Profile.getByPlayer(recipient);
            if (recipientProfile == null) {
                player.sendMessage(langConfig.getString("DUEL.COMMAND.INVALID_PLAYER").replace("%NAME%", args[0]));
                return;
            }

            if (profile != null) {
                DuelRequest duelRequest = recipientProfile.getDuelRequestByPlayer(player);
                if (duelRequest != null) {

                    if (profile.getState() != ProfileState.IN_LOBBY && profile.getState() != ProfileState.IN_QUEUE) {
                        player.sendMessage(langConfig.getString("DUEL.COMMAND.NOT_IN_SPAWN"));
                        return;
                    }

                    if (recipientProfile.getState() != ProfileState.IN_LOBBY && recipientProfile.getState() != ProfileState.IN_QUEUE) {
                        player.sendMessage(langConfig.getString("DUEL.COMMAND.PLAYER_NOT_IN_SPAWN").replace("%PLAYER%", recipient.getName()));
                        return;
                    }

                    if (duelRequest.isParty()) {
                        Party party = Party.getByPlayer(player);
                        Party recipientParty = recipientProfile.getParty();

                        if (party == null) {
                            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
                            return;
                        }

                        if (recipientParty == null) {
                            player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY_OTHER").replace("%PLAYER%", recipient.getName()));
                            return;
                        }

                        Queue queue = Queue.getByPlayer(player);
                        if (queue != null) {
                            queue.remove(profile);
                        }

                        recipientProfile.getDuelRequests().remove(player.getUniqueId());
                        new PartyFight(party, recipientParty, duelRequest.getLadder(), duelRequest.getArena());
                        return;
                    }

                    Queue queue = Queue.getByPlayer(player);
                    if (queue != null) {
                        queue.remove(profile);
                    }

                    recipientProfile.getDuelRequests().remove(player.getUniqueId());
                    new Fight(new Profile[]{profile, recipientProfile}, duelRequest.getLadder(), duelRequest.getArena(), FightType.UNRANKED);

                } else {
                    player.sendMessage(langConfig.getString("DUEL.COMMAND.NOT_RECEIVED").replace("%PLAYER%", player.getName()));
                }
            }

            return;
        }

        if (args.length != 1) {
            player.sendMessage(langConfig.getString("DUEL.COMMAND.USAGE"));
            return;
        }

        Player recipient = Bukkit.getPlayer(args[0]);
        if (recipient == null) {
            player.sendMessage(langConfig.getString("DUEL.COMMAND.INVALID_PLAYER").replace("%NAME%", args[0]));
            return;
        }

        if (recipient.getName().equals(player.getName())) {
            player.sendMessage(langConfig.getString("DUEL.COMMAND.CANNOT_DUEL_YOURSELF"));
            return;
        }

        Profile recipientProfile = Profile.getByPlayer(recipient);
        if (recipientProfile != null && profile != null) { //TODO: Check options, etc....................

            if (profile.getState() != ProfileState.IN_LOBBY && profile.getState() != ProfileState.IN_QUEUE) {
                player.sendMessage(langConfig.getString("DUEL.COMMAND.NOT_IN_SPAWN"));
                return;
            }

            if (recipientProfile.getState() != ProfileState.IN_LOBBY && recipientProfile.getState() != ProfileState.IN_QUEUE) {
                player.sendMessage(langConfig.getString("DUEL.COMMAND.PLAYER_NOT_IN_SPAWN").replace("%PLAYER%", recipient.getName()));
                return;
            }

            if (command.getLabel().contains("party")) {
                Party party = Party.getByPlayer(player);
                Party recipientParty = recipientProfile.getParty();

                if (party == null) {
                    player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY"));
                    return;
                }

                if (recipientParty == null) {
                    player.sendMessage(langConfig.getString("PARTY.NOT_IN_PARTY_OTHER").replace("%PLAYER%", recipient.getName()));
                    return;
                }

                if (recipientParty.getLeader().getPlayer().getUniqueId().equals(party.getLeader().getPlayer().getUniqueId())) {
                    player.sendMessage(langConfig.getString("DUEL.COMMAND.CANNOT_DUEL_YOURSELF"));
                    return;
                }

                if (!party.isLeader(player)) {
                    return;
                }
            }

            profile.getDuelRequests().put(recipient.getUniqueId(), new DuelRequest(command.getLabel().contains("party")));
            player.openInventory(DuelRequest.getLadderInventory());
        }

    }

}
