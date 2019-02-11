package us.kirai.bunkers.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;

public class FreezeCommand implements CommandExecutor, Listener {

	HashSet<UUID> frozen;
	
	public void onBlockBreak(BlockBreakEvent event) {
		if (frozen.contains(event.getPlayer())) {
			event.setCancelled(true);
		}
		else {
			event.setCancelled(false);
		}
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		if (frozen.contains(event.getPlayer())) {
			event.setCancelled(true);
		}
		else {
			event.setCancelled(false);
		}

	}
	
	public void onEntityMove(PlayerMoveEvent event) {
		if (frozen.contains(event.getPlayer())) {
			event.setCancelled(true);
		}
		else {
			event.setCancelled(false);
		}
	}
	
	public void onEntityPunch(EntityDamageByEntityEvent event) {
		if (frozen.contains(event.getEntity())) {
			event.setCancelled(true);
		}
		else {
			event.setCancelled(false);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!sender.hasPermission("freeze.command")) {
			sender.sendMessage(ChatColor.RED + "Nope.");
		}
		else {
		
		if (args.length < 1 && sender.hasPermission("freeze.command")) {
			sender.sendMessage(ChatColor.RED + "Correct Usage: /freeze <player>");
			}
		}
		
		
		Player player = (Player) sender;
		frozen.add(player.getUniqueId());
		
		
		
		return true;
	}

}
