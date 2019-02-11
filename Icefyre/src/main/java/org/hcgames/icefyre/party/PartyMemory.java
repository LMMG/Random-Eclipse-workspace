package org.hcgames.icefyre.party;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class PartyMemory {

    @Getter private final Profile leader;
    @Getter private final List<Profile> members;

    public PartyMemory(Party party) {
        this.leader = party.getLeader();
        this.members = party.getMembers();
    }

    public boolean contains(Profile profile) {
        return members.contains(profile) || isLeader(profile.getPlayer());
    }

    public boolean isLeader(Player player) {
        return player.getUniqueId().equals(leader.getPlayer().getUniqueId());
    }

    public void sendMessage(String message) {
        leader.getPlayer().sendMessage(message);
        for (Profile profile : members) {
            profile.getPlayer().sendMessage(message);
        }
    }

    public int getSize() {
        return members.size() + 1;
    }

    public List<Profile> getAllProfiles() {
        List<Profile> toReturn = new ArrayList<>();

        toReturn.add(leader);
        toReturn.addAll(members);

        return toReturn;
    }

}
