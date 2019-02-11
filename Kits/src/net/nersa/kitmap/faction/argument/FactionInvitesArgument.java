package net.nersa.kitmap.faction.argument;

import com.doctordark.util.command.CommandArgument;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.type.Faction;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Faction argument used to check invites for {@link Faction}s.
 */
public class FactionInvitesArgument extends CommandArgument {

	private final HCF plugin;

	public FactionInvitesArgument(HCF plugin) {
		super("invites", "View faction invitations.");
		this.plugin = plugin;
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can have faction invites.");
			return true;
		}

		List<String> receivedInvites = new ArrayList<>();
		for (Faction faction : plugin.getFactionManager().getFactions()) {
			if (faction instanceof PlayerFaction) {
				PlayerFaction targetPlayerFaction = (PlayerFaction) faction;
				if (targetPlayerFaction.getInvitedPlayerNames().contains(sender.getName())) {
					receivedInvites.add(targetPlayerFaction.getDisplayName(sender));
				}
			}
		}

		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction((Player) sender);
		String delimiter = ChatColor.WHITE + ", " + ChatColor.GRAY;

		if (playerFaction != null) {
			Set<String> sentInvites = playerFaction.getInvitedPlayerNames();
			sender.sendMessage(ChatColor.YELLOW + "Sent by " + playerFaction.getDisplayName(sender) + ChatColor.GRAY.toString() + ChatColor.ITALIC + " (" + sentInvites.size() + ')' + ChatColor.GRAY + ": " + ChatColor.WHITE + (sentInvites.isEmpty() ? "Your faction has not invited anyone." : StringUtils.join(sentInvites, delimiter) + '.'));
		}

		sender.sendMessage(ChatColor.YELLOW + "Requested " + ChatColor.GRAY.toString() + ChatColor.ITALIC + " (" + + receivedInvites.size() + ')' + ChatColor.GRAY + ": " + ChatColor.WHITE + (receivedInvites.isEmpty() ? "No factions have invited you." : StringUtils.join(receivedInvites, ChatColor.WHITE + delimiter) + '.'));

		return true;
	}
}
