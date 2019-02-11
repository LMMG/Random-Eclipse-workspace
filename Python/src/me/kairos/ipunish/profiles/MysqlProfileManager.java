package me.kairos.ipunish.profiles;

import com.google.common.base.Joiner;
import me.kairos.ipunish.IPunish;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class MysqlProfileManager implements IProfileManager {

    private static final int THREAD_COUNT = 3;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(THREAD_COUNT);

    private PreparedStatement lookupProfileStatement;
    private final IPunish plugin;

    public MysqlProfileManager(IPunish plugin) {
        this.plugin = plugin;

        try {
            this.lookupProfileStatement = plugin.getBanDatabase().getConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Profile loadOrSaveProfile(UUID uuid, InetAddress address) {
        Profile profile = loadProfileSynchronously(uuid);
        if (profile == null) {
            profile = new Profile(uuid, address.getHostAddress().replace("/", ""), 0, 0, 0, 0);
            save(profile);
        }

        return profile;
    }

    @Override
    public Profile getProfile(UUID uuid) {
        return loadProfileSynchronously(uuid);
    }

    /**
     * Looks up a users' profile from the database synchronously.
     *
     * @param uuid the uuid of user to lookup
     * @return the returned profile
     */
    private Profile loadProfileSynchronously(UUID uuid) {
        Callable<Profile> profileCallable = new Callable<Profile>() {
            @Override
            public Profile call() throws Exception {
                return lookupProfile(uuid);
            }
        };

        Profile profile = null;
        Future<Profile> future = EXECUTOR.submit(profileCallable);
        try {
            profile = future.get();
        } catch (InterruptedException | ExecutionException ignored) {
        }

        return profile;
    }

    /**
     * Looks up a users' profile from the database.
     *
     * @param uuid the uuid of user to lookup
     * @return the returned profile
     */
    private Profile lookupProfile(UUID uuid) {
        Profile profile = null;
        ResultSet resultSet = null;
        try {
            lookupProfileStatement.setString(1, uuid.toString());
            resultSet = lookupProfileStatement.executeQuery();

            if (resultSet.next()) {
                profile = new Profile(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("ip"),
                        resultSet.getInt("kicks"), resultSet.getInt("bans"), resultSet.getInt("unbans"), resultSet.getInt("mutes"));

                int val = resultSet.getInt("banInit");
                profile.setBanStamp(val == -1L ? -1L : val * 1000L);

                val = resultSet.getInt("banLength");
                profile.setBanDurationMillis(val == -1 ? -1L : val * 1000L);

                val = resultSet.getInt("muteInit");
                profile.setMuteStamp(val == -1 ? -1L : val * 1000L);

                val = resultSet.getInt("muteLength");
                profile.setMuteDurationMillis(val == -1 ? -1L : val * 1000L);

                profile.setBanReason(resultSet.getString("banReason"));
                profile.setMuteReason(resultSet.getString("muteReason"));

                String id = resultSet.getString("bannerUUID");
                profile.setBannerUUID(id == null ? null : UUID.fromString(id));

                id = resultSet.getString("muterUUID");
                profile.setMuterUUID(id == null ? null : UUID.fromString(id));

                LinkedHashSet<UUID> ids = new LinkedHashSet<>();
                for (String alt : resultSet.getString("alts").split(",")) {
                    alt = alt.replace(",", "").replace(" ", "");
                    if (alt.length() == 36) ids.add(UUID.fromString(alt));
                }

                LinkedHashSet<String> ips = new LinkedHashSet<>();
                for (String ip : resultSet.getString("ips").split(",")) {
                    ips.add(ip.replace(",", "").replace(" ", ""));
                }

                profile.setAlts(ids);
                profile.setIps(ips);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return profile;
    }

    @Override
    public void save(Profile profile) {
        new BukkitRunnable() {
            @Override
            public void run() {
                storeProfile(profile);
            }
        }.runTaskAsynchronously(plugin);
    }

    private void storeProfile(Profile profile) {
        try {
            PreparedStatement statement = plugin.getBanDatabase().getConnection().prepareStatement("INSERT INTO players (" +
                    "uuid," +
                    "ip," +
                    "bans, " +
                    "unbans," +
                    "banLength," +
                    "muteLength," +
                    "bannerUUID," +
                    "muterUUID," +
                    "muteInit," +
                    "kicks," +
                    "mutes," +
                    "banInit," +
                    "banReason," +
                    "muteReason," +
                    "alts," +
                    "ips" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                    "uuid=?," +
                    "ip=?," +
                    "bans=?," +
                    "unbans=?," +
                    "banLength=?," +
                    "muteLength=?," +
                    "bannerUUID=?," +
                    "muterUUID=?," +
                    "muteInit=?," +
                    "kicks=?," +
                    "mutes=?," +
                    "banInit=?," +
                    "banReason=?," +
                    "muteReason=?," +
                    "alts=?," +
                    "ips=?");

            int i = 0;
            statement.setString(++i, profile.getUuid().toString());
            statement.setString(++i, profile.getLastIP());
            statement.setInt(++i, profile.getBanCount());
            statement.setInt(++i, profile.getUnbanCount());
            statement.setInt(++i, profile.getBanDurationMillis() == -1L ? -1 : (int) (profile.getBanDurationMillis() / 1000L));
            statement.setInt(++i, profile.getMuteDurationMillis() == -1L ? -1 : (int) (profile.getMuteDurationMillis() / 1000L));
            statement.setString(++i, profile.getBannerUUID() == null ? null : profile.getBannerUUID().toString());
            statement.setString(++i, profile.getMuterUUID() == null ? null : profile.getMuterUUID().toString());
            statement.setInt(++i, profile.getMuteStamp() == -1L ? -1 : (int) (profile.getMuteStamp() / 1000L));
            statement.setInt(++i, profile.getKickCount());
            statement.setInt(++i, profile.getMuteCount());
            statement.setInt(++i, profile.getBanStamp() == -1L ? -1 : (int) (profile.getBanStamp() / 1000L));
            statement.setString(++i, profile.getBanReason());
            statement.setString(++i, profile.getMuteReason());
            statement.setString(++i, Joiner.on(", ").join(profile.getAlts()));
            statement.setString(++i, Joiner.on(", ").join(profile.getIps()));
            statement.setString(++i, profile.getUuid().toString());
            statement.setString(++i, profile.getLastIP());
            statement.setInt(++i, profile.getBanCount());
            statement.setInt(++i, profile.getUnbanCount());
            statement.setInt(++i, profile.getBanDurationMillis() == -1L ? -1 : (int) (profile.getBanDurationMillis() / 1000L));
            statement.setInt(++i, profile.getMuteDurationMillis() == -1L ? -1 : (int) (profile.getMuteDurationMillis() / 1000L));
            statement.setString(++i, profile.getBannerUUID() == null ? null : profile.getBannerUUID().toString());
            statement.setString(++i, profile.getMuterUUID() == null ? null : profile.getMuterUUID().toString());
            statement.setInt(++i, profile.getMuteStamp() == -1L ? -1 : (int) (profile.getMuteStamp() / 1000L));
            statement.setInt(++i, profile.getKickCount());
            statement.setInt(++i, profile.getMuteCount());
            statement.setInt(++i, profile.getBanStamp() == -1L ? -1 : (int) (profile.getBanStamp() / 1000L));
            statement.setString(++i, profile.getBanReason());
            statement.setString(++i, profile.getMuteReason());
            statement.setString(++i, Joiner.on(", ").join(profile.getAlts()));
            statement.setString(++i, Joiner.on(", ").join(profile.getIps()));
            statement.executeUpdate();
        } catch (SQLException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save profile: " + profile.getUuid(), ex);
        }
    }
}
