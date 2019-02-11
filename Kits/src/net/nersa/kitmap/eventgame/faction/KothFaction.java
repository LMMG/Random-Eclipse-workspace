package net.nersa.kitmap.eventgame.faction;

import com.doctordark.util.BukkitUtils;
import com.google.common.collect.ImmutableList;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.CaptureZone;
import net.nersa.kitmap.eventgame.EventType;
import net.nersa.kitmap.faction.claim.Claim;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * Represents a 'King of the Hill' faction.
 */
public class KothFaction extends CapturableFaction implements ConfigurationSerializable {

	private CaptureZone captureZone;

	public KothFaction(String name) {
		super(name);
	}

	public KothFaction(Map<String, Object> map) {
		super(map);
		this.captureZone = (CaptureZone) map.get("captureZone");
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put("captureZone", captureZone);
		return map;
	}

	@Override
	public List<CaptureZone> getCaptureZones() {
		return captureZone == null ? ImmutableList.of() : ImmutableList.of(captureZone);
	}

	@Override
	public EventType getEventType() {
		return EventType.KOTH;
	}

	@Override
	public void printDetails(CommandSender sender) {
		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(getDisplayName(sender));

		for (Claim claim : claims) {
			Location location = claim.getCenter();
			sender.sendMessage(ChatColor.YELLOW + "  Location�7: " + ChatColor.WHITE + location.getBlockX() + ", " + location.getBlockZ() + " �7�o(" + ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ")");
			//sender.sendMessage(ChatColor.YELLOW + "  Location�7: " + ChatColor.WHITE + '(' + ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ() + ')');
		}

		if (captureZone != null) {
			long remainingCaptureMillis = captureZone.getRemainingCaptureMillis();
			long defaultCaptureMillis = captureZone.getDefaultCaptureMillis();
			if (remainingCaptureMillis > 0L && remainingCaptureMillis != defaultCaptureMillis) {
				sender.sendMessage(ChatColor.YELLOW + "  Remaining Time�7:�f " + DurationFormatUtils.formatDurationWords(remainingCaptureMillis, true, true));
			}

			sender.sendMessage(ChatColor.YELLOW + "  Capture Delay�7: " + ChatColor.WHITE + captureZone.getDefaultCaptureWords());
			if (captureZone.getCappingPlayer() != null && sender.hasPermission("hcf.koth.checkcapper")) {
				Player capping = captureZone.getCappingPlayer();
				PlayerFaction playerFaction = HCF.getInstance().getFactionManager().getPlayerFaction(capping);
				String factionTag = "[" + (playerFaction == null ? "*" : playerFaction.getName()) + "]";
				sender.sendMessage(ChatColor.YELLOW + "  Current Capper�7: " + ChatColor.WHITE + capping.getName() + ChatColor.GRAY + factionTag);
			}
		}

		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
	}

	/**
	 * Gets the {@link CaptureZone} of this {@link KothFaction}.
	 *
	 * @return the {@link CaptureZone} of this {@link KothFaction}
	 */
	public CaptureZone getCaptureZone() {
		return captureZone;
	}

	/**
	 * Sets the {@link CaptureZone} for this {@link KothFaction}.
	 *
	 * @param captureZone the {@link CaptureZone} to set
	 */
	public void setCaptureZone(CaptureZone captureZone) {
		this.captureZone = captureZone;
	}
}