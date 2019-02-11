package org.hcgames.icefyre.game.queue;

import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.ItemBuilder;
import org.hcgames.icefyre.util.file.ConfigFile;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueHandler {

    private final Icefyre main;
    private final ConfigFile configFile;
    private final ConfigFile langFile;
    private int starting;

    public QueueHandler(Icefyre main) {
        this.main = main;
        this.configFile = main.getConfigFile();
        this.langFile = main.getLangFile();
        this.starting = main.getConfigFile().getInt("QUEUE.STARTING_RANGE");

        run();
    }

    public void addProfileToQueue(Profile profile, Queue queue) {
        profile.setQueueProfile(new QueueProfile(profile, queue, profile.getRankings().get(queue.getLadder())));
        profile.getInvitedPlayers().clear();

        Party party = profile.getParty();
        if (party != null) {
            party.sendMessage(main.getLangFile().getString("PARTY.COMMAND.LEAVE.ANNOUNCEMENT").replace("%PLAYER%", profile.getPlayer().getName()));
            party.getMembers().remove(profile);
            main.getFightHandler().setupPlayer(profile.getPlayer());

            if (party.getMembers().size() == 0) {
                Party.getParties().remove(party);
                main.getLobby().setupPlayer(party.getLeader().getPlayer());
                main.getFightHandler().setupPlayer(party.getLeader().getPlayer());
            }

        }

        int ranking = profile.getQueueProfile().getRanking();

        profile.getPlayer().sendMessage(
                main.getLangFile().getString("QUEUE.JOIN." + (queue.getType() == QueueType.RANKED ? "RANKED" : "UNRANKED"))
                .replace("%LADDER%", queue.getLadder().getName())
                .replace("%ELO%", ranking + "")
        );

        profile.getPlayer().sendMessage(
                main.getLangFile().getString("QUEUE.SEARCHING." + (queue.getType() == QueueType.RANKED ? "RANKED" : "UNRANKED"))
                .replace("%LADDER%", queue.getLadder().getName())
                .replace("%ELO%", ranking + "")
                .replace("%LOW_RANGE%", ranking - starting + "")
                .replace("%HIGH_RANGE%", ranking + starting + "")
        );

        profile.getQueueProfile().getRange()[0] = starting;
        queue.getPlayersInQueue().add(profile.getPlayer());
        setupPlayer(profile.getPlayer());
    }

    public void setupPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);

        Inventory inventory = player.getInventory();
        inventory.setItem(0, new ItemBuilder(
                Material.valueOf(configFile.getString("HOTBAR_ITEMS.QUEUE.NO_TEAM.QUEUE_INFO.ITEM")))
                .name(configFile.getString("HOTBAR_ITEMS.QUEUE.NO_TEAM.QUEUE_INFO.NAME"))
                .lore(configFile.getStringList("HOTBAR_ITEMS.QUEUE.NO_TEAM.QUEUE_INFO.LORE"))
                .build()
        );

        inventory.setItem(8, new ItemBuilder(
                Material.valueOf(configFile.getString("HOTBAR_ITEMS.QUEUE.NO_TEAM.LEAVE_QUEUE.ITEM")))
                .name(configFile.getString("HOTBAR_ITEMS.QUEUE.NO_TEAM.LEAVE_QUEUE.NAME"))
                .lore(configFile.getStringList("HOTBAR_ITEMS.QUEUE.NO_TEAM.LEAVE_QUEUE.LORE"))
                .build()
        );

        player.updateInventory();
        player.getInventory().setHeldItemSlot(0);
    }

    public void run() { //TODO: Use constants instead of ugly numbers, make delay configurable
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Profile profile : Profile.getProfiles()) {
                    QueueProfile queueProfile = profile.getQueueProfile();
                    if (queueProfile != null) {
                        Player player = profile.getPlayer();
                        Queue queue = queueProfile.getQueue();

                        int roundedCurrent = (int) ((10 - System.currentTimeMillis() % 10) + System.currentTimeMillis());
                        int roundedPrevious = (int) ((10 - queueProfile.getStart() % 10) + queueProfile.getStart());
                        int range = Math.round((roundedCurrent - roundedPrevious) / 5000) * starting;
                        int ranking = profile.getQueueProfile().getRanking();

                        if (range <= starting) {
                            range = starting;
                        }

                        if (range > main.getConfigFile().getInt("QUEUE.MAX_RANGE") || (ranking - range <= 0)) {
                            queue.remove(profile);

                            profile.getPlayer().sendMessage(
                                    main.getLangFile().getString("QUEUE.FAILED." + (queue.getType() == QueueType.RANKED ? "RANKED" : "UNRANKED"))
                                            .replace("%LADDER%", queue.getLadder().getName())
                            );

                            continue;
                        }

                        for (QueueProfile opponent : queue.getQueueProfiles()) {

                            if (opponent.equals(queueProfile) || !opponent.getQueue().equals(queue)) {
                                continue;
                            }

                            if (queue.getType() == QueueType.RANKED) {
                                int low = ranking - range;
                                int high = ranking + range;

                                int opponentTime = (int) ((10 - opponent.getStart() % 10) + opponent.getStart());
                                int opponentRange = Math.round((roundedCurrent - opponentTime) / 5000) * starting;
                                int opponentRanking = opponent.getRanking();

                                if (opponentRange <= starting) {
                                    opponentRange = starting;
                                }

                                int opponentLow = opponentRanking - opponentRange;
                                int opponentHigh = opponentRanking + opponentRange;

                                if ((ranking >= opponentLow && ranking <= opponentHigh) && (opponentRanking >= low && opponentRanking <= high)) {
                                    queue.match(queueProfile, opponent);
                                }

                            } else {
                                queue.match(queueProfile, opponent);
                            }
                        }


                        if ((roundedCurrent - roundedPrevious) % 5000 == 0 && range > starting) {
                            queueProfile.getRange()[0] = range;
                            player.sendMessage(
                                    main.getLangFile().getString("QUEUE.SEARCHING." + (queue.getType() == QueueType.RANKED ? "RANKED" : "UNRANKED"))
                                            .replace("%LADDER%", queue.getLadder().getName())
                                            .replace("%ELO%", ranking + "")
                                            .replace("%LOW_RANGE%", ranking - range + "")
                                            .replace("%HIGH_RANGE%", ranking + range + "") //TODO: Prevent low range being negative ugh shitters
                            );
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(main, 2L, 2L);
    }

}
