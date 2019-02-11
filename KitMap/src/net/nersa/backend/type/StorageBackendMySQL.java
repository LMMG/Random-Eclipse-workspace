package net.nersa.backend.type;

import net.nersa.backend.DatabaseCredentials;
import net.nersa.backend.StorageBackend;
import net.nersa.backend.connection.ConnectionPoolManager;
import net.nersa.kitmap.HCF;
import net.nersa.player.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class StorageBackendMySQL implements StorageBackend {
    
    private ConnectionPoolManager poolManager;
    
    public StorageBackendMySQL(DatabaseCredentials credentials) {
        this.poolManager = new ConnectionPoolManager(HCF.getInstance(), credentials);
    }
    
    @Override
    public void closeConnections() {
        this.poolManager.closePool();
    }
    
    @Override
    public void createProfile(Player player) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                String update = "INSERT INTO `kitmap_statistics` (`player_name`, `player_uuid`) VALUES (?, ?) ON DUPLICATE KEY UPDATE player_name = '" + player.getName() + "'";
                PreparedStatement statement = null;
                
                try {
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement(update);
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                        e.printStackTrace();
                        this.cancel();
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(HCF.getInstance());
    }
    
    @Override
    public void saveProfile(Player player) {
        new BukkitRunnable() {
            public void run() {
                PlayerData playerData = HCF.getInstance().getPlayerManager().getPlayerData(player);

                if (playerData == null) {
                    //HCF.getInstance().getLogger().severe("Failed saveProfile (no data) -> " + player.getName());
                	System.out.println("[Kits] (MySQL) Failed to save the profile of '" + player.getName() + "' due to no data being provided.");
                    return;
                }
                
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("UPDATE `kitmap_statistics` SET `player_name`=?, `playtime`=?, `kills`=?, `deaths`=?, `balance`=? WHERE `player_uuid`=?");
                    statement.setString(1, player.getName());
                    statement.setLong(2, HCF.getInstance().getPlayTimeManager().getTotalPlayTime(player.getUniqueId()));
                    statement.setInt(3, playerData.getKills());
                    statement.setInt(4, playerData.getDeaths());
                    statement.setInt(5, HCF.getInstance().getEconomyManager().getBalance(player.getUniqueId()));
                    statement.setString(6, playerData.getPlayer().getUniqueId().toString());
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                    //HCF.getInstance().getLogger().severe("Failed saveProfile (exception) -> " + player.getName());
                	System.out.println("[Kits] (MySQL) Failed to save the profile of '" + player.getName() + "' due to an SQL Exception.");
                    e.printStackTrace();
                    this.cancel();
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(HCF.getInstance());
    }

    @Override
    public void insertKill(Player killer, Player dead) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("INSERT INTO `kitmap_kills` (`killer_name`, `killer_uuid`, `dead_name`, `dead_uuid`) VALUES (?, ?, ?, ?)");

                    if(killer == null) {
                        statement.setString(1, "environment");
                        statement.setString(2, "");
                    } else {
                        statement.setString(1, killer.getName());
                        statement.setString(2, killer.getUniqueId().toString());
                    }

                    statement.setString(3, dead.getName());
                    statement.setString(4, dead.getUniqueId().toString());
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                    //HCF.getInstance().getLogger().severe("Failed insertKill -> " + dead.getName());
                    System.out.println("[Kits] (MySQL) Failed to save the kill of '" + killer.getName() + "' due to an SQL Exception..");
                    e.printStackTrace();
                    this.cancel();
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(HCF.getInstance());
    }

}