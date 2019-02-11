package gg.vexus.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ListCommand implements CommandExecutor {

	private final String TO_BE_ON_LIST_PERMISSION = "common.vanish";

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		List<String> list = new ArrayList();
		Player[] arrayOfPlayer;
		int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
		for (int i = 0; i < j; i++) {
			Player player = arrayOfPlayer[i];
			if ((player.hasPermission(this.TO_BE_ON_LIST_PERMISSION)) && (canSee(sender, player))) {
				list.add(player.getName());
			}
		}
		sender.sendMessage("§7§m------------------------------------------------------------");
		sender.sendMessage("§bThere are currently §o§l" + Bukkit.getOnlinePlayers().length + "§r§b players online.");
		sender.sendMessage("§bStaff Online§7: ");
		sender.sendMessage("§7» " + ChatColor.GOLD + list.toString().replace("[", "").replace("]", "").replace(",",
				new StringBuilder().append(ChatColor.YELLOW).append("," + ChatColor.GOLD).toString()));
		sender.sendMessage("§bNo staff online? Join our Teamspeak @ §lts.vexus.gg§e.");
		sender.sendMessage("§7§m------------------------------------------------------------");
		return true;
	}

	private boolean canSee(CommandSender sender, Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getRealName(String group) {
		return PermissionsEx.getPermissionManager().getGroup(group).getPrefix().replace("[", "").replace("]", "")
				.replace("_", "").replace(group, "");
	}
}
