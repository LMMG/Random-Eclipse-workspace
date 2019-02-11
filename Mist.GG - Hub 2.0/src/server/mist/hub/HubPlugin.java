package server.mist.hub;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class HubPlugin extends JavaPlugin implements Listener
{
	
	@Getter public static HubPlugin instance;

	public void onEnable() {
		instance = this;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "HubPlugin is now being enabled @ Author: Addons, Version: 0.1-STABLE");
		
		loadCommands();
		loadCommands();
		loadListeners();
		loadConfigs();
	}
	
	public void loadEssentials() {
		
	}
	
	public void loadConfigs() {
		
	}
	
	public void loadCommands() {
		
	}
		
	public void loadListeners() {
		PluginManager manager = Bukkit.getServer().getPluginManager();
	}
	
	public void onDisable() {
		instance = null; // for the skids
	}
}
