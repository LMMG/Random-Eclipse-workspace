package net.nersa.kitmap.pvpclass.archer;


import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.pvpclass.PvpClass;
import net.nersa.kitmap.pvpclass.event.PvpClassUnequipEvent;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Represents a {@link PvpClass} used to buff Archer game-play such as Bows.
 */
public class ArcherClass extends PvpClass implements Listener {

	public static final HashMap<UUID, UUID> TAGGED = new HashMap<>();// for tagged key
	private static final PotionEffect ARCHER_CRITICAL_EFFECT = new PotionEffect(PotionEffectType.WITHER, 60, 0);
	private static final int MARK_TIMEOUT_SECONDS = 15;
	private static final int MARK_EXECUTION_LEVEL = 3;
	private static final float MINIMUM_FORCE = 0.5F;
	private static final PotionEffect ARCHER_SPEED_EFFECT = new PotionEffect(PotionEffectType.SPEED, 160, 3);
	private static final long ARCHER_SPEED_COOLDOWN_DELAY = TimeUnit.MINUTES.toMillis(1L);
	private final TObjectLongMap<UUID> archerSpeedCooldowns = new TObjectLongHashMap<>();

	private final Table<UUID, UUID, ArcherMark> marks = HashBasedTable.create(); // Key of the Players UUID who applied the mark, value as the Mark applied.
	private final HCF plugin;

	public ArcherClass(HCF plugin) {
		super("Archer", TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;

		this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
		this.passiveEffects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
		this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
	}

	public Map<UUID, ArcherMark> getSentMarks(Player player) {
		synchronized (marks) {
			return marks.column(player.getUniqueId());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerClassUnequip(PvpClassUnequipEvent event) {
		getSentMarks(event.getPlayer()).clear();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event) {
		getSentMarks(event.getEntity()).clear();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		getSentMarks(event.getPlayer()).clear();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent event) {
		getSentMarks(event.getPlayer()).clear();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamage(final EntityDamageByEntityEvent event) {
		final Entity entity = event.getEntity();
		final Entity damager = event.getDamager();
		if (entity instanceof Player && damager instanceof Arrow) {
			final Arrow arrow = (Arrow) damager;
			final ProjectileSource source = arrow.getShooter();
			if (source instanceof Player) {
				Player damaged = (Player) event.getEntity();
				final Player shooter = (Player) source;
				final PvpClass equipped = this.plugin.getPvpClassManager().getEquippedClass(shooter);
				if (equipped == null || !equipped.equals(this)) {
					return;
				}

				if (plugin.getTimerManager().archerTimer.getRemaining((Player) entity) == 0 || plugin.getTimerManager().archerTimer.getRemaining((Player) entity) < TimeUnit.SECONDS.toMillis(5)) {
					if (plugin.getPvpClassManager().getEquippedClass(damaged) != null && plugin.getPvpClassManager().getEquippedClass(damaged).equals(this))
						return;
					plugin.getTimerManager().archerTimer.setCooldown((Player) entity, entity.getUniqueId());
					TAGGED.put(damaged.getUniqueId(), shooter.getUniqueId());
					final double distance = shooter.getLocation().distance(damaged.getLocation());
					shooter.sendMessage(ChatColor.GOLD + "You tagged " + ChatColor.RED + damaged.getName() + ChatColor.GOLD + " from " + ChatColor.RED + String.format("%.1f", distance) + ChatColor.GOLD + " blocks away.");
					damaged.sendMessage(ChatColor.GOLD + "You were archer tagged by " + ChatColor.RED + shooter.getName() + ChatColor.GOLD + " from " + ChatColor.RED + String.format("%.1f", distance) + ChatColor.GOLD + " blocks away.");
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if (event.hasItem() && event.getItem().getType() == Material.SUGAR) {
				if (plugin.getPvpClassManager().getEquippedClass(event.getPlayer()) != this) {
					return;
				}

				Player player = event.getPlayer();
				UUID uuid = player.getUniqueId();
				long timestamp = archerSpeedCooldowns.get(uuid);
				long millis = System.currentTimeMillis();
				long remaining = timestamp == archerSpeedCooldowns.getNoEntryValue() ? -1L : timestamp - millis;
				if (remaining > 0L) {
					player.sendMessage(ChatColor.RED + "Cannot use " + getName() + " speed for another " + DurationFormatUtils.formatDurationWords(remaining, true, true) + ".");
				} else {
					ItemStack stack = player.getItemInHand();
					if (stack.getAmount() == 1) {
						player.setItemInHand(new ItemStack(Material.AIR, 1));
					} else stack.setAmount(stack.getAmount() - 1);

					plugin.getEffectRestorer().setRestoreEffect(player, ARCHER_SPEED_EFFECT);
					archerSpeedCooldowns.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + ARCHER_SPEED_COOLDOWN_DELAY);
				}
			}
		}
	}

	@Override
	public boolean isApplicableFor(Player player) {
		PlayerInventory playerInventory = player.getInventory();

		ItemStack helmet = playerInventory.getHelmet();
		if (helmet == null || helmet.getType() != Material.LEATHER_HELMET) return false;

		ItemStack chestplate = playerInventory.getChestplate();
		if (chestplate == null || chestplate.getType() != Material.LEATHER_CHESTPLATE) return false;

		ItemStack leggings = playerInventory.getLeggings();
		if (leggings == null || leggings.getType() != Material.LEATHER_LEGGINGS) return false;

		ItemStack boots = playerInventory.getBoots();
		return !(boots == null || boots.getType() != Material.LEATHER_BOOTS);
	}
}
