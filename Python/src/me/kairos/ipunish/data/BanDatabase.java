package me.kairos.ipunish.data;

import lombok.Getter;
import me.kairos.ipunish.IPunish;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a ban database.
 *
 * @author -_Husky_-
 * @author tips48
 * @author modified by DoctorDank
 */
public class BanDatabase {

    @Getter
    private Connection connection;

    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    /**
     * Creates a new MySQL instance
     *
     * @param hostname the name of the host
     * @param port     the port number
     * @param username the username
     * @param password the password
     */
    public BanDatabase(String hostname, String port, String username, String password) {
        this(hostname, port, null, username, password);
    }

    /**
     * Creates a new MySQL instance for a specific database
     *
     * @param hostname the name of the host
     * @param port     the port number
     * @param database the database name
     * @param username the username
     * @param password the password
     */
    public BanDatabase(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            IPunish.getInstance().getLogger().info("Attempting to connect to database.");
            boolean maria = IPunish.getInstance().getConfiguration().mariaDb;
            Class.forName(maria ? "org.mariadb.jdbc.Driver" : "com.mysql.jdbc.Driver");
            String connectionURL = "jdbc:" + (maria ? "mariadb" : "mysql") + "://" + this.hostname + ":" + this.port;
            if (database != null) {
                connectionURL = connectionURL + "/" + this.database;
            }

            this.connection = DriverManager.getConnection(connectionURL, this.user, this.password);
        }
    }

    public boolean isConnected() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    public void closeConnection() throws SQLException {
        if (this.isConnected()) {
            this.connection.close();
        }
    }

    public void createTable() throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected to database");
        }

        PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS players (uuid varchar(255)," +
                "ip varchar(255)," +
                "bans int," +
                "unbans int," +
                "muteLength int," +
                "banLength int," +
                "muteInit int," +
                "kicks int," +
                "mutes int," +
                "banInit long, " +
                "banReason varchar(255)," +
                "muteReason varchar(255)," +
                "alts text," +
                "ips text," +
                "bannerUUID varchar(255)," +
                "muterUUID varchar(255)," +
                "PRIMARY KEY(uuid));");

        statement.executeUpdate();
    }
}