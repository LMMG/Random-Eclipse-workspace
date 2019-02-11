package net.nersa.kitmap.combatlog;

import com.google.common.base.Function;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoggerEntity extends EntitySkeleton {
	private static final Function DAMAGE_FUNCTION;

	static {
		DAMAGE_FUNCTION = (f1 -> 0.0);
	}

	private final UUID playerUUID;

	public LoggerEntity(final World world, final Location location, final Player player) {
		super((net.minecraft.server.v1_7_R4.World) ((CraftWorld) world).getHandle());
		this.lastDamager = ((CraftPlayer) player).getHandle().lastDamager;
		final double x = location.getX();
		final double y = location.getY();
		final double z = location.getZ();
		final String playerName = player.getName();
		final boolean hasSpawned = ((CraftWorld) world).getHandle().addEntity((Entity) this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Combat Logger for [" + playerName + "] " + (hasSpawned ? (ChatColor.GREEN + "successfully spawned") : (ChatColor.RED + "failed to spawn")) + ChatColor.GOLD + " at (" + String.format("%.1f", x) + ", " + String.format("%.1f", y) + ", " + String.format("%.1f", z) + ')');
		this.playerUUID = player.getUniqueId();
		if (hasSpawned) {
			this.setCustomName(playerName);
			this.setCustomNameVisible(true);
			this.setPositionRotation(x, y, z, location.getYaw(), location.getPitch());
		}
	}

	private static PlayerNmsResult getResult(final World world, final UUID playerUUID) {
		final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
		if (offlinePlayer.hasPlayedBefore()) {
			final WorldServer worldServer = ((CraftWorld) world).getHandle();
			final EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), worldServer, new GameProfile(playerUUID, offlinePlayer.getName()), new PlayerInteractManager((net.minecraft.server.v1_7_R4.World) worldServer));
			final CraftPlayer player = entityPlayer.getBukkitEntity();
			if (player != null) {
				player.loadData();
				return new PlayerNmsResult((Player) player, entityPlayer);
			}
		}
		return null;
	}

	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	public void move(final double d0, final double d1, final double d2) {
	}

	public void b(final int i) {
	}

	public void dropDeathLoot(final boolean flag, final int i) {
	}

	public Entity findTarget() {
		return null;
	}

	public boolean damageEntity(final DamageSource damageSource, final float amount) {
		final PlayerNmsResult nmsResult = getResult((World) this.world.getWorld(), this.playerUUID);
		if (nmsResult == null) {
			return true;
		}
		final EntityPlayer entityPlayer = nmsResult.entityPlayer;
		if (entityPlayer != null) {
			entityPlayer.setPosition(this.locX, this.locY, this.locZ);
			final EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent((Entity) entityPlayer, damageSource, (double) amount, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, LoggerEntity.DAMAGE_FUNCTION, LoggerEntity.DAMAGE_FUNCTION, LoggerEntity.DAMAGE_FUNCTION, LoggerEntity.DAMAGE_FUNCTION, LoggerEntity.DAMAGE_FUNCTION, LoggerEntity.DAMAGE_FUNCTION);
			if (event.isCancelled()) {
				return false;
			}
		}
		return super.damageEntity(damageSource, amount);
	}

	public boolean a(final EntityHuman entityHuman) {
		return false;
	}

	public void h() {
		super.h();
	}

	public void collide(final Entity entity) {
	}

	public void die(final DamageSource damageSource) {
		final PlayerNmsResult playerNmsResult = getResult((World) this.world.getWorld(), this.playerUUID);
		if (playerNmsResult != null) {
			final Player player = playerNmsResult.player;
			final PlayerInventory inventory = player.getInventory();
			final boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");
			final ArrayList drops = new ArrayList();
			if (!keepInventory) {
				for (final ItemStack loggerDeathEvent : inventory.getContents()) {
					if (loggerDeathEvent != null && loggerDeathEvent.getType() != Material.AIR) {
						drops.add(loggerDeathEvent);
					}
				}
				for (final ItemStack loggerDeathEvent : inventory.getArmorContents()) {
					if (loggerDeathEvent != null && loggerDeathEvent.getType() != Material.AIR) {
						drops.add(loggerDeathEvent);
					}
				}
			}
			String var13 = ChatColor.GRAY + "(Combat-Logger) " + this.combatTracker.b().c();
			final EntityPlayer var14 = playerNmsResult.entityPlayer;
			var14.combatTracker = this.combatTracker;
			if (Bukkit.getPlayer(var14.getName()) != null) {
				Bukkit.getPlayer(var14.getUniqueID()).getInventory().clear();
				Bukkit.getPlayer(var14.getUniqueID()).kickPlayer("error");
			}
			final PlayerDeathEvent var15 = CraftEventFactory.callPlayerDeathEvent(var14, (List) drops, var13, keepInventory);
			var13 = var15.getDeathMessage();
			if (var13 != null && !var13.isEmpty()) {
				Bukkit.broadcastMessage(var13);
			}
			super.die(damageSource);
			final LoggerDeathEvent var16 = new LoggerDeathEvent(this);
			Bukkit.getPluginManager().callEvent((Event) var16);
			if (!var15.getKeepInventory()) {
				inventory.clear();
				inventory.setArmorContents(new ItemStack[inventory.getArmorContents().length]);
			}
			var14.setLocation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
			var14.setHealth(0.0f);
			player.saveData();
		}
	}

	public CraftLivingEntity getBukkitEntity() {
		return (CraftLivingEntity) super.getBukkitEntity();
	}

	public static final class PlayerNmsResult {
		public final Player player;
		public final EntityPlayer entityPlayer;

		public PlayerNmsResult(final Player player, final EntityPlayer entityPlayer) {
			this.player = player;
			this.entityPlayer = entityPlayer;
		}
	}
}