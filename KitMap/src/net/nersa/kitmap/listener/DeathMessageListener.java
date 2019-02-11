package net.nersa.kitmap.listener;

import net.minecraft.server.v1_7_R4.EntityLiving;
import net.nersa.kitmap.HCF;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.google.common.base.Preconditions;

public class DeathMessageListener implements Listener {
	private HCF plugin;

	public DeathMessageListener(HCF plugin) {
		this.plugin = plugin;
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ')', replacement);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		String message = event.getDeathMessage();
		if (message == null || message.isEmpty()) {
			return;
		}
		event.setDeathMessage(getDeathMessage(message, event.getEntity(), this.getKiller(event)));
	}

	private CraftEntity getKiller(PlayerDeathEvent event) {
		EntityLiving lastAttacker = ((CraftPlayer) event.getEntity()).getHandle().aX();
		return (lastAttacker == null) ? null : lastAttacker.getBukkitEntity();
	}

	private String getDeathMessage(String input, Entity entity, Entity killer) {
		input = input.replaceFirst("\\[", "");
		input = replaceLast(input, "]", "");
		if (entity != null) {
			input = input.replaceFirst("(?i)" + this.getEntityName(entity), ChatColor.RED + this.getDisplayName(entity) + ChatColor.YELLOW);
		}
		if (killer != null && (entity == null || !killer.equals(entity))) {
			input = input.replaceFirst("(?i)" + this.getEntityName(killer), ChatColor.RED + this.getDisplayName(killer) + ChatColor.YELLOW);
		}
		return input;
	}

	private String getEntityName(Entity entity) {
		Preconditions.checkNotNull((Object) entity, "Entity cannot be null");
		return (entity instanceof Player) ? ((Player) entity).getName() : ((CraftEntity) entity).getHandle().getName();
	}

	private String getDisplayName(Entity entity) {
		Preconditions.checkNotNull((Object) entity, "Entity cannot be null");
		if (entity instanceof Player) {
			Player player = (Player) entity;
			return player.getName() + ChatColor.DARK_RED + '[' + ChatColor.DARK_RED + this.plugin.getPlayerManager().getPlayerData(player).getKills() + ChatColor.DARK_RED + ']';
		}
		return WordUtils.capitalizeFully(entity.getType().name().replace('_', ' '));
	}
}
