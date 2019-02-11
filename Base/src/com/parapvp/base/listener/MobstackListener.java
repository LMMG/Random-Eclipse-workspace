/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.google.common.primitives.Ints
 *  net.minecraft.server.v1_7_R4.Entity
 *  net.minecraft.server.v1_7_R4.WorldServer
 *  net.minecraft.util.gnu.trove.iterator.TObjectIntIterator
 *  net.minecraft.util.gnu.trove.map.TObjectIntMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.CreatureSpawner
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity
 *  org.bukkit.entity.Ageable
 *  org.bukkit.entity.Creature
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Zombie
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.SpawnerSpawnEvent
 *  org.bukkit.event.player.PlayerBreedEntityEvent
 *  org.bukkit.event.player.PlayerTemptEntityEvent
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.parapvp.base.listener;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.primitives.Ints;
import com.parapvp.base.BasePlugin;
import com.parapvp.util.cuboid.CoordinatePair;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.gnu.trove.iterator.TObjectIntIterator;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerBreedEntityEvent;
import org.bukkit.event.player.PlayerTemptEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MobstackListener extends BukkitRunnable implements Listener
{
	private static final int NATURAL_STACK_RADIUS = 81;
	private static final int MAX_STACKED_QUANTITY = 40;
	private static final String STACKED_PREFIX = ChatColor.AQUA + "x";
	private final Table<CoordinatePair, EntityType, Integer> naturalSpawnStacks = HashBasedTable.create();
	private final TObjectIntMap<Location> spawnerStacks = new TObjectIntHashMap();
	private final BasePlugin plugin;

	public MobstackListener(BasePlugin plugin)
	{
		this.plugin = plugin;
	}

	private CoordinatePair fromLocation(Location location)
	{
		return new CoordinatePair(location.getWorld(), 81 * Math.round(location.getBlockX() / 81), 81 * Math.round(location.getBlockZ() / 81));
	}

	public void run()
	{
		for (World world : Bukkit.getServer().getWorlds())
		{
			if (world.getEnvironment() == World.Environment.THE_END)
				continue;
			for (LivingEntity entity : world.getLivingEntities())
			{
				if (!entity.isValid() || entity instanceof Player)
					continue;
				for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(8.0, 8.0, 8.0))
				{
					if (!(nearby instanceof LivingEntity) || !nearby.isValid() || nearby instanceof Player)
						continue;
					this.stack(entity, (LivingEntity) nearby);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerTemptEntity(PlayerTemptEntityEvent event)
	{
		int stackedQuantity = this.getStackedQuantity((LivingEntity) event.getEntity());
		if (stackedQuantity >= 40)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "This entity is already max stacked.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerBreedEntity(PlayerBreedEntityEvent event)
	{
		if (event.getEntity() instanceof Horse) { return; }
		LivingEntity chosen = this.plugin.getRandom().nextBoolean() ? event.getFirstParent() : event.getSecondParent();
		int stackedQuantity = this.getStackedQuantity(chosen);
		if (stackedQuantity == -1)
		{
			stackedQuantity = 1;
		}
		this.setStackedQuantity(chosen, ++stackedQuantity);
		event.getPlayer().sendMessage(ChatColor.GREEN + "Stack increased due to breeding.");
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onSpawnerSpawn(SpawnerSpawnEvent event)
	{
		switch (event.getEntityType())
		{
			default:
		}
		CreatureSpawner spawner = event.getSpawner();
		World world = spawner.getWorld();
		if (world != null && world.getEnvironment() == World.Environment.THE_END) { return; }
		Location location = spawner.getLocation();
		Optional entityIdOptional = Optional.fromNullable(this.spawnerStacks.get(location));
		if (entityIdOptional.isPresent())
		{
			CraftEntity target;
			int entityId = (Integer) entityIdOptional.get();
			Entity nmsTarget = ((CraftWorld) location.getWorld()).getHandle().getEntity(entityId);
			CraftEntity craftEntity = target = nmsTarget != null ? nmsTarget.getBukkitEntity() : null;
			if (target != null && target instanceof LivingEntity)
			{
				LivingEntity targetLiving = (LivingEntity) target;
				int stackedQuantity = this.getStackedQuantity(targetLiving);
				if (stackedQuantity == -1)
				{
					stackedQuantity = 1;
				}
				if (stackedQuantity < 40)
				{
					this.setStackedQuantity(targetLiving, ++stackedQuantity);
					event.setCancelled(true);
					return;
				}
			}
		}
		this.spawnerStacks.put(location, event.getEntity().getEntityId());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		EntityType entityType = event.getEntityType();
		switch (entityType)
		{
			default:
		}
		switch (event.getSpawnReason())
		{
			case CHUNK_GEN:
			case NATURAL:
			case DEFAULT:
			{
				Location location = event.getLocation();
				CoordinatePair coordinatePair = this.fromLocation(location);
				Optional entityIdOptional = Optional.fromNullable(this.naturalSpawnStacks.get(coordinatePair, entityType));
				if (entityIdOptional.isPresent())
				{
					CraftEntity target;
					int entityId = (Integer) entityIdOptional.get();
					Entity nmsTarget = ((CraftWorld) location.getWorld()).getHandle().getEntity(entityId);
					CraftEntity craftEntity = target = nmsTarget == null ? null : nmsTarget.getBukkitEntity();
					if (target != null && target instanceof LivingEntity)
					{
						boolean canSpawn;
						LivingEntity targetLiving = (LivingEntity) target;
						if (targetLiving instanceof Ageable)
						{
							canSpawn = ((Ageable) targetLiving).isAdult();
						}
						else
						{
							boolean bl = canSpawn = !(targetLiving instanceof Zombie) || !((Zombie) targetLiving).isBaby();
						}
						if (canSpawn)
						{
							int stackedQuantity = this.getStackedQuantity(targetLiving);
							if (stackedQuantity == -1)
							{
								stackedQuantity = 1;
							}
							if (stackedQuantity < 40)
							{
								this.setStackedQuantity(targetLiving, ++stackedQuantity);
								event.setCancelled(true);
								return;
							}
						}
					}
				}
				this.naturalSpawnStacks.put(coordinatePair, entityType, event.getEntity().getEntityId());
				break;
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event)
	{
		LivingEntity livingEntity = event.getEntity();
		int stackedQuantity = this.getStackedQuantity(livingEntity);
		if (stackedQuantity > 1)
		{
			LivingEntity respawned = (LivingEntity) livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), event.getEntityType());
			this.setStackedQuantity(respawned, Math.min(40, --stackedQuantity));
			if (respawned instanceof Ageable)
			{
				((Ageable) respawned).setAdult();
			}
			if (respawned instanceof Zombie)
			{
				((Zombie) respawned).setBaby(false);
			}
			if (this.spawnerStacks.containsValue(livingEntity.getEntityId()))
			{
				TObjectIntIterator iterator = this.spawnerStacks.iterator();
				while (iterator.hasNext())
				{
					iterator.advance();
					if (iterator.value() != livingEntity.getEntityId())
						continue;
					iterator.setValue(respawned.getEntityId());
					break;
				}
			}
		}
	}

	private int getStackedQuantity(LivingEntity livingEntity)
	{
		String customName = livingEntity.getCustomName();
		if (customName != null && customName.contains(STACKED_PREFIX))
		{
			customName = customName.replace(STACKED_PREFIX, "");
			return Ints.tryParse((String) customName);
		}
		return -1;
	}

	private boolean stack(LivingEntity tostack, LivingEntity toremove)
	{
		Integer newStack = 1;
		Integer removeStack = 1;
		if (this.hasStack(tostack))
		{
			newStack = this.getStackedQuantity(tostack);
		}
		if (this.hasStack(toremove))
		{
			removeStack = this.getStackedQuantity(toremove);
		}
		else if (this.getStackedQuantity(toremove) == -1 && toremove.getCustomName() != null && toremove.getCustomName().contains(ChatColor.WHITE.toString())) { return false; }
		if (toremove.getType() != tostack.getType()) { return false; }
		if (toremove.getType() == EntityType.SLIME || toremove.getType() == EntityType.MAGMA_CUBE || tostack.getType() == EntityType.SLIME || tostack.getType() == EntityType.MAGMA_CUBE) { return false; }
		toremove.remove();
		this.setStackedQuantity(tostack, Math.min(newStack + removeStack, 40));
		return true;
	}

	private boolean hasStack(LivingEntity livingEntity)
	{
		if (this.getStackedQuantity(livingEntity) != -1) { return true; }
		return false;
	}

	private void setStackedQuantity(LivingEntity livingEntity, int quantity)
	{
		Preconditions.checkArgument((boolean) (quantity >= 0), "Stacked quantity cannot be negative");
		Preconditions.checkArgument((boolean) (quantity <= 40), "Stacked quantity cannot be more than 40");
		if (quantity <= 1)
		{
			livingEntity.setCustomName(null);
		}
		else
		{
			livingEntity.setCustomName(STACKED_PREFIX + quantity);
			livingEntity.setCustomNameVisible(false);
		}
	}

}
