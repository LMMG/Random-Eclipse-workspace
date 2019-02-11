package net.okaru.queue.jedis;

import java.util.UUID;

import net.okaru.queue.oQueue;
import net.okaru.queue.queue.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class QueueSubscriber {
    private JedisPubSub jedisPubSub;
    private Jedis jedis;
    private oQueue main;

    public QueueSubscriber(oQueue main) {
        this.main = main;
        this.jedis = new Jedis(main.getAddress());
        if (main.getConfigFile().getBoolean("DATABASE.AUTHENTICATION.ENABLED")) {
            this.jedis.auth(main.getConfigFile().getString("DATABASE.AUTHENTICATION.PASSWORD"));
        }
        this.subscribe();
    }

    public void subscribe() {
        this.jedisPubSub = this.get();
        new Thread(){

            @Override
            public void run() {
                QueueSubscriber.this.jedis.subscribe(QueueSubscriber.this.jedisPubSub, "queue_client");
            }
        }.start();
    }

    private JedisPubSub get() {
        return new JedisPubSub(){

            @Override
            public void onMessage(String channel, String message) {
                int position;
                if (message.equalsIgnoreCase("bye")) {
                    QueueSubscriber.this.main.setOffline(true);
                    Bukkit.broadcastMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.SERVER_FAILURE"));
                    for (Queue queue : Queue.getQueues()) {
                        queue.getPlayers().clear();
                    }
                    return;
                }
                String[] parts = message.split(";");
                Queue queue = Queue.getByName(parts[0]);
                if (queue == null) {
                    return;
                }
                if (QueueSubscriber.this.main.isOffline()) {
                    Bukkit.broadcastMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.SERVER_BACK_UP"));
                    QueueSubscriber.this.main.setOffline(false);
                }
                if (parts[1].equals("info")) {
                    for (int i = 2; i < parts.length; ++i) {
                        String rank = parts[i].split(":")[0];
                        int amount = Integer.parseInt(parts[i].split(":")[1]);
                        queue.getInfo().put(rank, amount);
                    }
                }
                if (parts[1].equals("data")) {
                    int totalInQueue = Integer.parseInt(parts[2]);
                    int playersOnServer = Integer.parseInt(parts[3]);
                    int maxPlayers = Integer.parseInt(parts[4]);
                    int whitelisted = Integer.parseInt(parts[5]);
                    int offline = Integer.parseInt(parts[6]);
                    if (queue.getData()[3] == 0 && whitelisted == 1) {
                        for (Player player : queue.getPlayerSet()) {
                            player.sendMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.KICKED_WHITELISTED"));
                        }
                        queue.getPlayers().clear();
                    }
                    if (queue.getData()[4] == 0 && offline == 1) {
                        for (Player player : queue.getPlayerSet()) {
                            player.sendMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.KICKED_OFFLINE"));
                        }
                        queue.getPlayers().clear();
                    }
                    queue.getData()[0] = totalInQueue;
                    queue.getData()[1] = playersOnServer;
                    queue.getData()[2] = maxPlayers;
                    queue.getData()[3] = whitelisted;
                    queue.getData()[4] = offline;
                }
                if (parts[1].equals("added")) {
                    UUID uuid = UUID.fromString(parts[2]);
                    position = Integer.valueOf(parts[3]);
                    for (Player other : queue.getPlayerSet()) {
                        if (queue.getPlayers().get(other.getUniqueId()) < position || other.getUniqueId().equals(uuid)) continue;
                        queue.getPlayers().put(other.getUniqueId(), queue.getPlayers().get(other.getUniqueId()) + 1);
                    }
                    queue.getPlayers().put(uuid, position);
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        player.sendMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.POSITION").replace("%POSITION%", queue.getPlayers().get(player.getUniqueId()) + "").replace("%TOTAL%", "" + queue.getData()[0] + "").replace("%QUEUE%", queue.getName()));
                        if (QueueSubscriber.this.main.getRank(player).equalsIgnoreCase("Normal")) {
                            player.sendMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.NO_RANK"));
                        }
                    }
                }
                if (parts[1].equals("removed")) {
                    UUID uuid = UUID.fromString(parts[2]);
                    position = Integer.valueOf(parts[3]);
                    boolean sendToServer = Boolean.valueOf(parts[4]);
                    if (queue.getPlayers().containsKey(uuid)) {
                        queue.getPlayers().remove(uuid);
                        Player player = Bukkit.getPlayer(uuid);
                        if (sendToServer && player != null) {
                            player.sendMessage(QueueSubscriber.this.main.getConfigFile().getString("MESSAGES.SENT").replace("%QUEUE%", queue.getName()));
                            queue.sendPlayer(player);
                        }
                    }
                    for (Player other : queue.getPlayerSet()) {
                        if (queue.getPlayers().get(other.getUniqueId()) <= position) continue;
                        queue.getPlayers().put(other.getUniqueId(), queue.getPlayers().get(other.getUniqueId()) - 1);
                    }
                }
            }
        };
    }

    public JedisPubSub getJedisPubSub() {
        return this.jedisPubSub;
    }

    public Jedis getJedis() {
        return this.jedis;
    }

}

