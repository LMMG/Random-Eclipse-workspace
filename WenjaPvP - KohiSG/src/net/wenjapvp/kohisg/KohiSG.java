package net.wenjapvp.kohisg;

import net.minecraft.util.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.wenjapvp.kohisg.border.BorderManager;
import net.wenjapvp.kohisg.commands.HubCommand;
import net.wenjapvp.kohisg.commands.SurvivalGamesCommand;
import net.wenjapvp.kohisg.listeners.ChatFormat;
import net.wenjapvp.kohisg.listeners.EnderpearlListener;
import net.wenjapvp.kohisg.loot.ChestConfiguration;
import net.wenjapvp.kohisg.profile.Profile;
import net.wenjapvp.kohisg.schematic.SchematicPopulator;
import net.wenjapvp.kohisg.scoreboard.ScoreboardHelper;
import net.wenjapvp.kohisg.spectate.SpectateListener;
import net.wenjapvp.kohisg.spectate.SpectateManager;
import net.wenjapvp.kohisg.task.GameTimer;
import net.wenjapvp.kohisg.task.SplitedRoadProcessor;
import net.wenjapvp.kohisg.utils.Color;
import net.wenjapvp.kohisg.utils.ItemStackUtils;
import net.wenjapvp.kohisg.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class KohiSG extends JavaPlugin implements Listener
{
	private static KohiSG instance;

	public static final int FEAST_OFFSET = 180;

	private GameState gameState;
	private GameTimer gameTimer;

	private ChestConfiguration chestConfiguration;
	private final ThreadLocal<ChestConfiguration> feastConfiguration = new ThreadLocal<>();

	private SpectateListener spectateListener;
	private EnderpearlListener enderpearlListener;

	private BorderManager borderManager;
	private SpectateManager spectateManager;

	private final Map<Player, ScoreboardHelper> helper = new HashMap<>();
	private final Map<Location, BrewingStand> activeStands = new HashMap<>();
	private final Set<Player> playerList = new HashSet<>();

	public static KohiSG getInstance()
	{
		if (instance == null)
		{
			instance = new KohiSG();
		}
		return instance;
	}

	@Override
	public void onEnable()
	{
		setup();
		getCommand("hub").setExecutor(new HubCommand());

	}

	public void setup()
	{
		instance = this;

		borderManager = new BorderManager(this);

		gameState = GameState.WAITING;

		gameTimer = new GameTimer();

		chestConfiguration = new ChestConfiguration(this, "chest.yml");
		feastConfiguration.set(new ChestConfiguration(this, "feast_chest.yml"));


		try
		{
			FileUtils.deleteDirectory(new File("world"));
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		setupScoreboard();

		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			onPlayerJoin(player);
		}

		gameTimer.start();
		borderManager.init();
		chestConfiguration.load();
		feastConfiguration.get().load();

		new BrewingUpdateTask().runTaskTimer(this, 1L, 1L);

		Bukkit.getServer().getPluginManager().registerEvents(new Profile(), this);
		spectateManager = new SpectateManager();
		enderpearlListener = new EnderpearlListener();
		Bukkit.getServer().getPluginManager().registerEvents(enderpearlListener, this);
		spectateListener = new SpectateListener();
		Bukkit.getServer().getPluginManager().registerEvents(spectateListener, this);

		Bukkit.getServer().getPluginManager().registerEvents(new ChatFormat(), this);

		Bukkit.getServer().getPluginCommand("survivalgames").setExecutor(new SurvivalGamesCommand());
	}

	public GameTimer getGameTimer()
	{
		return gameTimer;
	}

	public ChestConfiguration getChestConfiguration()
	{
		return chestConfiguration;
	}

	public ChestConfiguration getFeastConfiguration()
	{
		return feastConfiguration.get();
	}

	public BorderManager getBorderManager()
	{
		return borderManager;
	}

	public SpectateManager getSpectateManager()
	{
		return spectateManager;
	}

	public EnderpearlListener getEnderpearlListener()
	{
		return enderpearlListener;
	}

	public SpectateListener getSpectateListener()
	{
		return spectateListener;
	}

	public GameState getGameState()
	{
		return gameState;
	}

	public void setGameState(GameState gameState)
	{
		this.gameState = gameState;
	}

	public Set<Player> getPlayerList()
	{
		return playerList;
	}

	public void buildPlayerList()
	{
		for (Player everyone : Bukkit.getServer().getOnlinePlayers())
		{
			playerList.add(everyone);
		}
	}

	public boolean isPvpTime()
	{
		if (gameState == GameState.GAME)
		{
			int left = gameTimer.getLeft() - 540;
			if (left > 0) { return true; }
		}
		return false;
	}

	private boolean isInventoryEmpty(Inventory inventory)
	{
		for (ItemStack itemStack : inventory.getContents())
		{
			if (itemStack != null && itemStack.getType() != Material.AIR) { return false; }
		}
		return true;
	}

	private void breakChest(Chest chest)
	{
		chest.getBlock().setType(Material.AIR);
		chest.getWorld().playSound(chest.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.2F, 1.0F);
	}

	private void onPlayerJoin(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (player.isOnline())
				{
					Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
					ScoreboardHelper scoreboardHelper = new ScoreboardHelper(scoreboard, ("&6&lWenjaPvP &c[SG]"));
					helper.put(player, scoreboardHelper);
				}
			}
		}.runTaskLater(this, 20L);
	}

	private void setupScoreboard()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for (Map.Entry<Player, ScoreboardHelper> entry : helper.entrySet())
				{
					Player player = entry.getKey();
					ScoreboardHelper scoreboardHelper = entry.getValue();
					scoreboardHelper.clear();

					scoreboardHelper.add(Color.translate("&7&m------------------------"));
					int players = 0;
					if (gameState == GameState.WAITING || gameState == GameState.END)
					{
						players = Bukkit.getOnlinePlayers().length;
					}
					else
					{
						players = playerList.size();
					}
					scoreboardHelper.add(Color.translate("&e&lPlayers: &f" + players));

					if (isPvpTime())
					{
						int left = gameTimer.getLeft() - 540;
						scoreboardHelper.add(Color.translate("&c&lPvP Prot: &f" + TimeUtils.IntegerCountdown.setFormat(left)));
					}

					if (gameState == GameState.GAME)
					{
						int left = gameTimer.getLeft() - 180;
						if (left > 0)
						{
							scoreboardHelper.add("&b&lFeast: &f" + TimeUtils.IntegerCountdown.setFormat(left));
						}
					}

					scoreboardHelper.add("&3&lBorder: &f" + borderManager.getBorder());

					if (getEnderpearlListener().isCooldownActive(player))
					{
						scoreboardHelper.add("&e&lEnderpearl: &f" + TimeUtils.LongCountdown.setFormat(getEnderpearlListener().getMillisecondLeft(player)));
					}

					if (player.isOp())
					{
						scoreboardHelper.add(Color.translate("&7&m------------------------"));
						scoreboardHelper.add("&6&lStaff Mode »");
						scoreboardHelper.add("&6&l» &7 Allocated Memory: " + net.wenjapvp.kohisg.utils.SystemUtils.getMaximumMemory());
						scoreboardHelper.add("&6&l» &7 Total Memory: " + net.wenjapvp.kohisg.utils.SystemUtils.getTotalMemory());
						scoreboardHelper.add("&6&l» &7 Free Memory: " + net.wenjapvp.kohisg.utils.SystemUtils.getFreeMemory());
					}

					scoreboardHelper.add(Color.translate("&7&m------------------------"));
					scoreboardHelper.update(player);
				}
			}
		}.runTaskTimer(this, 0L, 3L);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.PHYSICAL && event.hasItem() && event.getItem().getType() == Material.COMPASS)
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			Player targetp = null;
			double distance = Double.MAX_VALUE;
			for (Player op : Bukkit.getServer().getOnlinePlayers())
			{
				if ((op != player) && (!this.spectateManager.isSpectating(op)))
				{
					if (distance > player.getLocation().distanceSquared(op.getLocation()))
					{
						targetp = op;
						distance = player.getLocation().distanceSquared(targetp.getLocation());
					}
				}
			}
			if (targetp != null)
			{
				player.setCompassTarget(targetp.getLocation());
				player.sendMessage(ChatColor.YELLOW + "Tracking player " + targetp.getName());
				ItemStackUtils.setItemTitle(event.getItem(), ChatColor.YELLOW + "Tracking player " + targetp.getName());
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW + "Target not found");
			}
		}
	}

	@EventHandler
	public void onPlayerInitialSpawn(PlayerInitialSpawnEvent event)
	{
		event.setSpawnLocation(new Location(event.getPlayer().getWorld(), 8.0D, 65.0D, 8.0D));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		event.setJoinMessage(null);

		Player player = event.getPlayer();
		if (gameState == GameState.GAME || gameState == GameState.DEATH_MATH)
		{
			for (Player other : Bukkit.getServer().getOnlinePlayers())
			{
				other.hidePlayer(player);
				player.hidePlayer(other);
			}
			event.setJoinMessage(null);
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				spectateManager.spectate(player);
			}
		}.runTaskLater(this, 17L);
		onPlayerJoin(player);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (playerList.contains(event.getEntity()))
		{
			playerList.remove(event.getEntity());
			event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if (gameState == GameState.GAME || gameState == GameState.DEATH_MATH)
		{
			Player player = event.getPlayer();
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					spectateManager.spectate(player);
				}
			}.runTask(this);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(null);
		helper.remove(event.getPlayer());
		if (playerList.contains(event.getPlayer()))
		{
			playerList.remove(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		event.setLeaveMessage(null);
		helper.remove(event.getPlayer());
		if (playerList.contains(event.getPlayer()))
		{
			playerList.remove(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldInit(WorldInitEvent event)
	{
		event.getWorld().setSpawnLocation(0, 0, 0);
		event.getWorld().getPopulators().add(new SchematicPopulator(chestConfiguration));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		event.setDroppedExp((int) (event.getDroppedExp() * 2.75D));
	}

	@EventHandler
	public void onWorldLoadEvent(WorldLoadEvent event)
	{
		new SplitedRoadProcessor(this, event.getWorld().getSpawnLocation(), 15000, 2).run();
	}

	private Player getPlayer(Entity entity)
	{
		if (entity instanceof Player) { return (Player) entity; }
		if (entity instanceof Projectile)
		{
			Projectile projectile = (Projectile) entity;
			if (projectile.getShooter() instanceof Player && projectile.getShooter() != entity)
			{
				Player player = (Player) projectile.getShooter();
				return player;
			}
		}
		return null;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageByEntityEvent event)
	{
		Player damager = getPlayer(event.getDamager());
		Player damagee = getPlayer(event.getEntity());
		if (damager != null && damagee != null && isPvpTime())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Chest)
		{
			Chunk chunk = event.getClickedBlock().getChunk();
			if (chunk.getX() == 0 && chunk.getZ() == 0) { return; }
			breakChest((Chest) event.getClickedBlock().getState());
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if (event.getInventory() instanceof DoubleChestInventory)
		{
			DoubleChestInventory inventory = (DoubleChestInventory) event.getInventory();
			Inventory left = inventory.getLeftSide();
			Inventory right = inventory.getRightSide();
			if (inventory.getViewers().size() == 1)
			{
				if (isInventoryEmpty(left))
				{
					Chest chest = (Chest) left.getHolder();
					breakChest(chest);
				}
				if (isInventoryEmpty(right))
				{
					Chest chest = (Chest) right.getHolder();
					breakChest(chest);
				}
			}
		}
		else
		{
			Inventory inventory = event.getInventory();
			if (inventory.getHolder() instanceof Chest)
			{
				Chest chest = (Chest) inventory.getHolder();
				if (inventory.getViewers().size() == 1 && isInventoryEmpty(inventory))
				{
					breakChest(chest);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractBrewingStand(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			BlockState state = event.getClickedBlock().getState();
			if (state instanceof BrewingStand)
			{
				activeStands.put(state.getLocation(), (BrewingStand) state);
			}
		}
	}

	public class BrewingUpdateTask extends BukkitRunnable
	{
		public BrewingUpdateTask()
		{}

		@Override
		public void run()
		{
			Iterator<Map.Entry<Location, BrewingStand>> iter = KohiSG.this.activeStands.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry<Location, BrewingStand> entry = (Map.Entry) iter.next();
				if (!((BrewingStand) entry.getValue()).getChunk().isLoaded() || ((Location) entry.getKey()).getBlock().getType() != Material.BREWING_STAND)
				{
					iter.remove();
				}
				else
				{
					BrewingStand stand = (BrewingStand) entry.getValue();
					if (stand.getBrewingTime() > 1)
					{
						stand.setBrewingTime(Math.max(1, stand.getBrewingTime() - 2));
					}
				}
			}
		}
	}
}