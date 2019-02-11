package io.hcnation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;

public class Core extends JavaPlugin implements Listener
{
	
	@Getter public static Core instance;

	public void onEnable() {
		instance = this;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
						Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Autosave is commencing...");
						Bukkit.broadcastMessage(ChatColor.GREEN + "Autosave is now done...");
						Bukkit.getWorld("world").save();
						Bukkit.dispatchCommand(player, "save-all");
				}
			}
		}.runTaskTimerAsynchronously(instance, 3600L, 3600L);
	}
	
	public void onDisable()
	{
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		Bukkit.getLogger().severe("msg: " + event.getMessage());
		if ((event.getMessage().startsWith("/reload")))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		}
	}
}
