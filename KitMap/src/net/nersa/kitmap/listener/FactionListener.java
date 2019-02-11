package net.nersa.kitmap.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.faction.KothFaction;
import net.nersa.kitmap.faction.event.CaptureZoneEnterEvent;
import net.nersa.kitmap.faction.event.CaptureZoneLeaveEvent;
import net.nersa.kitmap.faction.event.FactionCreateEvent;
import net.nersa.kitmap.faction.event.FactionRemoveEvent;
import net.nersa.kitmap.faction.event.FactionRenameEvent;
import net.nersa.kitmap.faction.event.PlayerClaimEnterEvent;
import net.nersa.kitmap.faction.event.PlayerJoinFactionEvent;
import net.nersa.kitmap.faction.event.PlayerLeaveFactionEvent;
import net.nersa.kitmap.faction.event.PlayerLeftFactionEvent;
import net.nersa.kitmap.faction.struct.Relation;
import net.nersa.kitmap.faction.type.Faction;
import net.nersa.kitmap.faction.type.PlayerFaction;
import net.nersa.utils.MessageUtils;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.google.common.base.Optional;

public class FactionListener implements Listener {

	private static final long FACTION_JOIN_WAIT_MILLIS = TimeUnit.SECONDS.toMillis(30L);
	private static final String FACTION_JOIN_WAIT_WORDS = DurationFormatUtils.formatDurationWords(FACTION_JOIN_WAIT_MILLIS, true, true);

	private static final String LAND_CHANGED_META_KEY = "landChangedMessage";
	private static final long LAND_CHANGE_MSG_THRESHOLD = 225L;

	private final HCF plugin;

	public FactionListener(HCF plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionRenameMonitor(FactionRenameEvent event) {
		Faction faction = event.getFaction();
		if (faction instanceof KothFaction) {
			((KothFaction) faction).getCaptureZone().setName(event.getNewName());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionCreate(FactionCreateEvent event) {
		Faction faction = event.getFaction();

		if (faction instanceof PlayerFaction) {
			CommandSender sender = event.getSender();

			for (Player player : Bukkit.getOnlinePlayers()) {
				String msg = ChatColor.YELLOW + "The faction " + ChatColor.WHITE + (player == null ? faction.getName() : faction.getDisplayName(player)) + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "created" + ChatColor.YELLOW + " by " + ChatColor.WHITE + (sender instanceof Player ? MessageUtils.getFormattedName((Player)sender) : sender.getName()) + ChatColor.YELLOW + '.';
				player.sendMessage(msg);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionRemove(FactionRemoveEvent event) {
		Faction faction = event.getFaction();
		if (faction instanceof PlayerFaction) {
			CommandSender sender = event.getSender();
			for (Player player : Bukkit.getOnlinePlayers()) {
				String msg = ChatColor.YELLOW + "The faction " + ChatColor.WHITE + (player == null ? faction.getName() : faction.getDisplayName(player)) + ChatColor.YELLOW + " has been " + ChatColor.RED + "disbanded" + ChatColor.YELLOW + " by " + ChatColor.WHITE + (sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName()) + ChatColor.YELLOW + '.';
				player.sendMessage(msg);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionRename(FactionRenameEvent event) {
		Faction faction = event.getFaction();
		if (faction instanceof PlayerFaction) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				Relation relation = faction.getRelation(player);
				String msg = ChatColor.YELLOW + "The faction " + relation.toChatColour() + event.getOriginalName() + ChatColor.YELLOW + " has been " + ChatColor.AQUA + "renamed" + ChatColor.YELLOW + " to " + relation.toChatColour() + event.getNewName() + ChatColor.YELLOW + '.';
				player.sendMessage(msg);
			}
		}
	}

	private long getLastLandChangedMeta(Player player) {
		List<MetadataValue> value = player.getMetadata(LAND_CHANGED_META_KEY);
		long millis = System.currentTimeMillis();
		long remaining = value == null || value.isEmpty() ? 0L : value.get(0).asLong() - millis;
		if (remaining <= 0L) { // update the metadata.
			player.setMetadata(LAND_CHANGED_META_KEY, new FixedMetadataValue(plugin, millis + LAND_CHANGE_MSG_THRESHOLD));
		}

		return remaining;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCaptureZoneEnter(CaptureZoneEnterEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.YELLOW + "Now entering cap zone: " + event.getCaptureZone().getDisplayName() + ChatColor.YELLOW + '(' + event.getFaction().getName() + ChatColor.YELLOW + ')');

		if (getLastLandChangedMeta(player) > 0L) return;

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCaptureZoneLeave(CaptureZoneLeaveEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.YELLOW + "Now leaving cap zone: " + event.getCaptureZone().getDisplayName() + ChatColor.YELLOW + '(' + event.getFaction().getName() + ChatColor.YELLOW + ')');

		if (getLastLandChangedMeta(player) > 0L) return; // delay before re-messaging.
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onPlayerClaimEnter(PlayerClaimEnterEvent event) {
		Faction toFaction = event.getToFaction();
		if (toFaction.isSafezone()) {
			Player player = event.getPlayer();
			player.setHealth(((Damageable) player).getMaxHealth());
			player.setFoodLevel(20);
			player.setFireTicks(0);
			player.setSaturation(4.0F);
		}

		Player player = event.getPlayer();
		if (getLastLandChangedMeta(player) > 0L) return; // delay before re-messaging.

		Faction fromFaction = event.getFromFaction();

		player.sendMessage(ChatColor.YELLOW + "Now leaving: " + fromFaction.getDisplayName(player) + " " + ChatColor.YELLOW + '(' + (fromFaction.isDeathban() ? ChatColor.RED + "Deathban" : ChatColor.GREEN + "Non-Deathban") + ChatColor.YELLOW + ')');

		player.sendMessage(ChatColor.YELLOW + "Now entering: " + toFaction.getDisplayName(player) + " " + ChatColor.YELLOW + '(' + (toFaction.isDeathban() ? ChatColor.RED + "Deathban" : ChatColor.GREEN + "Non-Deathban") + ChatColor.YELLOW + ')');
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerLeftFaction(PlayerLeftFactionEvent event) {
		Optional<Player> optionalPlayer = event.getPlayer();

		if (optionalPlayer.isPresent()) {
			plugin.getPlayerManager().getPlayerData(optionalPlayer.get()).setLastFactionLeaveMillis(System.currentTimeMillis());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerPreFactionJoin(PlayerJoinFactionEvent event) {
		Faction faction = event.getFaction();
		Optional<Player> optionalPlayer = event.getPlayer();
		if (faction instanceof PlayerFaction && optionalPlayer.isPresent()) {
			Player player = optionalPlayer.get();
			PlayerFaction playerFaction = (PlayerFaction) faction;

			long difference = (plugin.getPlayerManager().getPlayerData(player).getLastFactionLeaveMillis() - System.currentTimeMillis()) + FACTION_JOIN_WAIT_MILLIS;

			if (difference > 0L && !player.hasPermission("hcf.faction.argument.staff.forcejoin")) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You cannot join factions after just leaving within " + FACTION_JOIN_WAIT_WORDS + ". " + "You gotta wait another " + DurationFormatUtils.formatDurationWords(difference, true, true) + '.');
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onFactionLeave(PlayerLeaveFactionEvent event) {
		if (event.isForce() || event.isKick()) {
			return;
		}

		Faction faction = event.getFaction();
		if (faction instanceof PlayerFaction) {
			Optional<Player> optional = event.getPlayer();
			if (optional.isPresent()) {
				Player player = optional.get();
				if (plugin.getFactionManager().getFactionAt(player.getLocation()) == faction) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot leave your faction whilst you remain in its' territory.");
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction != null) {
			playerFaction.printDetails(player);
			playerFaction.broadcast(ChatColor.DARK_GREEN + "Member Online: " + ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.GOLD + '.', player.getUniqueId());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction != null) {
			playerFaction.broadcast(ChatColor.RED + "Member Offline: " + ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.GOLD + '.');
		}
	}
}
