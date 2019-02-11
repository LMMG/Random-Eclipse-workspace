package net.nersa.player;

import lombok.Getter;
import net.nersa.kitmap.HCF;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {

    public File configFile;
    @Getter private FileConfiguration config;

    public PlayerConfig(File file, String fileName) {
        configFile = new File(file, fileName);

        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();

            if(HCF.getInstance().getResource(fileName) == null) {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                HCF.getInstance().saveResource(fileName, false);
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            //Bukkit.getLogger().severe("Could not save config file " + configFile.toString());
        	System.out.println("[Kits] (Flatfile) Failed to save config file '" + configFile.toString() + "' due to unknown.");
            e.printStackTrace();
        }
    }

}