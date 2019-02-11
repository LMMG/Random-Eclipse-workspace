package net.nersa.kitmap.faction.argument;

import com.doctordark.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.struct.Role;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FactionAnnouncementArgument extends CommandArgument {

	private static final ImmutableList<String> CLEAR_LIST = ImmutableList.of("clear");
	private final HCF plugin;

	public FactionAnnouncementArgument(HCF plugin) {
		super("announcement", "Set your faction announcement.", new String[]{"announce", "motd"});
		this.plugin = plugin;
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName() + " <newAnnouncement>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
			return true;
		}

		Player player = (Player) sender;
		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}

		if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + "You must be a officer to edit the faction announcement.");
			return true;
		}

		String oldAnnouncement = playerFaction.getAnnouncement();
		String newAnnouncement;
		if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("remove")) {
			newAnnouncement = null;
		} else {
			newAnnouncement = StringUtils.join(args, ' ', 1, args.length);
		}

		if (oldAnnouncement == null && newAnnouncement == null) {
			sender.sendMessage(ChatColor.RED + "Your factions' announcement is already unset.");
			return true;
		}

		if (oldAnnouncement != null && newAnnouncement != null && oldAnnouncement.equals(newAnnouncement)) {
			sender.sendMessage(ChatColor.RED + "Your factions' announcement is already " + newAnnouncement + '.');
			return true;
		}

		playerFaction.setAnnouncement(newAnnouncement);

		if (newAnnouncement == null) {
			playerFaction.broadcast(ChatColor.WHITE + sender.getName() + ChatColor.YELLOW + " has cleared the factions' announcement.");
			return true;
		}

		playerFaction.broadcast(ChatColor.YELLOW + player.getName() + " has updated the factions announcement from " + ChatColor.WHITE + (oldAnnouncement != null ? oldAnnouncement : "none") + ChatColor.YELLOW + " to " + ChatColor.YELLOW + newAnnouncement + '.');

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return Collections.emptyList();
		} else if (args.length == 2) {
			return CLEAR_LIST;
		} else {
			return Collections.emptyList();
		}
	}
}
