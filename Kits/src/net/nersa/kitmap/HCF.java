package net.nersa.kitmap;

import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.nersa.backend.DatabaseCredentials;
import net.nersa.backend.StorageBackend;
import net.nersa.backend.type.StorageBackendMySQL;
import net.nersa.kitmap.combatlog.CombatLogListener;
import net.nersa.kitmap.command.ChestCommand;
import net.nersa.kitmap.command.EconomyCommand;
import net.nersa.kitmap.command.FocusCommand;
import net.nersa.kitmap.command.GoppleCommand;
import net.nersa.kitmap.command.HelpCommand;
import net.nersa.kitmap.command.LogoutCommand;
import net.nersa.kitmap.command.PayCommand;
import net.nersa.kitmap.command.RulesCommand;
import net.nersa.kitmap.command.ServerTimeCommand;
import net.nersa.kitmap.command.SpawnCommand;
import net.nersa.kitmap.economy.EconomyManager;
import net.nersa.kitmap.economy.FlatFileEconomyManager;
import net.nersa.kitmap.economy.ShopSignListener;
import net.nersa.kitmap.eventgame.CaptureZone;
import net.nersa.kitmap.eventgame.EventExecutor;
import net.nersa.kitmap.eventgame.EventScheduler;
import net.nersa.kitmap.eventgame.crate.KeyListener;
import net.nersa.kitmap.eventgame.crate.KeyManager;
import net.nersa.kitmap.eventgame.faction.CapturableFaction;
import net.nersa.kitmap.eventgame.faction.ConquestFaction;
import net.nersa.kitmap.eventgame.faction.KothFaction;
import net.nersa.kitmap.eventgame.koth.KothExecutor;
import net.nersa.kitmap.faction.FactionExecutor;
import net.nersa.kitmap.faction.FactionManager;
import net.nersa.kitmap.faction.FactionMember;
import net.nersa.kitmap.faction.FlatFileFactionManager;
import net.nersa.kitmap.faction.claim.Claim;
import net.nersa.kitmap.faction.claim.ClaimHandler;
import net.nersa.kitmap.faction.claim.ClaimWandListener;
import net.nersa.kitmap.faction.claim.Subclaim;
import net.nersa.kitmap.faction.claim.SubclaimWandListener;
import net.nersa.kitmap.faction.type.ClaimableFaction;
import net.nersa.kitmap.faction.type.EndPortalFaction;
import net.nersa.kitmap.faction.type.Faction;
import net.nersa.kitmap.faction.type.PlayerFaction;
import net.nersa.kitmap.faction.type.SpawnFaction;
import net.nersa.kitmap.listener.BorderListener;
import net.nersa.kitmap.listener.ChatListener;
import net.nersa.kitmap.listener.CoreListener;
import net.nersa.kitmap.listener.DeathListener;
import net.nersa.kitmap.listener.DeathMessageListener;
import net.nersa.kitmap.listener.EndListener;
import net.nersa.kitmap.listener.EntityLimitListener;
import net.nersa.kitmap.listener.EventSignListener;
import net.nersa.kitmap.listener.FactionListener;
import net.nersa.kitmap.listener.FocusListener;
import net.nersa.kitmap.listener.KillstreakListener;
import net.nersa.kitmap.listener.KitMapListener;
import net.nersa.kitmap.listener.PlayTimeManager;
import net.nersa.kitmap.listener.PortalListener;
import net.nersa.kitmap.listener.ProtectionListener;
import net.nersa.kitmap.listener.SkullListener;
import net.nersa.kitmap.listener.WorldListener;
import net.nersa.kitmap.listener.fixes.BlockHitFixListener;
import net.nersa.kitmap.listener.fixes.BlockJumpGlitchFixListener;
import net.nersa.kitmap.listener.fixes.BoatGlitchFixListener;
import net.nersa.kitmap.listener.fixes.CreativeClickListener;
import net.nersa.kitmap.listener.fixes.DupeListener;
import net.nersa.kitmap.listener.fixes.EnchantLimitListener;
import net.nersa.kitmap.listener.fixes.EnderChestRemovalListener;
import net.nersa.kitmap.listener.fixes.HitDetectionListener;
import net.nersa.kitmap.listener.fixes.InfinityArrowFixListener;
import net.nersa.kitmap.listener.fixes.PearlGlitchListener;
import net.nersa.kitmap.listener.fixes.PotionLimitListener;
import net.nersa.kitmap.listener.fixes.VoidGlitchFixListener;
import net.nersa.kitmap.pvpclass.PvpClassManager;
import net.nersa.kitmap.pvpclass.bard.EffectRestorer;
import net.nersa.kitmap.timer.TimerExecutor;
import net.nersa.kitmap.timer.TimerManager;
import net.nersa.kitmap.visualise.ProtocolLibHook;
import net.nersa.kitmap.visualise.VisualiseHandler;
import net.nersa.kitmap.visualise.WallBorderListener;
import net.nersa.player.PlayerManager;
import net.nersa.scoreboard.ScoreboardHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.doctordark.internal.com.doctordark.base.BasePlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class HCF extends JavaPlugin {

	@Getter private static HCF instance;
	@Getter private Random random = new Random();
	@Getter private ClaimHandler claimHandler;
	@Getter private CombatLogListener combatLogListener;
	@Getter private EconomyManager economyManager;
	@Getter private EffectRestorer effectRestorer;
	@Getter private EventScheduler eventScheduler;
	@Getter private FactionManager factionManager;
	@Getter private PlayTimeManager playTimeManager;
	@Getter private KeyManager keyManager;
	@Getter private PvpClassManager pvpClassManager;
	@Getter public ScoreboardHandler scoreboardHandler;
	@Getter private TimerManager timerManager;
	@Getter private PlayerManager playerManager;
	@Getter private StorageBackend storageBackend;
	@Getter private VisualiseHandler visualiseHandler;
	@Getter private WorldEditPlugin worldEdit;
    private static Permission perms = null;
    private static Chat chat = null;

	@Override
	public void onEnable() {
		instance = this;

		TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));

//		BasePlugin.getPlugin().init(this);
		ProtocolLibHook.hook(this);
		ConfigurationService.init(this);

		Plugin wep = getServer().getPluginManager().getPlugin("WorldEdit");
		worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;

		effectRestorer = new EffectRestorer(this);
		storageBackend = new StorageBackendMySQL(new DatabaseCredentials("localhost", 3306, "root", "masonc12", "sql2196118"));

		registerConfiguration();
		registerCommands();
		registerManagers();
		registerListeners();
		setupPermissions();
		setupChat();

		new BukkitRunnable() {
			@Override
			public void run() {
				saveData();
			}
		}.runTaskTimerAsynchronously(instance, TimeUnit.MINUTES.toMillis(10L), TimeUnit.MINUTES.toMillis(10L));

		new BukkitRunnable() {

			@Override
			public void run() {
				for (World w : Bukkit.getWorlds()) {
					w.setWeatherDuration(0);
					w.setThunderDuration(0);
				}
			}
		}.runTaskTimerAsynchronously(instance, 20, 20);
	}

	private void saveData() {
		this.playTimeManager.savePlaytimeData();
		this.factionManager.saveFactionData();
		this.economyManager.saveEconomyData();
		this.factionManager.saveFactionData();
		this.keyManager.saveKeyData();
		this.timerManager.saveTimerData();
		this.playerManager.savePlayerData();
	}

	@Override
	public void onDisable() {
		CombatLogListener.removeCombatLoggers();
		this.pvpClassManager.onDisable();
		this.saveData();
		this.getStorageBackend().closeConnections();
		HCF.instance = null;
	}

	private void registerConfiguration() {
		ConfigurationSerialization.registerClass(CaptureZone.class);
		ConfigurationSerialization.registerClass(Claim.class);
		ConfigurationSerialization.registerClass(Subclaim.class);
		ConfigurationSerialization.registerClass(ClaimableFaction.class);
		ConfigurationSerialization.registerClass(ConquestFaction.class);
		ConfigurationSerialization.registerClass(CapturableFaction.class);
		ConfigurationSerialization.registerClass(KothFaction.class);
		ConfigurationSerialization.registerClass(EndPortalFaction.class);
		ConfigurationSerialization.registerClass(Faction.class);
		ConfigurationSerialization.registerClass(FactionMember.class);
		ConfigurationSerialization.registerClass(PlayerFaction.class);
		ConfigurationSerialization.registerClass(SpawnFaction.class);
	}

	private void registerListeners() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(this.playTimeManager = new PlayTimeManager(this), this);
		manager.registerEvents(new BlockHitFixListener(), this);
		manager.registerEvents(new BlockJumpGlitchFixListener(), this);
		manager.registerEvents(new BoatGlitchFixListener(), this);
		manager.registerEvents(new KillstreakListener(), this);
		manager.registerEvents(new BorderListener(), this);
		manager.registerEvents(new ChatListener(this), this);
		manager.registerEvents(new ClaimWandListener(this), this);
		manager.registerEvents((this.combatLogListener = new CombatLogListener(this)), this);
		manager.registerEvents(new CoreListener(this), this);
		manager.registerEvents(new CreativeClickListener(), this);
		manager.registerEvents(new DeathListener(this), this);
		manager.registerEvents(new DeathMessageListener(this), this);
		manager.registerEvents(new EnchantLimitListener(), this);
		manager.registerEvents(new EnderChestRemovalListener(), this);
		manager.registerEvents(new EntityLimitListener(), this);
		manager.registerEvents(new EndListener(), this);
		manager.registerEvents(new EventSignListener(), this);
		manager.registerEvents(new FactionListener(this), this);
		manager.registerEvents(new InfinityArrowFixListener(), this);
		manager.registerEvents(new KeyListener(this), this);
		manager.registerEvents(new KitMapListener(this), this);
		manager.registerEvents(new PearlGlitchListener(this), this);
		manager.registerEvents(new PortalListener(this), this);
		manager.registerEvents(new PotionLimitListener(), this);
		manager.registerEvents(new ProtectionListener(this), this);
		manager.registerEvents(new SubclaimWandListener(this), this);
		manager.registerEvents(new ShopSignListener(this), this);
		manager.registerEvents(new SkullListener(), this);
		manager.registerEvents(new VoidGlitchFixListener(), this);
		manager.registerEvents(new WallBorderListener(this), this);
		manager.registerEvents(new WorldListener(this), this);
		manager.registerEvents(new DupeListener(), this);
		manager.registerEvents(new HitDetectionListener(), this);
		manager.registerEvents(new FocusListener(), this);
		KillstreakListener.setupItems();
	}

	private void registerCommands() {
		getCommand("chest").setExecutor(new ChestCommand(this));
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("spawn").setExecutor(new SpawnCommand(this));
		getCommand("event").setExecutor(new EventExecutor(this));
		getCommand("rules").setExecutor(new RulesCommand());
		getCommand("hcfhelp").setExecutor(new HelpCommand());
		getCommand("faction").setExecutor(new FactionExecutor(this));
		getCommand("gopple").setExecutor(new GoppleCommand(this));
		getCommand("koth").setExecutor(new KothExecutor(this));
		getCommand("logout").setExecutor(new LogoutCommand(this));
		getCommand("pay").setExecutor(new PayCommand(this));
		getCommand("servertime").setExecutor(new ServerTimeCommand());
		getCommand("timer").setExecutor(new TimerExecutor(this));
		getCommand("focus").setExecutor(new FocusCommand());

		Map<String, Map<String, Object>> map = getDescription().getCommands();

		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			PluginCommand command = getCommand(entry.getKey());
			command.setPermission("kitmap.command." + entry.getKey());
			command.setPermissionMessage(ChatColor.RED + "You do not have permission for this.");
		}
	}

	private void registerManagers() {
		this.claimHandler = new ClaimHandler(this);
		this.economyManager = new FlatFileEconomyManager(this);
		this.eventScheduler = new EventScheduler(this);
		this.factionManager = new FlatFileFactionManager(this);
		this.keyManager = new KeyManager(this);
		this.pvpClassManager = new PvpClassManager(this);
		this.timerManager = new TimerManager(this);
		this.scoreboardHandler = new ScoreboardHandler(this);
		this.playerManager = new PlayerManager();
		this.visualiseHandler = new VisualiseHandler();
	}
	
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
	
    public static Permission getPermissions() {
        return perms;
    }
    
    public static Chat getChat() {
        return chat;
    }
}