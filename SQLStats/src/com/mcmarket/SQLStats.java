package com.mcmarket;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcmarket.listener.StatsListener;
import com.mcmarket.sql.MySQL;

public class SQLStats extends JavaPlugin implements Listener {

	public static SQLStats INSTANCE;
	public static MySQL mysql;
	
	public static SQLStats getINSTANCE() {
		return INSTANCE;
	}

	public void onEnable()
	{
		INSTANCE = this;
		
		ConnectMySQL();
		
		getCommand("stats").setExecutor(new StatsListener());
		Bukkit.getServer().getPluginManager().registerEvents(new StatsListener(), this);
	}
	
	public void ConnectMySQL() {
		mysql = new MySQL("149.56.99.79", "kills", "root", "9ivRYx1s");
		mysql.update("CREATE TABLE IF NOT EXISTS Stats(UUID varchar(64), KILLS int, DEATHS int);");
		
	}
	
}
