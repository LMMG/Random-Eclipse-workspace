package org.hcgames.icefyre.party.fight;

import lombok.Getter;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.fight.FightStage;
import org.hcgames.icefyre.game.fight.FightType;
import org.hcgames.icefyre.game.fight.inventory.FightInventory;
import org.hcgames.icefyre.game.kit.selection.KitSelectionProfile;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.PartyMemory;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.packet.ProjectileUtil;

import java.util.*;

public class PartyFight {

    private static Icefyre main = Icefyre.getInstance();

    private static final int COUNTDOWN_DURATION = main.getConfigFile().getInt("FIGHT.PARTY.COUNTDOWN_DURATION");
    private static final String COUNTDOWN_MESSAGE = main.getLangFile().getString("FIGHT.PARTY.COUNTDOWN");
    private static final String COUNTDOWN_ENDED_MESSAGE = main.getLangFile().getString("FIGHT.PARTY.COUNTDOWN_ENDED");
    private static final boolean COUNTDOWN_SOUND_ENABLED = main.getConfigFile().getBoolean("FIGHT.PARTY.COUNTDOWN_SOUND.ENABLED");
    private static final Sound COUNTDOWN_SOUND = Sound.valueOf(main.getConfigFile().getString("FIGHT.PARTY.COUNTDOWN_SOUND.TYPE"));

    @Getter private PartyMemory[] parties;
    @Getter private Map<PartyMemory, List<Player>> deadPlayers;
    @Getter private Ladder ladder;
    @Getter private Arena arena;
    @Getter private FightStage stage;
    @Getter private long start;

    public PartyFight(Party party, Party opponentParty, Ladder ladder, Arena arena) {
        this.parties = new PartyMemory[]{new PartyMemory(party), new PartyMemory(opponentParty)};
        this.ladder = ladder;
        this.arena = arena;
        this.stage = FightStage.COUNTDOWN;
        this.deadPlayers = new HashMap<>();

        this.deadPlayers.put(parties[0], new ArrayList<>());
        this.deadPlayers.put(parties[1], new ArrayList<>());

        for (Profile profile : getAllProfiles()) {
            Player player = profile.getPlayer();

            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Item) {
                        ProjectileUtil.hide(player, entity);
                    }
                }
            }

            profile.setPartyFight(this);
            profile.setState(ProfileState.IN_GAME);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (party.contains(profile)) {
                        player.teleport(arena.getLocations()[0].clone().add(0, 0.5, 0));
                    } else {
                        player.teleport(arena.getLocations()[1].clone().add(0, 0.5, 0));
                    }
                }
            }.runTask(main);

            for (Profile other : getAllProfiles()) {
                if (other.getName().equals(profile.getName())) continue;
                player.showPlayer(other.getPlayer());
            }

            if (profile.hasKits(ladder)) {
                KitSelectionProfile selectionProfile = new KitSelectionProfile(profile, ladder);

                player.openInventory(selectionProfile.getInventory());

                profile.setSelectionProfile(selectionProfile);
            } else {
                ladder.getDefaultKit().apply(player);
            }
        }

        for (int i = 0; i <= COUNTDOWN_DURATION; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {

                    for (Profile profile : getAllProfiles()) {
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

    public PartyMemory getPartyByProfile(Profile profile) {
        for (PartyMemory party : parties) {
            if (party.contains(profile)) {
                return party;
            }
        }
        return null;
    }

    public PartyMemory getOpponent(PartyMemory party) {
        if (parties[0].getLeader().equals(party.getLeader())) {
            return parties[1];
        }
        return parties[0];
    }

    public List<Profile> getAllProfiles() {
        List<Profile> toReturn = new ArrayList<>();

        for (PartyMemory party : parties) {
            toReturn.addAll(party.getMembers());
            toReturn.add(party.getLeader());
        }

        return toReturn;
    }

    public String getFormattedDuration() {
        if (start == 0) {
            return "00:00";
        }
        return DurationFormatUtils.formatDuration(System.currentTimeMillis() - start, "mm:ss");
    }

    public void end(PartyMemory winner) {
        FancyMessage message = new FancyMessage(main.getLangFile().getString("FIGHT.PARTY.GAME_END.INVENTORIES"));

        int pos = 0;
        for (Profile profile : getAllProfiles()) {
            pos++;

            message.then(profile.getName()).tooltip(main.getLangFile().getString("FIGHT.PARTY.GAME_END.INVENTORIES_HOVER").replace("%PLAYER%", profile.getName())).command("/_ " + profile.getPlayer().getUniqueId());

            if (pos != getAllProfiles().size()) {
                message.then(", ");
            }
        }

        for (Profile profile : getAllProfiles()) {
            if (!(getDeadPlayers().get(getPartyByProfile(profile)).contains(profile.getPlayer()))) {
                profile.setFightInventory(new FightInventory(profile, new ArrayList<>(Arrays.asList(profile.getPlayer().getInventory().getContents())), new ArrayList<>(Arrays.asList(profile.getPlayer().getInventory().getArmorContents())), (int) profile.getPlayer().getHealth()));
            }
            profile.getPlayer().sendMessage(main.getLangFile().getString("FIGHT.PARTY.GAME_END.WINNER").replace("%WINNER%", winner.getLeader().getName()));
            message.send(profile.getPlayer());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Profile winnerProfile : getAllProfiles()) {
                    if (winner.contains(winnerProfile)) {
                        if (!(deadPlayers.get(winner).contains(winnerProfile.getPlayer()))) {
                            winnerProfile.setPartyFight(null);
                            winnerProfile.setState(ProfileState.IN_LOBBY);
                            winnerProfile.getPotions().clear();
                            winnerProfile.getPackets().clear();
                            Player player = winnerProfile.getPlayer();
                            main.getLobby().setupPlayer(player);
                            main.getLobby().teleportToSpawn(player);
                            main.getFightHandler().setupPlayer(player);
                            if (winner.isLeader(winnerProfile.getPlayer())) {
                                main.getPartyHandler().setupProfile(winnerProfile);
                            }
                        }
                    }
                }
            }
        }.runTaskLater(main, 20 * 2L);
    }

}
