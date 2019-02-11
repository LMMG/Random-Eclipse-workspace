package net.nersa.kitmap.faction.argument;

import com.doctordark.util.command.CommandArgument;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.struct.Role;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionDisbandArgument extends CommandArgument {

	private final HCF plugin;

	public FactionDisbandArgument(HCF plugin) {
		super("disband", "Disband your faction.");
		this.plugin = plugin;
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}

		Player player = (Player) sender;
		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}


		if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
			sender.sendMessage(ChatColor.RED + "You must be a leader to disband the faction.");
			return true;
		}

		plugin.getFactionManager().removeFaction(playerFaction, sender);
		return true;
	}
}
