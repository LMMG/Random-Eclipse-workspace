package gg.mist.commands.pin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import gg.mist.mSecruity;

public class LoginCommand implements Listener, CommandExecutor
{
	public static ArrayList<String> loggingin = new ArrayList();
	private File failsFile;

	public LoginCommand()
	{
		this.failsFile = new File(mSecruity.msecruity.getDataFolder(), "fails.yml");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player) sender;
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "pin.yml"));
		FileConfiguration fails = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "fails.yml"));
		if ((cmd.getName().equalsIgnoreCase("pin")) && (loggingin.contains(p.getName())))
		{
			if (args.length != 1)
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe correct usage is /pin <password>"));
			}
			else if (args.length == 1)
			{
				if ((isInt(args[0])) && (data.getInt(String.valueOf(p.getUniqueId())) != Integer.valueOf(args[0]).intValue()))
				{
					if ((fails.getInt(String.valueOf(p.getUniqueId())) != 0) && (fails.contains(String.valueOf(p.getUniqueId()))))
					{
						if ((isInt(args[0])) && (data.getInt(String.valueOf(p.getUniqueId())) != Integer.valueOf(args[0]).intValue()) && (fails.getInt(String.valueOf(p.getUniqueId())) == 1))
						{
							try
							{
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong pin! Please try again."));
								fails.set(String.valueOf(p.getUniqueId()), Integer.valueOf(2));
								fails.save(this.failsFile);
							}
							catch (NumberFormatException | IOException ex6)
							{
								Exception ex = null;
								Exception nfe = ex;
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a number."));
							}
							return false;
						}
						if ((isInt(args[0])) && (data.getInt(String.valueOf(p.getUniqueId())) != Integer.valueOf(args[0]).intValue()) && (fails.getInt(String.valueOf(p.getUniqueId())) == 2))
						{
							try
							{
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong pin! Please try again."));
								fails.set(String.valueOf(p.getUniqueId()), Integer.valueOf(3));
								fails.save(this.failsFile);
							}
							catch (NumberFormatException | IOException ex7)
							{
								Exception ex2 = null;
								Exception nfe = ex2;
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a number."));
							}
							return false;
						}
						if ((isInt(args[0])) && (data.getInt(String.valueOf(p.getUniqueId())) != Integer.valueOf(args[0]).intValue()) && (fails.getInt(String.valueOf(p.getUniqueId())) == 3))
						{
							try
							{
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blacklist " + p.getName());
								p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "Too many failed login attempts, please speak to an admin to get unbanned."));
								fails.set(String.valueOf(p.getUniqueId()), Integer.valueOf(0));
								fails.save(this.failsFile);
							}
							catch (NumberFormatException | IOException ex8)
							{
								Exception ex3 = null;
								Exception nfe = ex3;
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a number."));
							}
							return false;
						}
						return false;
					}
					try
					{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong pin! Please try again."));
						fails.set(String.valueOf(p.getUniqueId()), Integer.valueOf(1));
						fails.save(this.failsFile);
					}
					catch (NumberFormatException | IOException ex9)
					{
						Exception ex4 = null;
						Exception nfe = ex4;
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a number."));
					}
				}
				else if ((isInt(args[0])) && (data.getInt(String.valueOf(p.getUniqueId())) == Integer.valueOf(args[0]).intValue()))
				{
					try
					{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You just &a&lLOGGED IN&e."));
						loggingin.remove(p.getName());
						loggingin.remove(p.getName());
						loggingin.remove(p.getName());
						loggingin.remove(p.getName());
						loggingin.remove(p.getName());
						loggingin.remove(p.getName());
						fails.set(String.valueOf(p.getUniqueId()), Integer.valueOf(0));
						fails.save(this.failsFile);
					}
					catch (NumberFormatException | IOException ex10)
					{
						Exception ex5 = null;
						Exception nfe = ex5;
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a number."));
					}
				}
			}
		}
		return false;
	}

	public boolean hasPin(Player p)
	{
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(mSecruity.msecruity.getDataFolder(), "pin.yml"));
		return data.contains(String.valueOf(p.getUniqueId()));
	}

	@EventHandler
	public void onJoinStaff(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if (p.hasPermission("command.login"))
		{
			if (hasPin(p))
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must login using /pin <password>"));
				loggingin.add(p.getName());
			}
			else
			{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease set a pin using &a/setpin"));
			}
		}
	}

	public static boolean isInt(String s)
	{
		try
		{
			Integer.parseInt(s);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
}
