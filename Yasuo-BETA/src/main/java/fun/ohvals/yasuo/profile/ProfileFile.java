package fun.ohvals.yasuo.profile;

import com.google.common.io.ByteStreams;
import fun.ohvals.yasuo.Yasuo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class ProfileFile {

    private Yasuo yasuo = Yasuo.getYasuo();

    private File file, profiles;
    private Configuration config;

    public ProfileFile(String name) {
        profiles = new File(yasuo.getDataFolder() + File.separator + "profiles");
        file = new File(profiles, name + ".yml");

        if (!yasuo.getDataFolder().exists()) {
            yasuo.getDataFolder().mkdirs();
        }

        if (!profiles.exists()) {
            profiles.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("[" + yasuo.getDescription().getName() + "" + yasuo.getDescription().getVersion() + "] was not able to create profile '" + file.getName() + "'. Printing stacktrace below...");
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            System.out.println("[" + yasuo.getDescription().getName() + "" + yasuo.getDescription().getVersion() + "] was not able to load profile '" + file.getName() + "'. Printing stacktrace below...");
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(getConfig(), file);
        } catch (IOException e) {
            System.out.println("[" + yasuo.getDescription().getName() + "" + yasuo.getDescription().getVersion() + "] was not able to save profile '" + file.getName() + "'. Printing stacktrace below...");
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    public String getString(String path) {
        if (getConfig().getString(path).length() > 0) {
            return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
        }
        return "ERROR: STRING NOT FOUND";
    }

    public List<String> getStringList(String path) {
        List<String> list = new ArrayList<>();
        if (getConfig().get(path) != null) {
            for (String string : getConfig().getStringList(path)) {
                list.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return list;
        }
        return list;
    }

    public boolean getBoolean(String path) {
        if (Boolean.parseBoolean(getConfig().getString(path))) {
            return getConfig().getBoolean(path);
        }
        return false;
    }

    public List<String> getReversedStringList(String path) {
        List<String> list = getStringList(path);
        if (list != null) {
            int size = list.size();
            List<String> toReturn = new ArrayList<>();
            for (int i = size - 1; i >= 0; i--) {
                toReturn.add(list.get(i));
            }
            return toReturn;
        }
        return null;
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

}
