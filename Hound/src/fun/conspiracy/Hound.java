package fun.conspiracy;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fun.conspiracy.listener.ServerSelector;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class Hound extends JavaPlugin implements Listener {

	@Getter
	public static Hound hound;
	public static FileConfiguration config;
	public static File conf;
	public static FileConfiguration lang;
	public static File lanf;

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Hound] Loading instance...");
		hound = this;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Hound] Loading Config...");
		config = getConfig();
		config.options().copyDefaults(true);
		conf = new File("config.yml");
		saveConfig();
		saveDefaultConfig();
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Hound] Loading Lang...");
		lang = getConfig();
		lang.options().copyDefaults(true);
		lanf = new File("config.yml");
		saveConfig();
		saveDefaultConfig();
		
		//Listeners
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Hound] Loading Listeners...");
		PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new ServerSelector(), this);
		
		//Commands
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Hound] Loading Commands...");
	}

	public void onDisable() {

	}
}
