package net.nersa.kitmap.faction.event;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import lombok.Getter;
import net.nersa.kitmap.faction.event.cause.FactionLeaveCause;
import net.nersa.kitmap.faction.type.Faction;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

import java.util.UUID;

/**
 * Faction event called when a user has left their {@link Faction}.
 */
public class PlayerLeftFactionEvent extends FactionEvent {

	private static final HandlerList handlers = new HandlerList();
	@Getter
	private final UUID uniqueID;
	@Getter
	private final FactionLeaveCause cause;
	@Getter
	private final CommandSender sender;
	@Getter
	private final boolean isKick;
	@Getter
	private final boolean force;
	private Optional<Player> player;

	public PlayerLeftFactionEvent(CommandSender sender, @Nullable Player player, UUID playerUUID, PlayerFaction playerFaction, FactionLeaveCause cause, boolean isKick, boolean force) {
		super(playerFaction);

		Preconditions.checkNotNull(sender, "Sender cannot be null");
		Preconditions.checkNotNull(playerUUID, "Player UUID cannot be null");
		Preconditions.checkNotNull(playerFaction, "Player faction cannot be null");
		Preconditions.checkNotNull(cause, "Cause cannot be null");

		this.sender = sender;
		if (player != null) {
			this.player = Optional.of(player);
		}

		this.uniqueID = playerUUID;
		this.cause = cause;
		this.isKick = isKick;
		this.force = force;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Optional<Player> getPlayer() {
		if (this.player == null) {
			this.player = Optional.fromNullable(Bukkit.getPlayer(uniqueID));
		}

		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public PlayerFaction getFaction() {
		return (PlayerFaction) super.getFaction();
	}
}
