package rip.kirai;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import rip.kirai.commands.ChangePinCommand;
import rip.kirai.commands.DeletePinCommand;
import rip.kirai.commands.LoginCommand;
import rip.kirai.commands.RegisterCommand;
import rip.kirai.database.MongoDB;
import rip.kirai.listeners.AuthListener;

public class Katamine extends JavaPlugin implements Listener
{

	public static Katamine instance;

	public void onEnable()
	{
		instance = this;

		MongoDB db = new MongoDB();
		db.connect("127.0.0.1", 27017);
		
		loadListeners();
		loadCommands();
	}

	public void onDisable()
	{
		instance = null;
	}

	public void loadListeners()
	{
		PluginManager manager = Bukkit.getServer().getPluginManager();
		manager.registerEvents(new AuthListener(), this);
		manager.registerEvents(new LoginCommand(), this);
	}

	public void loadCommands()
	{
		getCommand("Register").setExecutor(new RegisterCommand());
		getCommand("login").setExecutor(new LoginCommand());
		getCommand("changepin").setExecutor(new ChangePinCommand());
		getCommand("deletepin").setExecutor(new DeletePinCommand());
	}
}
