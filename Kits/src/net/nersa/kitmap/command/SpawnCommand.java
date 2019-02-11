package net.nersa.kitmap.command;

import com.doctordark.util.BukkitUtils;

import net.nersa.kitmap.HCF;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpawnCommand implements CommandExecutor, TabCompleter {
	private static final long KIT_MAP_TELEPORT_DELAY;

	static {
		KIT_MAP_TELEPORT_DELAY = TimeUnit.SECONDS.toMillis(30L);
	}

	final HCF plugin;

	public SpawnCommand(final HCF plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		final Player player = (Player) sender;

		if (plugin.getTimerManager().getCombatTimer().getRemaining(player) != 0) {
			player.sendMessage(ChatColor.RED + "You may not use this command whilst combat tagged!");
			return true;
		}
		World world = Bukkit.getWorld("world");
		Location spawn = world.getSpawnLocation().clone().add(0.5, 0.5, 0.5);
		plugin.getTimerManager().teleportTimer.teleport(player, spawn, KIT_MAP_TELEPORT_DELAY, ChatColor.GREEN + "Teleporting to spawn in " + ChatColor.BOLD + 30 + " seconds" + ChatColor.GREEN + ".", PlayerTeleportEvent.TeleportCause.COMMAND);
		return true;
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (args.length != 1 || !sender.hasPermission(command.getPermission() + ".teleport")) {
			return Collections.emptyList();
		}
		return BukkitUtils.getCompletions(args, Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
	}
}
