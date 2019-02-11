package gg.vexus.commands;

import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gg.vexus.utils.StringRegister;
import net.md_5.bungee.api.ChatColor;

public class ClearChatCommand implements CommandExecutor {

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender.hasPermission("common.clearchat")) {
			if (args.length == 0) {
				commandSender.sendMessage(ChatColor.RED + "Usage: /clearchat <reason>");
			} else {
				for(Player online : Bukkit.getServer().getOnlinePlayers()) {
					online.sendMessage(new String[101]);
					online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + commandSender.getName() + " &e has cleared chat for &7" + StringUtils.join(args)));
				}
			}
		}
		else {
			commandSender.sendMessage(StringRegister.PERMISSION_MESSAGE);
		}
		return false;
	}
}
