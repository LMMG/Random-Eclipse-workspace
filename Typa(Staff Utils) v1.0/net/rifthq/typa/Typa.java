package net.rifthq.typa;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mysql.jdbc.Connection;

import net.md_5.bungee.api.ChatColor;
import net.rifthq.typa.commands.LoginCommand;
import net.rifthq.typa.commands.RegisterCommand;
import net.rifthq.typa.commands.ResetIPCommand;
import net.rifthq.typa.commands.ResetPlayerCommand;
import net.rifthq.typa.listeners.PlayerLoginListeners;
import net.rifthq.typa.listeners.PlayerMoveListeners;
import net.rifthq.typa.managers.MySQL;
import net.rifthq.typa.utils.Lists;

public class Typa extends JavaPlugin {
	private static Typa instance;
	public String host;
	public String username;
	public String password;
	public String database;
	public static String table;
	public int port;
	public static Connection connection;

	public static Typa getInstance() {
		if (instance == null) {
			instance = new Typa();
		}
		return instance;
	}

	public void onEnable() {
		this.setup();
	}

	public void onDisable() {
		instance = null;
		this.saveConfig();
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				getServer().getConsoleSender().sendMessage("[Typa] Successfully closed connection to SQL.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void setup() {
		instance = this;
		this.setupFiles();
		setupMySQL();
		loadCommands();
		loadListeners();
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (online.hasPermission("typa.auth"))
				Lists.toLogin.add(online.getName());
			online.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 9999));
			online.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 9999));
			online.sendMessage(ChatColor.RED + "A server reload has caused you to be placed in authentication mode!");

		}
	}

	public void loadCommands() {
		getCommand("register").setExecutor(new RegisterCommand());
		getCommand("login").setExecutor(new LoginCommand());
		getCommand("resetpin").setExecutor(new ResetPlayerCommand());
		getCommand("resetplayer").setExecutor(new ResetIPCommand());
	}

	public void loadListeners() {
		getServer().getPluginManager().registerEvents(new PlayerLoginListeners(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListeners(), this);
	}

	public ConsoleCommandSender getConsoleSender() {
		return Bukkit.getServer().getConsoleSender();
	}

	public FileConfigurationOptions getFileConfigurationOptions() {
		return this.getConfig().options();
	}

	private void setupFiles() {
		try {
			if (!this.getDataFolder().exists()) {
				this.getDataFolder().mkdirs();
			}
			if (!(new File(this.getDataFolder().getAbsolutePath(), "config.yml")).exists()) {
				this.getFileConfigurationOptions().copyDefaults(true);
				this.saveConfig();
				this.getConsoleSender().sendMessage("[Typa] The config file was not detected, because of the file does not exist it will created.");
			} else {
				this.getConsoleSender().sendMessage("[Typa] Successfully loaded all configuration files.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void setupMySQL() {
		host = this.getConfig().getString("host");
		port = this.getConfig().getInt("port");
		username = this.getConfig().getString("username");
		password = this.getConfig().getString("password");
		database = this.getConfig().getString("database");
		table = this.getConfig().getString("table");
		try {
			Typa login = this;
			synchronized (login) {
				if (this.getConnection() != null && !this.getConnection().isClosed()) {
					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				this.setConnection((Connection) DriverManager.getConnection(
						"jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username,
						this.password));
			MySQL.setupTables();
				getServer().getConsoleSender()
						.sendMessage("[Typa] Successfully connected to SQL Database using setttings: " + "jdbc:mysql://"
								+ this.host + ":" + this.port + "/" + this.database + " using user" + this.username
								+ " using password YES. ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.getServer().shutdown();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		Typa.connection = connection;
	}

}
