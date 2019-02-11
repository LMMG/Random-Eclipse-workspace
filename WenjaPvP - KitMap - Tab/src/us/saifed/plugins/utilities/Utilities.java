package us.saifed.plugins.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.java.JavaPlugin;

import com.alexandeh.kraken.Kraken;

import server.wenjapvp.hcf.HCF;
import us.saifed.plugins.utilities.listeners.ColorUtils;
import us.saifed.plugins.utilities.listeners.TabListener;

public class Utilities extends JavaPlugin
{
	private static Utilities instance;

	private TabListener tabListener;

	private HCF hcf;

	public static Utilities getInstance()
	{
		if (instance == null)
		{
			instance = new Utilities();
		}
		return instance;
	}

	@Override
	public void onEnable()
	{
		setup();

	}

	@Override
	public void onDisable()
	{
		instance = null;

	}

	public void setup()
	{
		instance = this;

		setupFiles();

		new Kraken(this);

		hcf = JavaPlugin.getPlugin(HCF.class);

		tabListener = new TabListener();
		Bukkit.getServer().getPluginManager().registerEvents(tabListener, this);

		long timeMillis = System.currentTimeMillis();
		getConsoleSender().sendMessage("&9[Utilities] Plugin loaded in " + (System.currentTimeMillis() - timeMillis) + "ms.");
	}

	public HCF getHCF()
	{
		return hcf;
	}

	public ConsoleCommandSender getConsoleSender()
	{
		return Bukkit.getServer().getConsoleSender();
	}

	public List<String> getDevelopers()
	{
		return Arrays.asList(new String[] {
				"ItsArslann"
		});
	}

	public FileConfigurationOptions getFileConfigurationOptions()
	{
		return getConfig().options();
	}

	public String getString(String path)
	{
		if (getConfig().contains(path)) { return new ColorUtils().translateFromString(getConfig().getString(path)); }
		return new ColorUtils().translateFromString("&cString not found: '" + path + "'");
	}

	public List<String> getStringList(String path)
	{
		if (getConfig().contains(path))
		{
			ArrayList<String> lines = new ArrayList();
			for (String text : getConfig().getStringList(path))
			{
				lines.add(new ColorUtils().translateFromString(text));
			}
			return lines;
		}
		return Arrays.asList(new String[] {
				new ColorUtils().translateFromString("&cString list not found: '" + path + "'")
		});
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
			if (!file.exists())
			{
				getFileConfigurationOptions().copyDefaults(true);
				saveConfig();
				getConsoleSender().sendMessage(new ColorUtils().translateFromString("&6[Utilities] The config file was not detected, because of the file does not exist it will created."));
			}
			else
			{
				getConsoleSender().sendMessage(new ColorUtils().translateFromString("&a[Utilities] Successfully loaded all configuration files."));
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}