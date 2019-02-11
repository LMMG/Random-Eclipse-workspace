package server.wenjapvp.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.doctordark.util.BukkitUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import server.wenjapvp.hcf.HCF;
import server.wenjapvp.hcf.faction.struct.Raidable;
import server.wenjapvp.hcf.faction.struct.Role;
import server.wenjapvp.hcf.faction.type.ClaimableFaction;
import server.wenjapvp.hcf.faction.type.Faction;
import server.wenjapvp.hcf.faction.type.PlayerFaction;
import server.wenjapvp.hcf.faction.type.WarzoneFaction;

public class FactionCoreListener implements Listener
{
	public static final String PROTECTION_BYPASS_PERMISSION = "hcf.faction.protection.bypass";

	public FactionCoreListener(HCF plugin)
	{
		this.plugin = plugin;
	}

	public static boolean attemptBuild(Entity entity, Location location, String denyMessage)
	{
		return attemptBuild(entity, location, denyMessage, false);
	}

	public static boolean attemptBuild(Entity entity, Location location, String denyMessage, boolean isInteraction)
	{
		boolean result = false;
		if ((entity instanceof Player))
		{
			Player player = (Player) entity;
			if ((player != null) && (player.getGameMode() == GameMode.CREATIVE) && (player.hasPermission("hcf.faction.protection.bypass"))) { return true; }
			if ((player != null) && (player.getWorld().getEnvironment() == World.Environment.THE_END))
			{
				player.sendMessage(ChatColor.RED + "You cannot build in the end.");
				return false;
			}
			Faction factionAt = HCF.getPlugin().getFactionManager().getFactionAt(location);
			if (!(factionAt instanceof ClaimableFaction))
			{
				result = true;
			}
			else if (((factionAt instanceof Raidable)) && (((Raidable) factionAt).isRaidable()))
			{
				result = true;
			}
			if ((player != null) && ((factionAt instanceof PlayerFaction)))
			{
				PlayerFaction playerFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(player);
				if ((playerFaction != null) && (playerFaction.equals(factionAt)))
				{
					result = true;
				}
			}
			if (result)
			{
				if ((!isInteraction) && (Math.abs(location.getBlockX()) <= 950) && (Math.abs(location.getBlockZ()) <= 950))
				{
					if ((denyMessage != null) && (player != null))
					{
						player.sendMessage(ChatColor.YELLOW + "You cannot build within " + ChatColor.GRAY + 950 + ChatColor.YELLOW + " blocks from spawn.");
					}
					return false;
				}
			}
			else if ((denyMessage != null) && (player != null))
			{
				player.sendMessage(String.format(denyMessage, new Object[] {
						factionAt.getDisplayName(player)
				}));
			}
		}
		return result;
	}

	public static boolean canBuildAt(Location from, Location to)
	{
		Faction toFactionAt = HCF.getPlugin().getFactionManager().getFactionAt(to);
		if (((toFactionAt instanceof Raidable)) && (!((Raidable) toFactionAt).isRaidable()))
		{
			Faction fromFactionAt = HCF.getPlugin().getFactionManager().getFactionAt(from);
			if (!toFactionAt.equals(fromFactionAt)) { return false; }
		}
		return true;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onStickyPistonExtend(BlockPistonExtendEvent event)
	{
		Block block = event.getBlock();
		Block targetBlock = block.getRelative(event.getDirection(), event.getLength() + 1);
		if ((targetBlock.isEmpty()) || (targetBlock.isLiquid()))
		{
			Faction targetFaction = this.plugin.getFactionManager().getFactionAt(targetBlock.getLocation());
			if (((targetFaction instanceof Raidable)) && (!((Raidable) targetFaction).isRaidable()) && (!targetFaction.equals(this.plugin.getFactionManager().getFactionAt(block))))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onStickyPistonRetract(BlockPistonRetractEvent event)
	{
		if (!event.isSticky()) { return; }
		Location retractLocation = event.getRetractLocation();
		Block retractBlock = retractLocation.getBlock();
		if ((!retractBlock.isEmpty()) && (!retractBlock.isLiquid()))
		{
			Block block = event.getBlock();
			Faction targetFaction = this.plugin.getFactionManager().getFactionAt(retractLocation);
			if (((targetFaction instanceof Raidable)) && (!((Raidable) targetFaction).isRaidable()) && (!targetFaction.equals(this.plugin.getFactionManager().getFactionAt(block))))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockFromTo(BlockFromToEvent event)
	{
		Block toBlock = event.getToBlock();
		Block fromBlock = event.getBlock();
		Material fromType = fromBlock.getType();
		Material toType = toBlock.getType();
		if (((toType == Material.REDSTONE_WIRE) || (toType == Material.TRIPWIRE)) && ((fromType == Material.AIR) || (fromType == Material.STATIONARY_LAVA) || (fromType == Material.LAVA)))
		{
			toBlock.setType(Material.AIR);
		}
		if (((toBlock.getType() == Material.WATER) || (toBlock.getType() == Material.STATIONARY_WATER) || (toBlock.getType() == Material.LAVA) || (toBlock.getType() == Material.STATIONARY_LAVA)) && (!canBuildAt(fromBlock.getLocation(), toBlock.getLocation())))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerPortal(PlayerPortalEvent event)
	{
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
		{
			Location from = event.getFrom();
			Location to = event.getTo();
			Player player = event.getPlayer();
			Faction fromFac = this.plugin.getFactionManager().getFactionAt(from);
			if (fromFac.isSafezone())
			{
				event.setTo(to.getWorld().getSpawnLocation().add(0.5D, 0.0D, 0.5D));
				event.useTravelAgent(false);
				player.sendMessage(ChatColor.YELLOW + "You were teleported to the spawn of target world as you were in a safe-zone.");
				return;
			}
			if ((event.useTravelAgent()) && (to.getWorld().getEnvironment() == World.Environment.NORMAL))
			{
				TravelAgent travelAgent = event.getPortalTravelAgent();
				if (!travelAgent.getCanCreatePortal()) { return; }
				Location foundPortal = travelAgent.findPortal(to);
				if (foundPortal != null) { return; }
				Faction factionAt = this.plugin.getFactionManager().getFactionAt(to);
				if ((factionAt instanceof ClaimableFaction))
				{
					Faction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
					if ((playerFaction != null) && (playerFaction.equals(factionAt))) { return; }
					player.sendMessage(ChatColor.YELLOW + "Portal would have created portal in territory of " + factionAt.getDisplayName(player) + ChatColor.YELLOW + '.');
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
		if (reason == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) { return; }
		Location location = event.getLocation();
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(location);
		if ((factionAt.isSafezone()) && (reason == CreatureSpawnEvent.SpawnReason.SPAWNER))
		{}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if ((entity instanceof Player))
		{
			Player player = (Player) entity;
			Faction playerFactionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
			EntityDamageEvent.DamageCause cause = event.getCause();
			if ((playerFactionAt.isSafezone()) && (cause != EntityDamageEvent.DamageCause.SUICIDE))
			{
				event.setCancelled(true);
			}
			Player attacker = BukkitUtils.getFinalAttacker(event, true);
			if (attacker != null)
			{
				Faction attackerFactionAt = this.plugin.getFactionManager().getFactionAt(attacker.getLocation());
				if (attackerFactionAt.isSafezone())
				{
					event.setCancelled(true);
					return;
				}
				if (playerFactionAt.isSafezone()) { return; }
				PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
				PlayerFaction attackerFaction;
				if ((playerFaction != null) && ((attackerFaction = this.plugin.getFactionManager().getPlayerFaction(attacker)) != null))
				{
					Role role = playerFaction.getMember(player).getRole();
					String astrix = role.getAstrix();
					if (attackerFaction.equals(playerFaction))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onVehicleEnter(VehicleEnterEvent event)
	{
		Entity entered = event.getEntered();
		if ((entered instanceof Player))
		{
			Vehicle vehicle = event.getVehicle();
			if ((vehicle instanceof Horse))
			{
				Horse horse = (Horse) event.getVehicle();
				AnimalTamer owner = horse.getOwner();
				if ((owner != null) && (!owner.equals(entered)))
				{
					((Player) entered).sendMessage(ChatColor.YELLOW + "You cannot enter a Horse that belongs to " + ChatColor.RED + owner.getName() + ChatColor.YELLOW + '.');
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if (((entity instanceof Player)) && (((Player) entity).getFoodLevel() < event.getFoodLevel()) && (this.plugin.getFactionManager().getFactionAt(entity.getLocation()).isSafezone()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPotionSplash(PotionSplashEvent event)
	{
		ThrownPotion potion = event.getEntity();
		if (!BukkitUtils.isDebuff(potion)) { return; }
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(potion.getLocation());
		if (factionAt.isSafezone())
		{
			event.setCancelled(true);
			return;
		}
		ProjectileSource source = potion.getShooter();
		if ((source instanceof Player))
		{
			Player player = (Player) source;
			for (LivingEntity affected : event.getAffectedEntities())
			{
				if (((affected instanceof Player)) && (!player.equals(affected)))
				{
					Player target = (Player) affected;
					if (!target.equals(source))
					{
						if (this.plugin.getFactionManager().getFactionAt(target.getLocation()).isSafezone())
						{
							event.setIntensity(affected, 0.0D);
						}
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityTarget(EntityTargetEvent event)
	{
		switch (event.getReason())
		{
			case COLLISION:
			case RANDOM_TARGET:
				Entity target = event.getTarget();
				if (((event.getEntity() instanceof LivingEntity)) && ((target instanceof Player)))
				{
					Faction factionAt = this.plugin.getFactionManager().getFactionAt(target.getLocation());
					Faction playerFaction;
					if ((factionAt.isSafezone()) || (((playerFaction = this.plugin.getFactionManager().getPlayerFaction((Player) target)) != null) && (factionAt.equals(playerFaction))))
					{
						event.setCancelled(true);
					}
				}
				break;
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBurn(BlockBurnEvent event)
	{
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
		if (((factionAt instanceof WarzoneFaction)) || (((factionAt instanceof Raidable)) && (!((Raidable) factionAt).isRaidable())))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockFade(BlockFadeEvent event)
	{
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
		if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onLeavesDelay(LeavesDecayEvent event)
	{
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
		if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockForm(BlockFormEvent event)
	{
		Faction factionAt = this.plugin.getFactionManager().getFactionAt(event.getBlock().getLocation());
		if (((factionAt instanceof ClaimableFaction)) && (!(factionAt instanceof PlayerFaction)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		Entity entity = event.getEntity();
		if (((entity instanceof LivingEntity)) && (!attemptBuild(entity, event.getBlock().getLocation(), null)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (!attemptBuild(event.getPlayer(), event.getBlock().getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.RED + '.'))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (!attemptBuild(event.getPlayer(), event.getBlockPlaced().getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.RED + '.'))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBucketFill(PlayerBucketFillEvent event)
	{
		if (!attemptBuild(event.getPlayer(), event.getBlockClicked().getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.RED + '.'))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBucketEmpty(PlayerBucketEmptyEvent event)
	{
		if (!attemptBuild(event.getPlayer(), event.getBlockClicked().getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.RED + '.'))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event)
	{
		Entity remover = event.getRemover();
		if (((remover instanceof Player)) && (!attemptBuild(remover, event.getEntity().getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.RED + '.')))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onHangingPlace(HangingPlaceEvent event)
	{
		if (!attemptBuild(event.getPlayer(), event.getEntity().getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.RED + '.'))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onHangingDamageByEntity(EntityDamageByEntityEvent event)
	{
		Entity entity = event.getEntity();
		if ((entity instanceof Hanging))
		{
			Player attacker = BukkitUtils.getFinalAttacker(event, false);
			if (!attemptBuild(attacker, entity.getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.YELLOW + '.'))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onHangingInteractByPlayer(PlayerInteractEntityEvent event)
	{
		Entity entity = event.getRightClicked();
		if (((entity instanceof Hanging)) && (!attemptBuild(event.getPlayer(), entity.getLocation(), ChatColor.RED + "You cannot build in the territory of §6%1$s" + ChatColor.YELLOW + '.')))
		{
			event.setCancelled(true);
		}
	}

	private static final ImmutableMultimap<Object, Object> ITEM_BLOCK_INTERACTABLES = ImmutableMultimap.builder().put(Material.DIAMOND_HOE, Material.GRASS).put(Material.GOLD_HOE, Material.GRASS).put(Material.IRON_HOE, Material.GRASS).put(Material.STONE_HOE, Material.GRASS).put(Material.WOOD_HOE, Material.GRASS).build();
	private static final ImmutableSet<Material> BLOCK_INTERACTABLES = Sets.immutableEnumSet(Material.BED, new Material[] {
			Material.BED_BLOCK,
			Material.BEACON,
			Material.FENCE_GATE,
			Material.IRON_DOOR,
			Material.TRAP_DOOR,
			Material.WOOD_DOOR,
			Material.WOODEN_DOOR,
			Material.IRON_DOOR_BLOCK,
			Material.CHEST,
			Material.TRAPPED_CHEST,
			Material.FURNACE,
			Material.BURNING_FURNACE,
			Material.BREWING_STAND,
			Material.HOPPER,
			Material.DROPPER,
			Material.DISPENSER,
			Material.STONE_BUTTON,
			Material.WOOD_BUTTON,
			Material.ENCHANTMENT_TABLE,
			Material.WORKBENCH,
			Material.ANVIL,
			Material.LEVER,
			Material.FIRE
	});
	private final HCF plugin;
}