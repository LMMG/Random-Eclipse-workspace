package net.rifthq.typa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;
import net.rifthq.typa.listeners.PlayerBuildListeners;
import net.rifthq.typa.listeners.PlayerCommandListener;
import net.rifthq.typa.listeners.PlayerDamageListener;
import net.rifthq.typa.listeners.PlayerIntractListener;
import net.rifthq.typa.listeners.PlayerJoinListener;
import net.rifthq.typa.listeners.PlayerLeaveListener;
import net.rifthq.typa.listeners.PlayerMoveListener;
import net.rifthq.typa.managers.MySQL;

public class Typa extends JavaPlugin
{
	private static Typa instance;
	public static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	public static PluginManager pm = Bukkit.getServer().getPluginManager();
	public String host;
	public String username;
	public String password;
	public String database;
	public String table;
	public int port;
	private Connection connection;
	private static Permission perms = null;
	public static ArrayList<String> logged_in = new ArrayList();

	public static Typa getInstance()
	{
		if (instance == null)
		{
			instance = new Typa();
		}
		return instance;
	}

	@Override
	public void onEnable()
	{
		instance = this;
		console.sendMessage(ChatColor.GREEN + "[Typa] Has enabled 100%");
		this.MySQLSetup();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		setupPermissions();
		pm.registerEvents(new PlayerMoveListener(), this);
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerLeaveListener(), this);
		pm.registerEvents(new PlayerDamageListener(), this);
		pm.registerEvents(new PlayerBuildListeners(), this);
		pm.registerEvents(new PlayerCommandListener(), this);
		pm.registerEvents(new PlayerIntractListener(), this);
	}

	@Override
	public void onDisable()
	{
		instance = null;
		this.saveConfig();
		try
		{
			if (this.connection != null && !this.connection.isClosed())
			{
				this.connection.close();
				console.sendMessage("[Typa] Successfully closed connection to SQL.");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void MySQLSetup()
	{
		this.host = this.getConfig().getString("host");
		this.port = this.getConfig().getInt("port");
		this.username = this.getConfig().getString("username");
		this.password = this.getConfig().getString("password");
		this.database = this.getConfig().getString("database");
		this.table = this.getConfig().getString("table");
		try
		{
			Typa login = this;
			synchronized (login)
			{
				if (this.getConnection() != null && !this.getConnection().isClosed()) { return; }
				Class.forName("com.mysql.jdbc.Driver");
				this.setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
				console.sendMessage("[Typa] Successfully connected to SQL.");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}

	public Connection getConnection()
	{
		return this.connection;
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("register"))
		{

			if (sender instanceof Player)
			{
				if (!sender.hasPermission("rifthq.register"))
				{
					sender.sendMessage(ChatColor.RED + "You are already authenticated as a " + Typa.getPermissions().getPrimaryGroup(p) + ".");
					return true;
				}
				if (args.length == 1)
				{
					if (args[0].length() > 25)
					{
						p.sendMessage(ChatColor.RED + "Your password must contain less than 50 characters!");
						return true;
					}
					if (args[0].length() < 5)
					{
						p.sendMessage(ChatColor.RED + "Your password must contain more than 5 characters!");
						return true;
					}
					MySQL.registerPlayer(p.getUniqueId(), p, args[0], p.getAddress().getHostName().toString());
				}
				else
				{
					p.sendMessage(ChatColor.RED + "/register <password>");
				}
			}
			else
			{
				sender.sendMessage("Player Only Command!");
			}
			return true;
		}
		if (label.equalsIgnoreCase("login"))
		{
			if (sender instanceof Player)
			{
				if (!sender.hasPermission("rifthq.register"))
				{
					sender.sendMessage(ChatColor.RED + "You are already authenticated as a " + Typa.getPermissions().getPrimaryGroup(p) + ".");
					return true;
				}
				if (logged_in.contains(p.getName()))
				{
					p.sendMessage(ChatColor.RED + "You are already logged in!");
					return true;
				}
				if (args.length == 1)
				{
					MySQL.playerLogin(p.getUniqueId(), p, args[0]);
				}
				else
				{
					p.sendMessage(ChatColor.RED + "/login <password>");
				}
			}
			else
			{
				sender.sendMessage("Player Only Command!");
			}
		}
		return false;
	}

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public static Permission getPermissions()
	{
		return perms;
	}
}
