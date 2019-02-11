package com.mcmarket.listener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcmarket.SQLStats;
import com.mcmarket.profile.SQLStatsProfile;

import net.md_5.bungee.api.ChatColor;

public class StatsListener implements Listener, CommandExecutor {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		SQLStatsProfile.createPlayer(player.getUniqueId().toString());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = (Player) e.getEntity();

		if (p.getKiller() instanceof Player) {
			SQLStatsProfile.addDeaths(p.getUniqueId().toString(), 1);
			SQLStatsProfile.addKills(p.getUniqueId().toString(), 1);
		} else {
			SQLStatsProfile.addDeaths(p.getUniqueId().toString(), 1);
		}
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String arg2, String[] args) {
		if (command.getName().equalsIgnoreCase("stats")) {
			if (!(commandSender instanceof Player)) {
				return true;
			}

			Player player = (Player) commandSender;
			player.sendMessage(ChatColor.GREEN + "Kills:" + SQLStatsProfile.getKills(player.getUniqueId().toString()));
			player.sendMessage(
					ChatColor.GREEN + "Deaths:" + SQLStatsProfile.getDeaths(player.getUniqueId().toString()));
		}
		return false;
	}
}
