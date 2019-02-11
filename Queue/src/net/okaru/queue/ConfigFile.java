package net.okaru.queue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigFile {
	private File file;
	private YamlConfiguration configuration;

	public ConfigFile(JavaPlugin plugin, String name) {
		this.file = new File(plugin.getDataFolder(), name + ".yml");
		if (!this.file.getParentFile().exists()) {
			this.file.getParentFile().mkdir();
		}
		plugin.saveResource(name + ".yml", false);
		this.configuration = YamlConfiguration.loadConfiguration(this.file);
	}

	public double getDouble(String path) {
		if (this.configuration.contains(path)) {
			return this.configuration.getDouble(path);
		}
		return 0.0;
	}

	public int getInt(String path) {
		if (this.configuration.contains(path)) {
			return this.configuration.getInt(path);
		}
		return 0;
	}

	public boolean getBoolean(String path) {
		if (this.configuration.contains(path)) {
			return this.configuration.getBoolean(path);
		}
		return false;
	}

	public String getString(String path) {
		if (this.configuration.contains(path)) {
			return ChatColor.translateAlternateColorCodes('&',
					this.configuration.getString(path));
		}
		return "ERROR: STRING NOT FOUND";
	}

	public List<String> getReversedStringList(String path) {
		List<String> list = this.getStringList(path);
		if (list != null) {
			int size = list.size();
			ArrayList<String> toReturn = new ArrayList<String>();
			for (int i = size - 1; i >= 0; --i) {
				toReturn.add(list.get(i));
			}
			return toReturn;
		}
		return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
	}

	public List<String> getStringList(String path) {
		if (this.configuration.contains(path)) {
			ArrayList<String> strings = new ArrayList<String>();
			for (String string : this.configuration.getStringList(path)) {
				strings.add(ChatColor.translateAlternateColorCodes('&', string));
			}
			return strings;
		}
		return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
	}

	public File getFile() {
		return this.file;
	}

	public YamlConfiguration getConfiguration() {
		return this.configuration;
	}
}
