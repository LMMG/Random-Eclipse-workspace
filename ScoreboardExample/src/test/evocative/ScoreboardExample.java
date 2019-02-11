package test.evocative;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import rip.evocative.PlayerBoard;
import test.evocative.listener.PlayerListener;
import test.evocative.listener.ScoreboardUpdateUtil;
import test.evocative.scoreboard.ScoreboardTimerProvider;

public class ScoreboardExample extends JavaPlugin implements Listener
{
	
	public static ScoreboardExample instance;
	
	public void onEnable()
	{
		instance = this;
		
		PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new PlayerListener(), this);
		
		new ScoreboardUpdateUtil().runTaskTimerAsynchronously(this, 2, 2L);
//		new ScoreboardUpdateUti
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			PlayerBoard.get(p).setProvider(ScoreboardTimerProvider.getInstance());
		}
	}
	
	public void onDisable()
	{
		instance = null; // for the skids
		PlayerBoard.dispose();
	}

	public static ScoreboardExample getInstance() {
		return instance;
	}
}
