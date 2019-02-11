package com.hcempire.horus.util.database;

import com.hcempire.horus.Horus;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

import java.util.Arrays;

public class HorusDatabase {

    @Getter private MongoClient client;
    @Getter private MongoDatabase database;
    @Getter private MongoCollection profiles;
    @Getter private MongoCollection fights;
    @Getter private MongoCollection koths;
    @Getter private MongoCollection crates;

    public HorusDatabase(Horus main) {
        if (main.getConfigFile().getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            client = new MongoClient(new ServerAddress(main.getConfigFile().getString("DATABASE.MONGO.HOST"), main.getConfigFile().getInt("DATABASE.MONGO.PORT")), Arrays.asList(MongoCredential.createCredential(main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.USER"), main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.DATABASE"), main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray())));
        } else {
            client = new MongoClient(new ServerAddress(main.getConfigFile().getString("DATABASE.MONGO.HOST"), main.getConfigFile().getInt("DATABASE.MONGO.PORT")));
        }

        database = client.getDatabase("horus");
        profiles = database.getCollection("profiles");
        fights = database.getCollection("fights");
        koths = database.getCollection("koths");
        crates = database.getCollection("crates");
    }

}
