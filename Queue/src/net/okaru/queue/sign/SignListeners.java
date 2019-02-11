package net.okaru.queue.sign;

import java.util.ArrayList;
import java.util.Iterator;

import net.okaru.queue.oQueue;
import net.okaru.queue.queue.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SignListeners implements Listener
{
	private oQueue main;

	public SignListeners(final oQueue main)
	{
		this.main = main;
		new BukkitRunnable()
		{

			@Override
			@SuppressWarnings({
					"unchecked",
					"rawtypes",
					"deprecation",
					"unused"
			})
			public void run()
			{
				for (Location location : main.getSigns().keySet())
				{
					SignType signType = main.getSigns().get(location);
					for (Player player : Bukkit.getOnlinePlayers())
					{
						Sign sign;
						Queue queue;
						ArrayList<String> toDisplay;
						if (!player.getLocation().getChunk().equals(location.getChunk()) || !(location.getBlock().getState() instanceof Sign) || (queue = Queue.getByName((sign = (Sign) location.getBlock().getState()).getLine(1))) == null)
							continue;
						if (signType == SignType.INFO)
						{
							toDisplay = new ArrayList<String>();
							Iterator<String> iterator = main.getConfigFile().getStringList("QUEUE_SIGN.QUEUE_INFO").iterator();
							block2: while (iterator.hasNext())
							{
								String text = iterator.next();
								for (String rank : queue.getInfo().keySet())
								{
									if (!text.toLowerCase().contains(rank.toLowerCase()))
										continue;
									int amount = 0;
									if (queue.getInfo().containsKey(rank))
									{
										amount = queue.getInfo().get(rank);
									}
									if (amount >= 1000)
									{
										amount = 999;
									}
									text = text.replace("%AMOUNT%", "" + amount + "");
									toDisplay.add(text);
									continue block2;
								}
								toDisplay.add(text.replace("%AMOUNT%", "0"));
							}
							player.sendSignChange(location, toDisplay.toArray(new String[toDisplay.size()]));
						}
						if (signType != SignType.JOIN)
							continue;
						toDisplay = new ArrayList();
						if (main.isOffline())
						{
							for (String text : main.getConfigFile().getStringList("QUEUE_SIGN.QUEUE_SYSTEM_OFFLINE"))
							{
								toDisplay.add(text.replace("%NAME%", queue.getName()).replace("%SERVER%", queue.getServer()).replace("%ONLINE%", "" + queue.getData()[1] + "").replace("%MAX%", "" + queue.getData()[2] + ""));
							}
						}
						else if (queue.getData()[4] == 1)
						{
							for (String text : main.getConfigFile().getStringList("QUEUE_SIGN.OFFLINE"))
							{
								toDisplay.add(text.replace("%NAME%", queue.getName()).replace("%SERVER%", queue.getServer()).replace("%ONLINE%", "" + queue.getData()[1] + "").replace("%MAX%", "" + queue.getData()[2] + ""));
							}
						}
						else if (queue.getData()[3] == 1)
						{
							for (String text : main.getConfigFile().getStringList("QUEUE_SIGN.WHITELISTED"))
							{
								toDisplay.add(text.replace("%NAME%", queue.getName()).replace("%SERVER%", queue.getServer()).replace("%ONLINE%", "" + queue.getData()[1] + "").replace("%MAX%", "" + queue.getData()[2] + ""));
							}
						}
						else if (!queue.getPlayerSet().contains(player))
						{
							for (String text : main.getConfigFile().getStringList("QUEUE_SIGN.NOT_QUEUED"))
							{
								toDisplay.add(text.replace("%NAME%", queue.getName()).replace("%SERVER%", queue.getServer()).replace("%ONLINE%", "" + queue.getData()[1] + "").replace("%MAX%", "" + queue.getData()[2] + ""));
							}
						}
						else
						{
							for (String text : main.getConfigFile().getStringList("QUEUE_SIGN.QUEUED"))
							{
								int position = queue.getPlayers().get(player.getUniqueId());
								int total = queue.getData()[0];
								if (position >= 1000)
								{
									position = 999;
								}
								if (total >= 1000)
								{
									position = 999;
								}
								toDisplay.add(text.replace("%NAME%", queue.getName()).replace("%SERVER%", queue.getServer()).replace("%ONLINE%", "" + queue.getData()[1] + "").replace("%MAX%", "" + queue.getData()[2] + "").replace("%POSITION%", "" + position + "").replace("%TOTAL%", "" + total + ""));
							}
						}
						player.sendSignChange(location, toDisplay.toArray(new String[toDisplay.size()]));
					}
				}
			}
		}.runTaskTimerAsynchronously(main, 2, 2);
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		if (this.main.getSigns().containsKey(event.getBlock().getLocation()) && event.getPlayer().hasPermission("alduin.admin"))
		{
			this.main.getSigns().remove(event.getBlock().getLocation());
			event.getPlayer().sendMessage(ChatColor.RED + "You have removed a queue sign.");
		}
	}

	@EventHandler
	public void onPlayerInteractSignEvent(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && this.main.getSigns().containsKey(block.getLocation()))
		{
			event.setCancelled(true);
			Sign sign = (Sign) block.getState();
			final Queue queue = Queue.getByName(sign.getLine(1));
			if (!sign.getLine(2).equalsIgnoreCase("join")) { return; }
			if (this.main.isOffline())
			{
				player.sendMessage(this.main.getConfigFile().getString("MESSAGES.QUEUE_OFFLINE"));
				return;
			}
			if (queue != null)
			{
				Queue currentQueue = Queue.getByPlayer(player);
				if (currentQueue != null)
				{
					player.sendMessage(this.main.getConfigFile().getString("MESSAGES.ALREADY_IN_QUEUE").replace("%QUEUE%", currentQueue.getName()));
					return;
				}
				if (queue.getData()[4] == 1)
				{
					player.sendMessage(this.main.getConfigFile().getString("MESSAGES.SERVER_OFFLINE").replace("%QUEUE%", queue.getName()));
					return;
				}
				if (queue.getData()[3] == 1)
				{
					player.sendMessage(this.main.getConfigFile().getString("MESSAGES.SERVER_WHITELISTED").replace("%QUEUE%", queue.getName()));
					return;
				}
				player.sendMessage(this.main.getConfigFile().getString("MESSAGES.ADDED").replace("%QUEUE%", queue.getName()));
				new BukkitRunnable()
				{

					@Override
					public void run()
					{
						queue.addPlayer(player);
					}
				}.runTaskAsynchronously(this.main);
			}
		}
	}

	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event)
	{
		Player player = event.getPlayer();
		String[] args = event.getLines();
		if (args[0].equalsIgnoreCase("[Queue]") && player.hasPermission("alduin.admin"))
		{
			if (args.length >= 3)
			{
				SignType signType;
				String server = args[1];
				Queue queue = Queue.getByName(server);
				if (queue == null)
				{
					player.sendMessage(ChatColor.RED + "Invalid queue.");
					event.setCancelled(true);
					return;
				}
				try
				{
					signType = SignType.valueOf(args[2].toUpperCase());
				}
				catch (IllegalArgumentException ignored)
				{
					player.sendMessage(ChatColor.RED + "Invalid action.");
					event.setCancelled(true);
					return;
				}
				this.main.getSigns().put(event.getBlock().getLocation(), signType);
				player.sendMessage(ChatColor.YELLOW + "You have successfully created a queue sign.");
			}
			else
			{
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Invalid sign format.");
			}
		}
	}

}
