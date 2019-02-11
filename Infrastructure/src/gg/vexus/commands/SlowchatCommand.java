package gg.vexus.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import gg.vexus.Core;
import gg.vexus.utils.StringRegister;

public class SlowchatCommand implements CommandExecutor, Listener {
	private Core mainPlugin;
	private long slowChatTime;
	private BukkitTask bukkitTask;
	private Map<String, Long> playerChatTimes;

	public SlowchatCommand(Core mainPlugin) {
		this.mainPlugin = mainPlugin;
		this.slowChatTime = 0L;
		this.playerChatTimes = new HashMap();

		this.mainPlugin.getServer().getPluginManager().registerEvents(this, this.mainPlugin);
	}

	private long convertToMillis(long time, TimeUnit unit) {
		return TimeUnit.MILLISECONDS.convert(time, unit);
	}

	public static String getTimeMessage(long time) {
		return DurationFormatUtils.formatDurationWords(time, true, true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().hasPermission("common.slowchat.bypass")) {
			return;
		}
		if (this.slowChatTime > 0L) {
			if (!this.playerChatTimes.containsKey(e.getPlayer().getName())) {
				this.playerChatTimes.put(e.getPlayer().getName(),
						Long.valueOf(System.currentTimeMillis() + this.slowChatTime));
			} else {
				long timeRemaining = ((Long) this.playerChatTimes.get(e.getPlayer().getName())).longValue()
						- System.currentTimeMillis();
				if (timeRemaining / 1000L > 0L) {
					e.setCancelled(true);

					String timeMessage = getTimeMessage(timeRemaining);
					e.getPlayer()
							.sendMessage(ChatColor.RED + "Chat has been slowed. You can speak in " + timeMessage + ".");
				} else {
					this.playerChatTimes.put(e.getPlayer().getName(),
							Long.valueOf(System.currentTimeMillis() + this.slowChatTime));
				}
			}
		}
	}

	public boolean onCommand(CommandSender s, Command c, String alias, String[] args) {
		if (!s.hasPermission("common.slowchat.true")) {
			s.sendMessage(StringRegister.PERMISSION_MESSAGE);
			return true;
		}
		if (args.length != 1) {
			s.sendMessage(ChatColor.RED + "Usage: /" + c.getName() + " <{seconds}|off>");
			return true;
		}
		if (args[0].equalsIgnoreCase("off")) {
			if (this.slowChatTime == 0L) {
				s.sendMessage(ChatColor.RED + "SlowChat is already off.");
			} else {
				this.slowChatTime = 0L;
				if (this.bukkitTask != null) {
					this.bukkitTask.cancel();
					this.bukkitTask = null;
				}
				this.playerChatTimes.clear();
				this.mainPlugin.getServer().broadcastMessage(ChatColor.GREEN + "Chat is no longer being slowed.");
			}
		} else {
			try {
				Integer time = Integer.valueOf(Integer.parseInt(args[0]));
				if (time.intValue() == 0) {
					s.sendMessage(ChatColor.RED + "You must supply a number greater than zero.");
					s.sendMessage(ChatColor.RED + "If you want to turn off slowchat, use /slowchat off.");
				} else {
					if (this.bukkitTask != null) {
						this.bukkitTask.cancel();
					}
					this.bukkitTask = new BukkitRunnable() {
						public void run() {
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (p.hasPermission("common.slowchat.false")) {
									String slowChatMessage = DurationFormatUtils
											.formatDurationWords(SlowchatCommand.this.slowChatTime, true, true);

									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&d&lThe chat is still slowed (delay of " + slowChatMessage + ")."));
								}
							}
						}
					}

							.runTaskTimer(this.mainPlugin, 6000L, 6000L);

					this.slowChatTime = convertToMillis(time.intValue(), TimeUnit.SECONDS);

					this.mainPlugin.getServer()
							.broadcastMessage(ChatColor.GREEN + "Chat has been slowed by " + s.getName() + ".");
				}
			} catch (Exception e) {
				s.sendMessage(ChatColor.RED + "You must provide a valid number.");
			}
		}
		return true;
	}
}