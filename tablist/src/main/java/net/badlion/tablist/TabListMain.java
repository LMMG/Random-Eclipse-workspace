package net.badlion.tablist;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class TabListMain extends JavaPlugin implements Listener {

	@Getter public static TabListMain geTabListMain;
	
	public void onEnable() {
		geTabListMain = this;
		
		this.getServer().getPluginManager().registerEvents(new TabListManager(this), this);
	}
	
}
