package org.ipvp.hcf.command;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.economy.EconomyManager;

import com.doctordark.util.BukkitUtils;

public class BalCommand implements CommandExecutor {
	
	private final HCF plugin;

    public BalCommand(final HCF plugin) {
        this.plugin = plugin;
    }	

    
	public boolean onCommand(CommandSender commandSender, Command command, String arg2, String[] args) {
		if(command.getName().equalsIgnoreCase("bal")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("No");
				return true;
			}
			if(!commandSender.hasPermission("bal.use")) {
				commandSender.sendMessage(ChatColor.RED + "No");
				return true;
			}
			OfflinePlayer target;
			target = (Player) commandSender;
            UUID uuid = target.getUniqueId();
            final int balance = this.plugin.getEconomyManager().getBalance(uuid);
            commandSender.sendMessage(ChatColor.YELLOW + (commandSender.equals(target) ? "Your balance" : ("Balance of " + target.getName())) + " is " + ChatColor.GREEN + EconomyManager.ECONOMY_SYMBOL + balance + ChatColor.YELLOW + '.');
		}
		return false;
	}
}
