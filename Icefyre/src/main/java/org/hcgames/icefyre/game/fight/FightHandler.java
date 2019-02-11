package org.hcgames.icefyre.game.fight;

import com.bizarrealex.aether.scoreboard.Board;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.fight.PartyFight;
import org.hcgames.icefyre.profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class FightHandler {

    private final Icefyre main;

    public FightHandler(Icefyre main) {
        this.main = main;
    }

    public void setupPlayer(Player player) {
        Profile profile = Profile.getByPlayer(player);
        Board board = Board.getByPlayer(player);
        if (profile != null && board != null) {
            Team friendly = board.getScoreboard().getTeam("friendly");
            Team enemy = board.getScoreboard().getTeam("enemy");

            if (friendly == null) {
                friendly = board.getScoreboard().registerNewTeam("friendly");
                friendly.setPrefix(main.getLangFile().getString("TAB.FRIEND_PREFIX"));
            }

            if (enemy == null) {
                enemy = board.getScoreboard().registerNewTeam("enemy");
                enemy.setPrefix(main.getLangFile().getString("TAB.ENEMY_PREFIX"));
            }

            if (!(friendly.getEntries().contains(player.getName()))) {
                friendly.addEntry(player.getName());
            }

            Party party = Party.getByPlayer(player);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle()));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle()));

            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other.getName().equals(player.getName())) continue;
                if (party != null && party.contains(Profile.getByPlayer(other))) continue;
                player.hidePlayer(other);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) other).getHandle()));

                if (friendly.getEntries().contains(other.getName())) {
                    friendly.removeEntry((other.getName()));
                }

                enemy.addEntry(other.getName());
                setupPlayer(other, true);
            }

            if (party != null) {
                List<Profile> members = new ArrayList<>(party.getMembers());
                members.add(party.getLeader());
                for (Profile member : members) {
                    Player memberPlayer = member.getPlayer();
                    if (member.getName().equals(player.getName())) continue;
                    player.showPlayer(memberPlayer);

                    if (enemy.getEntries().contains(member.getName())) {
                        enemy.removeEntry(member.getName());
                    }

                    friendly.addEntry(member.getName());
                }
            }
        }
    }

    public void setupPlayer(Player player, boolean forced) {
        Profile profile = Profile.getByPlayer(player);
        Board board = Board.getByPlayer(player);
        if (profile != null && board != null) {
            Team friendly = board.getScoreboard().getTeam("friendly");
            Team enemy = board.getScoreboard().getTeam("enemy");

            if (friendly == null) {
                friendly = board.getScoreboard().registerNewTeam("friendly");
                friendly.setPrefix(main.getLangFile().getString("TAB.FRIEND_PREFIX"));
            }

            if (enemy == null) {
                enemy = board.getScoreboard().registerNewTeam("enemy");
                enemy.setPrefix(main.getLangFile().getString("TAB.ENEMY_PREFIX"));
            }

            if (!(friendly.getEntries().contains(player.getName()))) {
                friendly.addEntry(player.getName());
            }

            Party party = Party.getByPlayer(player);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle()));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle()));

            Fight fight = profile.getFight();
            PartyFight partyFight = profile.getPartyFight();

            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other.getName().equals(player.getName())) continue;
                if (party != null && party.contains(Profile.getByPlayer(other))) continue;
                if (fight != null && fight.getOpponent(other) != null) continue;
                if (partyFight != null && partyFight.getAllProfiles().contains(Profile.getByPlayer(other))) continue;

                player.hidePlayer(other);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) other).getHandle()));

                if (friendly.getEntries().contains(other.getName())) {
                    friendly.removeEntry(other.getName());
                }

                enemy.addEntry(other.getName());
                if (!(forced)) {
                    setupPlayer(other);
                }
            }

            if (party != null) {
                List<Profile> members = new ArrayList<>(party.getMembers());
                members.add(party.getLeader());
                for (Profile member : members) {
                    Player memberPlayer = member.getPlayer();
                    if (member.getName().equals(player.getName())) continue;
                    memberPlayer.showPlayer(player);
                    player.showPlayer(memberPlayer);

                    if (enemy.getEntries().contains(member.getName())) {
                        enemy.removeEntry(member.getName());
                    }

                    friendly.addEntry(player.getName());
                }
            }
        }
    }

}
