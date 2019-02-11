package org.hcgames.icefyre.game.fight;

import lombok.Getter;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.fight.cache.CachedFight;
import org.hcgames.icefyre.game.fight.inventory.FightInventory;
import org.hcgames.icefyre.game.kit.selection.KitSelectionProfile;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.EloCalculator;
import org.hcgames.icefyre.util.packet.ProjectileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fight {

    private static Icefyre main = Icefyre.getInstance();

    private static final int COUNTDOWN_DURATION = main.getConfigFile().getInt("FIGHT.1V1.COUNTDOWN_DURATION");
    private static final String COUNTDOWN_MESSAGE = main.getLangFile().getString("FIGHT.1V1.COUNTDOWN");
    private static final String COUNTDOWN_ENDED_MESSAGE = main.getLangFile().getString("FIGHT.1V1.COUNTDOWN_ENDED");
    private static final boolean COUNTDOWN_SOUND_ENABLED = main.getConfigFile().getBoolean("FIGHT.1V1.COUNTDOWN_SOUND.ENABLED");
    private static final Sound COUNTDOWN_SOUND = Sound.valueOf(main.getConfigFile().getString("FIGHT.1V1.COUNTDOWN_SOUND.TYPE"));

    @Getter private Profile[] participants;
    @Getter private ArrayList<Profile> spectators;
    @Getter private Ladder ladder;
    @Getter private Arena arena;
    @Getter private FightStage stage;
    @Getter private FightType type;
    @Getter private long start;
    public Fight(Profile[] participants, Ladder ladder, Arena arena, FightType type) {
        this.participants = participants;
        this.spectators = new ArrayList<>();
        this.ladder = ladder;
        this.arena = arena;
        this.stage = FightStage.COUNTDOWN;
        this.type = type;

        for (Profile profile : participants) {
            Player player = profile.getPlayer();
            Party party = profile.getParty();
            if (party != null) {
                if (party.isLeader(profile.getPlayer())) {
                    /*Party.getParties().remove(party);
                    party.getMembers().add(party.getLeader());
                    for (Profile member : new ArrayList<>(party.getMembers())) {
                        member.getPlayer().sendMessage(main.getLangFile().getString("PARTY.DELETED"));
                        main.getFightHandler().setupPlayer(member.getPlayer());
                        main.getLobby().setupPlayer(member.getPlayer());
                    }*/
                    profile.getPlayer().performCommand("party disband");
                } else {
                    party.sendMessage(main.getLangFile().getString("PARTY.COMMAND.LEAVE.ANNOUNCEMENT").replace("%PLAYER%", player.getName()));
                    party.getMembers().remove(profile);
                    main.getLobby().setupPlayer(player);
                    main.getFightHandler().setupPlayer(player);

                    if (party.getMembers().size() == 0) {
                        Party.getParties().remove(party);
                        main.getLobby().setupPlayer(party.getLeader().getPlayer());
                        main.getFightHandler().setupPlayer(party.getLeader().getPlayer());
                    }
                }
            }
        }

        int loc = 0;
        for (Profile profile : participants) {
            Player player = profile.getPlayer();

            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Item) {
                        ProjectileUtil.hide(player, entity);
                    }
                }
            }

            profile.getInvitedPlayers().clear();

            player.showPlayer(getOpponent(player).getPlayer());

            player.getInventory().clear();

            int finalLoc = loc;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(arena.getLocations()[finalLoc].clone().add(0, 0.5, 0));
                }
            }.runTask(main);

            profile.setFight(this);
            profile.setState(ProfileState.IN_GAME);

            if (profile.hasKits(ladder)) {
                KitSelectionProfile selectionProfile = new KitSelectionProfile(profile, ladder);

                player.openInventory(selectionProfile.getInventory());

                profile.setSelectionProfile(selectionProfile);
            } else {
                ladder.getDefaultKit().apply(player);
            }

            loc++;
        }

        for (int i = 0; i <= COUNTDOWN_DURATION; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {

                    for (Profile profile : participants) {
                        Player player = profile.getPlayer();

                        if (finalI == COUNTDOWN_DURATION) {
                            player.sendMessage(COUNTDOWN_ENDED_MESSAGE.replace("%SECONDS%", COUNTDOWN_DURATION - finalI + ""));
                        } else {
                            player.sendMessage(COUNTDOWN_MESSAGE.replace("%SECONDS%", COUNTDOWN_DURATION - finalI + ""));
                        }

                        if (COUNTDOWN_SOUND_ENABLED) {
                            player.playSound(player.getLocation(), COUNTDOWN_SOUND, 1, -2);
                        }
                    }

                    if (finalI == COUNTDOWN_DURATION) {
                        stage = FightStage.FIGHTING;
                        start = System.currentTimeMillis();
                    }
                }
            }.runTaskLaterAsynchronously(main, 20 * i);
        }
    }


    //TODO: Rename this
    private void sendRankedMessage(Player winner) {
        Profile loserProfile = getOpponent(winner);
        Profile winnerProfile = getOpponent(loserProfile.getPlayer());
        Player loser = loserProfile.getPlayer();

        int winnerElo = winnerProfile.getRankings().get(ladder);
        int loserElo = loserProfile.getRankings().get(ladder);
        int[] rankings = EloCalculator.getNewRankings(winnerProfile.getRankings().get(ladder), loserProfile.getRankings().get(ladder), true);

        String message = main.getLangFile().getString("FIGHT.1V1.GAME_END.ELO_CHANGES").replace("%WINNER_ELO%", rankings[0] + "").replace("%WINNER_AMOUNT%", rankings[0] - winnerElo + "")
                .replace("%LOSER_ELO%", rankings[1] + "").replace("%LOSER_AMOUNT%", loserElo - rankings[1] + "")
                .replace("%WINNER%", winner.getName())
                .replace("%LOSER%", loser.getName());

        winnerProfile.getRankings().put(ladder, rankings[0]);
        loserProfile.getRankings().put(ladder, rankings[1]);

        winner.sendMessage(message);
        loser.sendMessage(message);
    }

    public void end(Profile winner) {
        stage = FightStage.END;

        if (type == FightType.RANKED) {
            sendRankedMessage(winner.getPlayer());
        }

        FancyMessage message = new FancyMessage(main.getLangFile().getString("FIGHT.1V1.GAME_END.INVENTORIES")).then(winner.getName()).tooltip(main.getLangFile().getString("FIGHT.1V1.GAME_END.INVENTORIES_HOVER").replace("%PLAYER%", winner.getName())).command("/_ " + winner.getPlayer().getUniqueId()).then(", ").then(getOpponent(winner.getPlayer()).getName()).tooltip(main.getLangFile().getString("FIGHT.1V1.GAME_END.INVENTORIES_HOVER").replace("%PLAYER%", getOpponent(winner.getPlayer()).getName())).command("/_ " + getOpponent(winner.getPlayer()).getPlayer().getUniqueId());
        for (Profile profile : participants) {
            profile.setFightInventory(new FightInventory(profile, new ArrayList<>(Arrays.asList(profile.getPlayer().getInventory().getContents())), new ArrayList<>(Arrays.asList(profile.getPlayer().getInventory().getArmorContents())), (int) profile.getPlayer().getHealth()));
            message.send(profile.getPlayer());
        }

        for (Profile profile : participants) {
            profile.getCachedFights().add(new CachedFight(this, winner.getName().equals(profile.getName())));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                winner.setFight(null);
                winner.setState(ProfileState.IN_LOBBY);
                winner.getPotions().clear();
                winner.getPackets().clear();
                Player player = winner.getPlayer();
                main.getLobby().setupPlayer(player);
                main.getLobby().teleportToSpawn(player);
                main.getFightHandler().setupPlayer(player);

            }
        }.runTaskLater(main, 20 * 2L);

        if (spectators.size() > 0) {
            for (Profile profile : spectators) {
                message.send(profile.getPlayer());
                spectators.remove(profile);
                profile.getPlayer().hidePlayer(winner.getPlayer());
                profile.getPlayer().hidePlayer(getOpponent(winner.getPlayer()).getPlayer());
            }
            spectators.clear();
        }
    }

    public Profile getOpponent(Player player) {
        if (getFirstParticipant().getPlayer().getUniqueId().equals(player.getUniqueId())) {
            return getSecondParticipant();
        } else if (getSecondParticipant().getPlayer().getUniqueId().equals(player.getUniqueId())) {
            return getFirstParticipant();
        } else {
            return null;
        }
    }

    public String getFormattedDuration() {
        if (start == 0) {
            return "00:00";
        }
        return DurationFormatUtils.formatDuration(System.currentTimeMillis() - start, "mm:ss");
    }

    public Profile getFirstParticipant() {
        return participants[0];
    }

    public Profile getSecondParticipant() {
        return participants[1];
    }


    public Ladder getLadder() {
        return ladder;
    }
}