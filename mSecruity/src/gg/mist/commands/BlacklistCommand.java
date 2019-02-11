package gg.mist.commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import gg.mist.mSecruity;
import gg.mist.utils.UUIDFetcher;

public class BlacklistCommand implements Listener, CommandExecutor
{
	private File blacklistFile;

	public BlacklistCommand()
	{
		this.blacklistFile = new File(mSecruity.msecruity.getDataFolder(), "blacklist.yml");
	}

	public void blackListPlayer(String string) throws IOException
	{
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "blacklist.yml"));
		data.set(String.valueOf(string), Integer.valueOf(1));
		data.save(this.blacklistFile);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player) sender;
		if ((cmd.getName().equalsIgnoreCase("blacklist")) && (p.isOp()))
		{
			if (args.length != 1)
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/blacklist [player]"));
			}
			else if (args.length == 1)
			{
				String player = args[0];
				UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(new String[] {
						player
				}));
				Map<String, UUID> response = null;
				try
				{
					response = fetcher.call();
					blackListPlayer(String.valueOf(response));
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4" + sender.getName() + " &chas blacklisted &4" + args[0] + " &cfrom the network."));
					sender.sendMessage("");
					sender.sendMessage("§4*§c Blacklisted by: §4 " + sender.getName());
					sender.sendMessage("§4*§c Reason: Blacklisted");
					sender.sendMessage("§4*§c Blacklisted user has been saved to the database §7(Contant owner for unban)");
					if ((Bukkit.getPlayer(args[0]) != null) && (Bukkit.getPlayer(args[0]).isOnline()))
					{
						Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYour account is blacklisted from the Mist Network. This type of punishment cannot be appealed."));
					}
				}
				catch (Exception e)
				{
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAn error occured."));
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLoginBlackListed(PlayerLoginEvent e)
	{
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "blacklist.yml"));
		Player p = e.getPlayer();
		UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(new String[] {
				p.getName()
		}));
		Map<String, UUID> response = null;
		try
		{
			response = fetcher.call();
			if (data.getInt(String.valueOf(response)) == 1)
			{
				e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', "&cYour account is blacklisted from the Mist Network. This type of punishment cannot be appealed."));
			}
		}
		catch (Exception e2)
		{
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cError occured"));
			e2.printStackTrace();
		}
	}
}