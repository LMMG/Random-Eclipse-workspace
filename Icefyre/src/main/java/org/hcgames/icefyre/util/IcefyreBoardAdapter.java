package org.hcgames.icefyre.util;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.bizarrealex.aether.scoreboard.cooldown.BoardFormat;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.game.queue.QueueProfile;
import org.hcgames.icefyre.game.queue.QueueType;
import org.hcgames.icefyre.party.fight.PartyFight;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IcefyreBoardAdapter implements BoardAdapter {

    private Icefyre main;

    public IcefyreBoardAdapter(Icefyre main) {
        this.main = main;
    }

    @Override
    public String getTitle(Player player) {
        return main.getLangFile().getString("SCOREBOARD.TITLE");
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
        Profile profile = Profile.getByPlayer(player);

        if (profile != null) {

            if (profile.getParty() != null && profile.getState() == ProfileState.IN_LOBBY) {
                return getFormattedPartyList(profile, main.getLangFile().getStringList("SCOREBOARD.MODULES.IN_PARTY_LOBBY"));
            }

            if (profile.getState() == ProfileState.IN_LOBBY) {
                return getFormattedLobbyList(main.getLangFile().getStringList("SCOREBOARD.MODULES.IN_LOBBY"));
            }

            if (profile.getState() == ProfileState.IN_QUEUE) {
                QueueProfile queueProfile = profile.getQueueProfile();
                if (queueProfile != null) {
                    return getFormattedQueueList(queueProfile, main.getLangFile().getStringList("SCOREBOARD.MODULES.IN_QUEUE"));
                }
            }

            Fight fight = profile.getFight();
            if (fight != null) {
                return getFormattedFightList(profile, fight, main.getLangFile().getStringList("SCOREBOARD.MODULES.IN_DUEL"));
            }

            PartyFight partyFight = profile.getPartyFight();
            if (partyFight != null) {
                return getFormattedPartyFightList(profile, partyFight, main.getLangFile().getStringList("SCOREBOARD.MODULES.IN_PARTY_DUEL"));
            }

        }

        //waiting to get party items sec bbs :))))))))

        return null;
    }

    private int getPlayersInFightsSize() {
        int toReturn = 0;
        for (Profile profile : Profile.getProfiles()) {
            if (profile.getPlayer().isOnline()) {
                if (profile.getFight() != null) {
                    toReturn++;
                }
            }
        }
        return toReturn;
    }

    private int getPlayersInQueueSize() {
        int toReturn = 0;
        for (Profile profile : Profile.getProfiles()) {
            if (profile.getPlayer().isOnline()) {
                if (profile.getQueueProfile() != null) {
                    toReturn++;
                }
            }
        }
        return toReturn;
    }

    private List<String> getFormattedPartyFightList(Profile profile, PartyFight fight, List<String> strings) {
        List<String> toReturn = new ArrayList<>();

        for (String string : strings) {
            string = ChatColor.translateAlternateColorCodes('&', string);

            if (fight.getFormattedDuration().equals("30:00")) {
                fight.end(Math.random() >= 0.5 ? fight.getParties()[0] : fight.getParties()[1]);
            }

            string = string.replace("%TIME%", fight.getFormattedDuration());

            if (string.contains("%ENDER_PEARL%")) {
                Board board = Board.getByPlayer(profile.getPlayer());
                if (board != null) {
                    BoardCooldown cooldown = board.getCooldown("enderpearl");
                    if (cooldown != null) {
                        toReturn.add(string.replace("%ENDER_PEARL%", cooldown.getFormattedString(BoardFormat.SECONDS)));
                    }
                }
            } else {
                toReturn.add(string);
            }
        }

        return toReturn;
    }

    private List<String> getFormattedFightList(Profile profile, Fight fight, List<String> strings) {
        List<String> toReturn = new ArrayList<>();

        for (String string : strings) {
            string = ChatColor.translateAlternateColorCodes('&', string);
            string = string.replace("%OPPONENT%", fight.getOpponent(profile.getPlayer()).getName());

            if (fight.getFormattedDuration().equals("15:00")) {
                fight.end(Math.random() >= 0.5 ? fight.getFirstParticipant() : fight.getSecondParticipant());
            }

            string = string.replace("%TIME%", fight.getFormattedDuration());

            if (string.contains("%ENDER_PEARL%")) {
                Board board = Board.getByPlayer(profile.getPlayer());
                if (board != null) {
                    BoardCooldown cooldown = board.getCooldown("enderpearl");
                    if (cooldown != null) {
                        toReturn.add(string.replace("%ENDER_PEARL%", cooldown.getFormattedString(BoardFormat.SECONDS)));
                    }
                }
            } else {
                toReturn.add(string);
            }
        }

        return toReturn;
    }

    private List<String> getFormattedQueueList(QueueProfile queueProfile, List<String> strings) {
        List<String> toReturn = new ArrayList<>();

        for (String string : strings) {

            if ((string.contains("%LOW%") || string.contains("%HIGH%")) && queueProfile.getQueue().getType() == QueueType.UNRANKED) {
                continue;
            } else {
                int roundedCurrent = (int) ((10 - System.currentTimeMillis() % 10) + System.currentTimeMillis());
                int roundedPrevious = (int) ((10 - queueProfile.getStart() % 10) + queueProfile.getStart());
                int range = Math.round((roundedCurrent - roundedPrevious) / 5000) * QueueProfile.STARTING_RANGE;
                int ranking = queueProfile.getRanking();

                if (range <= QueueProfile.STARTING_RANGE) {
                    range = QueueProfile.STARTING_RANGE;
                }

                int low = ranking - range;
                int high = ranking + range;

                string = string.replace("%HIGH%", high + "");
                string = string.replace("%LOW%", low + "");
            }

            string = ChatColor.translateAlternateColorCodes('&', string);
            string = string.replace("%LADDER%", queueProfile.getQueue().getLadder().getName());
            string = string.replace("%TIME%", queueProfile.getFormattedTimeInQueue());
            toReturn.add(string);
        }

        return toReturn;
    }

    private List<String> getFormattedLobbyList(List<String> strings) {
        List<String> toReturn = new ArrayList<>();

        for (String string : strings) {
            string = ChatColor.translateAlternateColorCodes('&', string);
            string = string.replace("%ONLINE%", Bukkit.getOnlinePlayers().size() + "");
            string = string.replace("%IN_QUEUE%", getPlayersInQueueSize() + "");
            string = string.replace("%IN_FIGHTS%", getPlayersInFightsSize() + "");
            toReturn.add(string);
        }

        return toReturn;
    }

    private List<String> getFormattedPartyList(Profile profile, List<String> strings) {
        List<String> toReturn = new ArrayList<>();

        for (String string : strings) {
            string = ChatColor.translateAlternateColorCodes('&', string);
            string = string.replace("%ONLINE%", Bukkit.getOnlinePlayers().size() + "");
            string = string.replace("%IN_QUEUE%", getPlayersInQueueSize() + "");
            string = string.replace("%IN_FIGHTS%", getPlayersInFightsSize() + "");
            if (profile.getParty() != null) {
                Party party = profile.getParty();
                string = string.replace("%LEADER%", party.getLeader().getName());
                string = string.replace("%IN_PARTY%", party.getMembers().size() + 1 + "");
                toReturn.add(string);
            }
        }

        return toReturn;
    }
}
