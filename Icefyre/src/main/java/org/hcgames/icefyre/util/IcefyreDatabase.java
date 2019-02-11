package org.hcgames.icefyre.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.hcgames.icefyre.Icefyre;

import java.util.Arrays;

public class IcefyreDatabase {

    @Getter private MongoClient client;
    @Getter private MongoDatabase database;
    @Getter private MongoCollection profiles;

    public IcefyreDatabase(Icefyre main) {

        if (main.getConfigFile().getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            client = new MongoClient(new ServerAddress(main.getConfigFile().getString("DATABASE.MONGO.HOST"), main.getConfigFile().getInt("DATABASE.MONGO.PORT")), Arrays.asList(MongoCredential.createCredential(main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.USER"), main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.DATABASE"), main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray())));
        } else {
            client = new MongoClient(new ServerAddress(main.getConfigFile().getString("DATABASE.MONGO.HOST"), main.getConfigFile().getInt("DATABASE.MONGO.PORT")));
        }

        database = client.getDatabase(main.getConfigFile().getString("DATABASE.MONGO.DATABASE"));
        profiles = database.getCollection("profiles");
    }

}
