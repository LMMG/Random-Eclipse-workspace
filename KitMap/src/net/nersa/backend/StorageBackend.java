package net.nersa.backend;

import org.bukkit.entity.Player;

public interface StorageBackend {
    
    void closeConnections();
    
    void createProfile(Player player);
    
    void saveProfile(Player player);

    void insertKill(Player killer, Player dead);
    
}