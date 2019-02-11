package net.okaru.lobby.utils;

import java.io.File;

import org.bukkit.ChatColor;

import net.okaru.lobby.Lobby;

public class FileUtils {
	private final static Lobby lobbyInstance = Lobby.getInstance();

	public static void setupFiles() {
		try {
			if (!lobbyInstance.getDataFolder().exists()) {
				lobbyInstance.getDataFolder().mkdirs();
			}
			File file = new File(lobbyInstance.getDataFolder()
					.getAbsolutePath(), "config.yml");
			if (!file.exists()) {
				lobbyInstance.getFileConfigurationOptions().copyDefaults(true);
				lobbyInstance.saveConfig();
				System.out
						.println("[Lobby] The config file was not detected, because of the file does not exist it will created.");
			} else {
				System.out
						.println("[Lobby] Successfully loaded all configuration files.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static String getString(String path) {
		if (lobbyInstance.getConfig().contains(path)) {
			return ChatColor.translateAlternateColorCodes('&', lobbyInstance
					.getConfig().getString(path));
		}
		return ChatColor.translateAlternateColorCodes('&',
				"&cString not found: '" + path + "'");
	}

}
