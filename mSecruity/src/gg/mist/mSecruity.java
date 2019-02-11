package gg.mist;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.ExecutionError;

import gg.mist.commands.BlacklistCommand;
import gg.mist.commands.IpWhitelist;
import gg.mist.commands.pin.LoginCommand;
import gg.mist.commands.pin.PinCommand;
import gg.mist.events.DB;
import gg.mist.listener.JoinManager;
import gg.mist.listener.SecurityChatTrigger;
import gg.mist.listener.SecurityCommandListener;
import lombok.Getter;

public class mSecruity extends JavaPlugin implements Listener
{

	@Getter public static mSecruity msecruity;
	@Getter public static DB db;

	public void onEnable()
	{
		msecruity = this;

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[mSecruity] Has been enabled with no issues...");

		setupFiles();
		this.db = new DB();

		getCommand("blacklist").setExecutor(new BlacklistCommand());
		getCommand("ipreset").setExecutor(new IpWhitelist());
		getCommand("pin").setExecutor(new LoginCommand());
		getCommand("setpin").setExecutor(new PinCommand());

		PluginManager m = Bukkit.getServer().getPluginManager();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		m.registerEvents(new SecurityCommandListener(), this);
		m.registerEvents(new LoginCommand(), this);
		m.registerEvents(new PinCommand(), this);
		m.registerEvents(new SecurityChatTrigger(), this);
		getServer().getPluginManager().registerEvents(new BlacklistCommand(), this);
		m.registerEvents(new JoinManager(), this);

		File file2 = new File(new File(msecruity.getDataFolder(), "data"), "blacklist.yml");
		if (!file2.exists())
		{
			createBlackListFile();
		}
	}

	public void onDisable()
	{
		try
		{
			msecruity = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void createBlackListFile()
	{
		try
		{
			FileConfiguration black = YamlConfiguration.loadConfiguration(new File(msecruity.getDataFolder(), "blacklist.yml"));
			File blacklistFile = new File(msecruity.getDataFolder(), "blacklist.yml");
			black.save(blacklistFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public FileConfigurationOptions getFileConfigurationOptions()
	{
		return getConfig().options();
	}

	private void setupFiles()
	{
		try
		{
			if (!getDataFolder().exists())
			{
				getDataFolder().mkdirs();
			}
			File file = new File(getDataFolder().getAbsolutePath(), "config.yml");
			File file2 = new File(getDataFolder().getAbsolutePath(), "pin.yml");
			File file3 = new File(getDataFolder().getAbsolutePath(), "fails.yml");
			File file4 = new File(getDataFolder().getAbsoluteFile(), "notes.yml");
			File file5 = new File(getDataFolder(), "reclaim.yml");
			if (!file.exists())
			{
				getFileConfigurationOptions().copyDefaults(true);
				saveConfig();
			}
			else
			{}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void createPinFile()
	{
		try
		{
			FileConfiguration pin = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "pin.yml"));
			File pinFile = new File(mSecruity.msecruity.getDataFolder(), "pin.yml");
			pin.save(pinFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createFailedFile()
	{
		try
		{
			FileConfiguration fail = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "fails.yml"));
			File failFile = new File(mSecruity.msecruity.getDataFolder(), "fails.yml");
			fail.save(failFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onMoveBeforeLoggedIn(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName()))
		{
			e.setTo(e.getFrom());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must enter your &a&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onChatBeforeLoggedIn(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName()))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must enter your &a&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onCommandBeforeLoggedIn(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		if ((!e.getMessage().startsWith("/pin")) && (LoginCommand.loggingin.contains(p.getName())))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must enter your &a&lPIN&e. &7&o(/pin <pin>)"));
		}
	}
}
