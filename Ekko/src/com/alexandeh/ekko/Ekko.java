package com.alexandeh.ekko;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.ClaimListeners;
import com.alexandeh.ekko.factions.claims.ClaimPillar;
import com.alexandeh.ekko.factions.commands.*;
import com.alexandeh.ekko.factions.commands.admin.FactionAdminCommand;
import com.alexandeh.ekko.factions.commands.admin.FactionFreezeCommand;
import com.alexandeh.ekko.factions.commands.admin.FactionSetDtrCommand;
import com.alexandeh.ekko.factions.commands.admin.FactionThawCommand;
import com.alexandeh.ekko.factions.commands.leader.FactionDemoteCommand;
import com.alexandeh.ekko.factions.commands.leader.FactionDisbandCommand;
import com.alexandeh.ekko.factions.commands.leader.FactionLeaderCommand;
import com.alexandeh.ekko.factions.commands.leader.FactionPromoteCommand;
import com.alexandeh.ekko.factions.commands.officer.*;
import com.alexandeh.ekko.factions.commands.system.FactionColorCommand;
import com.alexandeh.ekko.factions.commands.system.FactionCreateSystemCommand;
import com.alexandeh.ekko.factions.commands.system.FactionToggleDeathbanCommand;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.files.ConfigFile;
import com.alexandeh.ekko.listeners.ChatListeners;
import com.alexandeh.ekko.listeners.SaveCommand;
import com.alexandeh.ekko.listeners.ScoreboardListeners;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.profiles.ProfileListeners;
import com.alexandeh.ekko.utils.command.CommandFramework;
import com.alexandeh.ekko.utils.player.PlayerUtility;
import com.alexandeh.ekko.utils.player.SimpleOfflinePlayer;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;

@Getter
public class Ekko extends JavaPlugin {

    private static Ekko instance;

    private CommandFramework framework;
    private ConfigFile mainConfig;
    private ConfigFile langConfig;
    private Economy economy;
    private MongoClient client;
    private MongoDatabase mongoDatabase;
    @Setter private boolean loaded;

    public void onEnable() {
        instance = this;

        mainConfig = new ConfigFile(this, "config");
        langConfig = new ConfigFile(this, "lang");

        setupDatabase();
        Faction.load();

        framework = new CommandFramework(this);
        economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        SimpleOfflinePlayer.load(this);
        PlayerFaction.runTasks(this);

        registerListeners();
        registerCommands();
    }

    public void onDisable() {
        Faction.save();

        for (Player player : PlayerUtility.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getClaimProfile() != null) {
                profile.getClaimProfile().removePillars();
            }
            for (ClaimPillar claimPillar : profile.getMapPillars()) {
                claimPillar.remove();
            }
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        try {
            SimpleOfflinePlayer.save(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.close();
    }

    private void registerCommands() {
        new FactionHelpCommand();
        new FactionDisbandCommand();
        new FactionCreateCommand();
        new FactionVersionCommand();
        new FactionInviteCommand();
        new FactionJoinCommand();
        new FactionRenameCommand();
        new FactionPromoteCommand();
        new FactionDemoteCommand();
        new FactionLeaderCommand();
        new FactionUninviteCommand();
        new FactionChatCommand();
        new FactionSetHomeCommand();
        new FactionMessageCommand();
        new FactionAnnouncementCommand();
        new FactionLeaveCommand();
        new FactionShowCommand();
        new FactionKickCommand();
        new FactionInvitesCommand();
        new FactionAllyCommand();
        new FactionEnemyCommand();
        new FactionDepositCommand();
        new FactionWithdrawCommand();
        new FactionClaimCommand();
        new FactionMapCommand();
        new FactionUnclaimCommand();
        new FactionListCommand();
        new FactionHomeCommand();
        new FactionStuckCommand();
        new FactionCreateSystemCommand();
        new FactionToggleDeathbanCommand();
        new FactionColorCommand();
        new FactionFreezeCommand();
        new FactionThawCommand();
        new FactionSetDtrCommand();
        new FactionAdminCommand();
        Bukkit.getPluginCommand("ekko").setExecutor(new SaveCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ProfileListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ClaimListeners(), this);
    }

    private void setupDatabase() {
        if (mainConfig.getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            client = new MongoClient(new ServerAddress(mainConfig.getString("DATABASE.MONGO.HOST"), mainConfig.getInt("DATABASE.MONGO.PORT")), Arrays.asList(MongoCredential.createCredential(mainConfig.getString("DATABASE.MONGO.AUTHENTICATION.USER"), mainConfig.getString("DATABASE.MONGO.AUTHENTICATION.DATABASE"), mainConfig.getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray())));
        } else {
            client = new MongoClient(new ServerAddress(mainConfig.getString("DATABASE.MONGO.HOST"), mainConfig.getInt("DATABASE.MONGO.PORT")));
        }

        mongoDatabase = client.getDatabase(mainConfig.getString("DATABASE.MONGO.DATABASE"));
    }

    public static Ekko getInstance() {
        return instance;
    }
}
