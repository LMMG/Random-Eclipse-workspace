package me.kairos.ipunish;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.kairos.ipunish.commands.BanCommand;
import me.kairos.ipunish.commands.HistoryCommand;
import me.kairos.ipunish.commands.IPunishCommand;
import me.kairos.ipunish.commands.KickCommand;
import me.kairos.ipunish.commands.MuteCommand;
import me.kairos.ipunish.commands.PunishCommand;
import me.kairos.ipunish.commands.TempbanCommand;
import me.kairos.ipunish.commands.UnbanCommand;
import me.kairos.ipunish.commands.UnmuteCommand;
import me.kairos.ipunish.data.BanDatabase;
import me.kairos.ipunish.listeners.PlayerListeners;
import me.kairos.ipunish.profiles.IProfileManager;
import me.kairos.ipunish.profiles.MysqlProfileManager;

public class IPunish extends JavaPlugin
{

	public static final UUID CONSOLE_UUID = UUID.fromString("30a521d0-7179-410d-990f-c09f11276d17");
	public static final String BUNGEE_CHANNEL_NAME = "BungeeCord";

	private static final String NO_PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission to issue this command!";

	@Getter private static IPunish instance;

	@Getter private BanDatabase banDatabase;

	@Getter private IProfileManager profileManager;

	@Getter @Setter private Configuration configuration;

	@Override
	public void onEnable()
	{
		instance = this;

		getServer().getMessenger().registerOutgoingPluginChannel(this, BUNGEE_CHANNEL_NAME);
		saveDefaultConfig();
		this.configuration = new Configuration(this);

		// Attempt to connect to database.
		banDatabase = new BanDatabase(getConfig().getString("database.host"), getConfig().getString("database.port"), getConfig().getString("database.name"), getConfig().getString("database.login.username"), getConfig().getString("database.login.password"));
		try
		{
			banDatabase.connect();
			getLogger().info("Successfully connected to database");
		}
		catch (SQLException | ClassNotFoundException ex)
		{
			getLogger().log(Level.SEVERE, "Could not connect to database", ex);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Attempt to create database.
		try
		{
			banDatabase.createTable();
		}
		catch (SQLException ex)
		{
			getLogger().log(Level.WARNING, "Failed to create create ban database", ex);
		}

		// Register other necessities.
		this.registerManagers();
		this.registerListeners();
		this.registerCommands();
	}

	private void registerManagers()
	{
		this.profileManager = new MysqlProfileManager(this);
	}

	private void registerListeners()
	{
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new PlayerListeners(this), this);
	}

	private void registerCommands()
	{
		PluginCommand command = getCommand("ban");
		command.setExecutor(new BanCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.ban");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("history");
		command.setExecutor(new HistoryCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.history");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("ipunish");
		command.setExecutor(new IPunishCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.ipunish");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("kick");
		command.setExecutor(new KickCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.kick");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("mute");
		command.setExecutor(new MuteCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.mute");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("punish");
		command.setExecutor(new PunishCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.punish");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("tempban");
		command.setExecutor(new TempbanCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.tempban");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("unban");
		command.setExecutor(new UnbanCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.unban");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);

		command = getCommand("unmute");
		command.setExecutor(new UnmuteCommand(this));
		command.setTabCompleter(this);
		command.setPermission("python.unmute");
		command.setPermissionMessage(NO_PERMISSION_MESSAGE);
	}

	@Override
	public void onDisable()
	{
		try
		{
			banDatabase.closeConnection();
		}
		catch (SQLException ex)
		{
			getLogger().log(Level.SEVERE, "Failed to close connection", ex);
			ex.printStackTrace();
		}
	}

	public static UUID getUUID(CommandSender sender)
	{
		return sender instanceof Player ? ((Player) sender).getUniqueId() : CONSOLE_UUID;
	}
}
