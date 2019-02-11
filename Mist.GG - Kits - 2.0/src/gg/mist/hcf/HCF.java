package gg.mist.hcf;

import java.util.Map;
import java.util.Random;

import gg.mist.hcf.eventgame.eotw.EotwListener;
import gg.mist.hcf.faction.type.EndPortalFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.doctordark.internal.com.doctordark.base.BasePlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import lombok.Getter;
import gg.mist.hcf.combatlog.CustomEntityRegistration;
import gg.mist.hcf.economy.EconomyCommand;
import gg.mist.hcf.economy.EconomyManager;
import gg.mist.hcf.economy.PayCommand;
import gg.mist.hcf.eventgame.CaptureZone;
import gg.mist.hcf.eventgame.EventExecutor;
import gg.mist.hcf.eventgame.crate.KeyListener;
import gg.mist.hcf.eventgame.crate.KeyManager;
import gg.mist.hcf.eventgame.eotw.EotwCommand;
import gg.mist.hcf.eventgame.eotw.EotwHandler;
import gg.mist.hcf.eventgame.faction.CapturableFaction;
import gg.mist.hcf.eventgame.faction.ConquestFaction;
import gg.mist.hcf.faction.FactionExecutor;
import gg.mist.hcf.faction.FactionManager;
import gg.mist.hcf.faction.FactionMember;
import gg.mist.hcf.faction.FlatFileFactionManager;
import gg.mist.hcf.faction.argument.FactionClaimChunkArgument;
import gg.mist.hcf.faction.claim.ClaimHandler;
import gg.mist.hcf.faction.claim.ClaimWandListener;
import gg.mist.hcf.faction.claim.Subclaim;
import gg.mist.hcf.faction.type.Faction;
import gg.mist.hcf.faction.type.SpawnFaction;
import gg.mist.hcf.pvpclass.PvpClassManager;
import gg.mist.hcf.pvpclass.bard.EffectRestorer;
import gg.mist.hcf.sotw.SotwCommand;
import gg.mist.hcf.sotw.SotwListener;
import gg.mist.hcf.timer.TimerExecutor;
import gg.mist.hcf.timer.TimerManager;
import gg.mist.hcf.user.FactionUser;
import gg.mist.hcf.user.UserManager;
import gg.mist.hcf.visualise.VisualiseHandler;
import gg.mist.hcf.visualise.WallBorderListener;
import gg.mist.hcfold.MapKitCommand;
import gg.mist.hcf.combatlog.CombatLogListener;
import gg.mist.hcf.config.ConfigurationService;
import gg.mist.hcf.economy.FlatFileEconomyManager;
import gg.mist.hcf.economy.ShopSignListener;
import gg.mist.hcf.eventgame.EventScheduler;
import gg.mist.hcf.eventgame.conquest.ConquestExecutor;
import gg.mist.hcf.eventgame.faction.KothFaction;
import gg.mist.hcf.eventgame.koth.KothExecutor;
import gg.mist.hcf.faction.argument.FactionManagerArgument;
import gg.mist.hcf.faction.claim.Claim;
import gg.mist.hcf.faction.type.ClaimableFaction;
import gg.mist.hcf.faction.type.PlayerFaction;
import gg.mist.hcf.listener.CoreListener;
import gg.mist.hcf.listener.CrowbarListener;
import gg.mist.hcf.listener.DeathListener;
import gg.mist.hcf.listener.FactionListener;
import gg.mist.hcf.listener.ProtectionListener;
import gg.mist.hcf.listener.SignSubclaimListener;
import gg.mist.hcf.sotw.SotwTimer;
import gg.mist.hcf.visualise.ProtocolLibHook;
import gg.mist.hcfold.EndListener;
import gg.mist.hcfold.EventSignListener;

public class HCF extends JavaPlugin {

	public static HCF plugin;
	@Getter
	private Random random = new Random();
	@Getter
	private ClaimHandler claimHandler;
	@Getter
	private EconomyManager economyManager;
	@Getter
	private EffectRestorer effectRestorer;
	@Getter
	private EotwHandler eotwHandler;
	@Getter
	private EventScheduler eventScheduler;
	@Getter
	private FactionManager factionManager;
	@Getter
	private PvpClassManager pvpClassManager;
	@Getter
	private SotwTimer sotwTimer;
	@Getter
	public static TimerManager timerManager;
	@Getter
	private KeyManager keyManager;
	@Getter
	private UserManager userManager;
	@Getter
	private VisualiseHandler visualiseHandler;
	@Getter
	private WorldEditPlugin worldEdit;
	@Getter
	private CombatLogListener combatLogListener;

	public static HCF getPlugin() {
		return plugin;
	}

	public TimerManager getTimerManager() {
		return timerManager;
	}

	@Override
	public void onEnable() {
		Cooldowns.createCooldown("medic_cooldown");

		HCF.plugin = this;
		BasePlugin.getPlugin().init(this);

		ProtocolLibHook.hook(this);

		Plugin wep = getServer().getPluginManager().getPlugin("WorldEdit");
		this.worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;
		CustomEntityRegistration.registerCustomEntities();

		ConfigurationService.init(this);
		this.effectRestorer = new EffectRestorer(this);
		this.registerConfiguration();
		this.registerCommands();

		this.claimHandler = new ClaimHandler(this);
		this.economyManager = new FlatFileEconomyManager(this);
		this.eotwHandler = new EotwHandler(this);
		this.eventScheduler = new EventScheduler(this);
		this.factionManager = new FlatFileFactionManager(this);
		this.keyManager = new KeyManager(this);
		this.pvpClassManager = new PvpClassManager(this);
		this.sotwTimer = new SotwTimer();
		this.timerManager = new TimerManager(this); // needs to be registered
													// before ScoreboardHandler
		this.userManager = new UserManager(this);
		this.visualiseHandler = new VisualiseHandler();

		this.registerListeners();
	}

	private void saveData() {
		this.combatLogListener.removeCombatLoggers();
		this.economyManager.saveEconomyData();
		this.factionManager.saveFactionData();
		this.keyManager.saveKeyData();
		this.timerManager.saveTimerData();
		this.userManager.saveUserData();
	}

	@Override
	public void onDisable() {
		this.combatLogListener.removeCombatLoggers();
		this.pvpClassManager.onDisable();
		this.factionManager.saveFactionData();
		this.economyManager.saveEconomyData();
		this.factionManager.saveFactionData();
		this.keyManager.saveKeyData();
		this.timerManager.saveTimerData();
		this.userManager.saveUserData();
		this.saveData();

		HCF.plugin = null; // always initialise last
	}

	private void registerConfiguration() {
		ConfigurationSerialization.registerClass(CaptureZone.class);
		ConfigurationSerialization.registerClass(Claim.class);
		ConfigurationSerialization.registerClass(Subclaim.class);
		ConfigurationSerialization.registerClass(FactionUser.class);
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
		manager.registerEvents(this.combatLogListener = new CombatLogListener(this), this);
		manager.registerEvents(new FactionManagerArgument(this), this);
		manager.registerEvents(new FactionClaimChunkArgument(this), this);
		manager.registerEvents(new ClaimWandListener(this), this);
		manager.registerEvents(new CoreListener(this), this);
		manager.registerEvents(new CrowbarListener(this), this);
		manager.registerEvents(new DeathListener(this), this);
		manager.registerEvents(new EndListener(), this);
		manager.registerEvents(new EotwListener(this), this);
		manager.registerEvents(new EventSignListener(), this);
		manager.registerEvents(new FactionListener(this), this);
		manager.registerEvents(new KeyListener(this), this);
		manager.registerEvents(new ProtectionListener(this), this);
		manager.registerEvents(new SignSubclaimListener(this), this);
		manager.registerEvents(new ShopSignListener(this), this);
		manager.registerEvents(new SotwListener(this), this);
		manager.registerEvents(new WallBorderListener(this), this);
	}

	private void registerCommands() {
		getCommand("conquest").setExecutor( new ConquestExecutor(this));
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("eotw").setExecutor(new EotwCommand(this));
		getCommand("event").setExecutor( new EventExecutor(this));
		getCommand("faction").setExecutor( new FactionExecutor(this));
		getCommand("koth").setExecutor( new KothExecutor(this));
		getCommand("mapkit").setExecutor(new MapKitCommand(this));
		getCommand("pay").setExecutor(new PayCommand(this));
		getCommand("sotw").setExecutor(new SotwCommand(this));
		getCommand("timer").setExecutor( new TimerExecutor(this));

		Map<String, Map<String, Object>> map = getDescription().getCommands();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			PluginCommand command = getCommand(entry.getKey());
			command.setPermission("" + entry.getKey());
			command.setPermissionMessage(ChatColor.RED + "You do not have permissions to execute this command.");
		}
	}

	private void registerCooldowns() {
		Cooldowns.createCooldown("medic_cooldown");
	}

	public EotwHandler getEotwHandler() {
		return eotwHandler;
	}

	public FactionManager getFactionManager() {
		return factionManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public Random getRandom() {
		return random;
	}

	public SotwTimer getSotwTimer() {
		return sotwTimer;
	}

	public PvpClassManager getPvpClassManager() {
		return pvpClassManager;
	}

	public EffectRestorer getEffectRestorer() {
		return effectRestorer;
	}

	public EconomyManager getEconomyManager() {
		return economyManager;
	}

	public VisualiseHandler getVisualiseHandler() {
		return visualiseHandler;
	}

	public EventScheduler getEventScheduler() {
		return eventScheduler;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}
}
