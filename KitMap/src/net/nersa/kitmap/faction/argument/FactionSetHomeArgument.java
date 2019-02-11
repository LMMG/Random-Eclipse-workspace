package net.nersa.kitmap.faction.argument;

import com.doctordark.util.command.CommandArgument;

import net.nersa.kitmap.ConfigurationService;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.FactionMember;
import net.nersa.kitmap.faction.claim.Claim;
import net.nersa.kitmap.faction.struct.Role;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionSetHomeArgument extends CommandArgument {

	private final HCF plugin;

	public FactionSetHomeArgument(HCF plugin) {
		super("sethome", "Sets the faction home location.");
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

		FactionMember factionMember = playerFaction.getMember(player);

		if (factionMember.getRole() == Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + "You must be a faction officer to set the home.");
			return true;
		}

		Location location = player.getLocation();

		boolean insideTerritory = false;
		for (Claim claim : playerFaction.getClaims()) {
			if (claim.contains(location)) {
				insideTerritory = true;
				break;
			}
		}

		if (!insideTerritory) {
			player.sendMessage(ChatColor.RED + "You may only set your home in your own territory.");
			return true;
		}

		playerFaction.setHome(location);
		playerFaction.broadcast(ConfigurationService.TEAMMATE_COLOUR + factionMember.getRole().getAstrix() + sender.getName() + ChatColor.RED + " has updated the faction home point.");
		return true;
	}

}
