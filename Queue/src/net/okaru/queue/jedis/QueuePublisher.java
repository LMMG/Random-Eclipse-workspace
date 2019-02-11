package net.okaru.queue.jedis;

import net.okaru.queue.oQueue;
import redis.clients.jedis.Jedis;

public class QueuePublisher {
    private oQueue main;

    public QueuePublisher(oQueue main) {
        this.main = main;
    }

    public void write(String message) {
        Jedis jedis = null;
        try {
            jedis = this.main.getPool().getResource();
            if (this.main.getConfigFile().getBoolean("DATABASE.AUTHENTICATION.ENABLED")) {
                jedis.auth(this.main.getConfigFile().getString("DATABASE.AUTHENTICATION.PASSWORD"));
            }
            jedis.publish("queue_server", message);
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}

