package org.hcgames.icefyre.game.queue;

import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.game.fight.FightType;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.ItemBuilder;
import org.hcgames.icefyre.util.file.ConfigFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Queue {

    private static List<Queue> queues = new ArrayList<>();
    private static Icefyre main = Icefyre.getInstance();
    private static ConfigFile configFile = main.getConfigFile();
    private static ConfigFile langFile = main.getLangFile();

    @Getter private Ladder ladder;
    @Getter private QueueType type;
    @Getter private List<Player> playersInArena, playersInQueue;

    public Queue(Ladder ladder, QueueType type) {
        this.ladder = ladder;
        this.type = type;
        this.playersInArena = new ArrayList<>();
        this.playersInQueue = new ArrayList<>();

        queues.add(this);
    }

    public Set<QueueProfile> getQueueProfiles() {
        Set<QueueProfile> toReturn = new HashSet<>();

        for (Player player : getPlayersInQueue()) {
            Profile profile = Profile.getByPlayer(player);
            if (profile != null) {
                QueueProfile queueProfile = profile.getQueueProfile();
                if (queueProfile != null) {
                    toReturn.add(queueProfile);
                }
            }
        }

        return toReturn;
    }

    public void match(QueueProfile queueprofile, QueueProfile opponent) {
        Profile profile = queueprofile.getProfile();
        Profile opponentProfile = opponent.getProfile();

        profile.setQueueProfile(null);
        opponentProfile.setQueueProfile(null);

        Player player = profile.getPlayer();
        Player opponentPlayer = opponentProfile.getPlayer();

        playersInQueue.removeAll(Arrays.asList(player, opponentPlayer));

        Arena arena = ladder.getRandomArena();

        if (arena == null || arena.getLocations()[0] == null || arena.getLocations()[1] == null || ladder.getDefaultKit() == null) {
            for (Player recipient : new HashSet<>(Arrays.asList(player, opponentPlayer))) {
                recipient.sendMessage(langFile.getString("QUEUE.ERROR.LADDER_NOT_SET_UP").replace("%LADDER%", ladder.getName()));
                main.getLobby().teleportToSpawn(recipient);
                main.getLobby().setupPlayer(recipient);
            }
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                new Fight(new Profile[]{profile, opponentProfile}, ladder, arena, type == QueueType.UNRANKED ? FightType.UNRANKED : FightType.RANKED);
            }
        }.runTask(main);
    }

    public void remove(Profile profile) {
        profile.setState(ProfileState.IN_LOBBY);
        profile.setQueueProfile(null);
        playersInQueue.remove(profile.getPlayer());
        main.getLobby().setupPlayer(profile.getPlayer());
    }

    public static Queue getByPlayer(Player player) {
        for (Queue queue : queues) {
            if (queue.playersInArena.contains(player) || queue.getPlayersInQueue().contains(player)) {
                return queue;
            }
        }
        return null;
    }

    public static Queue getByLadder(Ladder ladder, QueueType queueType) {
        for (Queue queue : queues) {
            if (queue.getLadder().equals(ladder) && queue.getType() == queueType) {
                return queue;
            }
        }
        return null;
    }

    public static Inventory getGraphicalInterface(QueueType type) {
        int size = (int) (9*(Math.ceil(Math.abs(getQueues(type).size()/9))));

        if (size <= 9) {
            size = 9;
        }

        Inventory inventory = Bukkit.createInventory(null, size, langFile.getString("QUEUE.INVENTORY." + (type == QueueType.RANKED ? "RANKED" : "UNRANKED") + ".TITLE"));

        for (Queue queue : getQueues(type)) {
            ItemStack icon = queue.getLadder().getIcon().clone();
            List<String> lore = new ArrayList<>();

            for (String string : icon.getItemMeta().getLore()) {
                lore.add(string.replace("%PLAYERS_IN_ARENA%", queue.getPlayersInArena().size() + "").replace("%PLAYERS_IN_QUEUE%", queue.getPlayersInQueue().size() + ""));
            }

            inventory.addItem(new ItemBuilder(icon).lore(lore).build());
        }

        return inventory;
    }

    public static List<Queue> getQueues(QueueType type) {
        List<Queue> toReturn = new ArrayList<>();

        for (Queue queue : queues) {
            if (queue.getType() == type) {
                toReturn.add(queue);
            }
        }

        return toReturn;
    }

    public static List<Queue> getQueues() {
        return queues;
    }
}
