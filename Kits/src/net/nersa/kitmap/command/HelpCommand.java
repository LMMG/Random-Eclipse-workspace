package net.nersa.kitmap.command;

import java.util.Collections;
import java.util.List;

import net.nersa.kitmap.HCF;
import net.nersa.utils.MessageUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 *
 */
public class HelpCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender p, Command command, String label, String[] args) {
		if (!(p instanceof Player)) {
			p.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		if(HCF.getInstance().getConfig().contains("help")) {
			for(String line : HCF.getInstance().getConfig().getStringList("help")) {
				p.sendMessage(MessageUtils.color(line));
			}
		}
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}
}