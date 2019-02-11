package us.kirai.bunkers.tab.api.example;

import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;

import us.kirai.bunkers.tab.api.TablistManager;

public class TablistPlugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		new TablistManager(this, new ExampleSupplier(), TimeUnit.SECONDS.toMillis(5l));
	}

}
