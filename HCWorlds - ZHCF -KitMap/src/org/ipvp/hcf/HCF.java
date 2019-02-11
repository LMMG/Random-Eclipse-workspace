package org.ipvp.hcf;

import java.io.File;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.ipvp.hcf.combatlog.CombatLogListener;
import org.ipvp.hcf.command.BalCommand;
import org.ipvp.hcf.command.LogoutCommand;
import org.ipvp.hcf.command.ServerTimeCommand;
import org.ipvp.hcf.economy.EconomyCommand;
import org.ipvp.hcf.economy.EconomyManager;
import org.ipvp.hcf.economy.FlatFileEconomyManager;
import org.ipvp.hcf.economy.PayCommand;
import org.ipvp.hcf.economy.ShopSignListener;
import org.ipvp.hcf.eventgame.CaptureZone;
import org.ipvp.hcf.eventgame.EventExecutor;
import org.ipvp.hcf.eventgame.EventScheduler;
import org.ipvp.hcf.eventgame.conquest.ConquestExecutor;
import org.ipvp.hcf.eventgame.eotw.EotwCommand;
import org.ipvp.hcf.eventgame.eotw.EotwHandler;
import org.ipvp.hcf.eventgame.eotw.EotwListener;
import org.ipvp.hcf.eventgame.faction.CapturableFaction;
import org.ipvp.hcf.eventgame.faction.ConquestFaction;
import org.ipvp.hcf.eventgame.faction.KothFaction;
import org.ipvp.hcf.eventgame.koth.KothExecutor;
import org.ipvp.hcf.faction.FactionExecutor;
import org.ipvp.hcf.faction.FactionManager;
import org.ipvp.hcf.faction.FactionMember;
import org.ipvp.hcf.faction.FlatFileFactionManager;
import org.ipvp.hcf.faction.claim.Claim;
import org.ipvp.hcf.faction.claim.ClaimHandler;
import org.ipvp.hcf.faction.claim.ClaimWandListener;
import org.ipvp.hcf.faction.claim.Subclaim;
import org.ipvp.hcf.faction.type.ClaimableFaction;
import org.ipvp.hcf.faction.type.EndPortalFaction;
import org.ipvp.hcf.faction.type.Faction;
import org.ipvp.hcf.faction.type.PlayerFaction;
import org.ipvp.hcf.faction.type.RoadFaction;
import org.ipvp.hcf.faction.type.SpawnFaction;
import org.ipvp.hcf.listener.AutoSmeltOreListener;
import org.ipvp.hcf.listener.BorderListener;
import org.ipvp.hcf.listener.BottledExpListener;
import org.ipvp.hcf.listener.ChatListener;
import org.ipvp.hcf.listener.CoreListener;
import org.ipvp.hcf.listener.CrowbarListener;
import org.ipvp.hcf.listener.DeathListener;
import org.ipvp.hcf.listener.DeathMessageListener;
import org.ipvp.hcf.listener.DeathSignListener;
import org.ipvp.hcf.listener.ElevatorListener;
import org.ipvp.hcf.listener.EntityLimitListener;
import org.ipvp.hcf.listener.ExpMultiplierListener;
import org.ipvp.hcf.listener.FactionListener;
import org.ipvp.hcf.listener.FactionsCoreListener;
import org.ipvp.hcf.listener.FoundDiamondsListener;
import org.ipvp.hcf.listener.ItemStatTrackingListener;
import org.ipvp.hcf.listener.KitMapListener;
import org.ipvp.hcf.listener.PortalListener;
import org.ipvp.hcf.listener.ProtectionListener;
import org.ipvp.hcf.listener.SignSubclaimListener;
import org.ipvp.hcf.listener.SkullListener;
import org.ipvp.hcf.listener.UnRepairableListener;
import org.ipvp.hcf.listener.WorldListener;
import org.ipvp.hcf.listener.fixes.BeaconStrengthFixListener;
import org.ipvp.hcf.listener.fixes.BlockHitFixListener;
import org.ipvp.hcf.listener.fixes.BoatGlitchFixListener;
import org.ipvp.hcf.listener.fixes.EnchantLimitListener;
import org.ipvp.hcf.listener.fixes.EnderChestRemovalListener;
import org.ipvp.hcf.listener.fixes.InfinityArrowFixListener;
import org.ipvp.hcf.listener.fixes.PearlGlitchListener;
import org.ipvp.hcf.listener.fixes.PotionLimitListener;
import org.ipvp.hcf.pvpclass.PvpClassManager;
import org.ipvp.hcf.pvpclass.bard.EffectRestorer;
import org.ipvp.hcf.scoreboard.ScoreboardHandler;
import org.ipvp.hcf.sotw.SotwCommand;
import org.ipvp.hcf.sotw.SotwListener;
import org.ipvp.hcf.sotw.SotwTimer;
import org.ipvp.hcf.timer.TimerExecutor;
import org.ipvp.hcf.timer.TimerManager;
import org.ipvp.hcf.user.FactionUser;
import org.ipvp.hcf.user.UserManager;
import org.ipvp.hcf.visualise.ProtocolLibHook;
import org.ipvp.hcf.visualise.VisualiseHandler;
import org.ipvp.hcf.visualise.WallBorderListener;
import org.ipvp.hcfold.EndListener;
import org.ipvp.hcfold.EventSignListener;
import org.ipvp.hcfold.MapKitCommand;

import com.doctordark.internal.com.doctordark.base.BasePlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import lombok.Getter;

public class HCF extends JavaPlugin {

    @Getter
    private static HCF plugin;

    @Getter
    private Random random = new Random();

    @Getter
    private ClaimHandler claimHandler;

    @Getter
    private CombatLogListener combatLogListener;

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
    private FoundDiamondsListener foundDiamondsListener;


    @Getter
    private PvpClassManager pvpClassManager;

    @Getter
    private ScoreboardHandler scoreboardHandler;

    @Getter
    private SotwTimer sotwTimer;

    @Getter
    private TimerManager timerManager;

    @Getter
    private UserManager userManager;

    @Getter
    private VisualiseHandler visualiseHandler;

    @Getter
    private WorldEditPlugin worldEdit;

    public static FileConfiguration lang;
    public static File conf;
    
    @Override
    public void onEnable() {
        BasePlugin.getPlugin().init(this);
        HCF.plugin = this;
        ProtocolLibHook.hook(this);
        Plugin wep = getServer().getPluginManager().getPlugin("WorldEdit");
        this.worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;
        ConfigurationService.init(this);
        this.effectRestorer = new EffectRestorer(this);
        this.registerConfiguration();
        this.registerManagers();
        this.registerListeners();
        
        lang = getConfig();
        lang.options().copyDefaults(true);
        conf = new File(getDataFolder(), "lang.yml");
        saveConfig();
        saveDefaultConfig();
        
        	 new BukkitRunnable()
 		    {
 		      public void run()
 		      {
 		    	for(Player player : Bukkit.getOnlinePlayers()) {
 		    		if(player.hasPermission("core.alert")) {
 		    			player.sendMessage(ChatColor.GREEN + "Saved all faction and world data..");
 		    		}
 		    	}
 		    	saveData();
 		        Bukkit.savePlayers();
 		        Bukkit.getWorld("world").save();
 		        Bukkit.getWorld("world_the_end").save();
 		        Bukkit.getWorld("world_nether").save();
 		      }
 		 }  .runTaskTimerAsynchronously(HCF.plugin, 6000L, 6000L);
        
        getCommand("conquest").setExecutor(new ConquestExecutor(this));
        getCommand("economy").setExecutor(new EconomyCommand(this));
        getCommand("eotw").setExecutor(new EotwCommand(this));
        getCommand("event").setExecutor(new EventExecutor(this));
        getCommand("faction").setExecutor(new FactionExecutor(this));
        getCommand("koth").setExecutor(new KothExecutor(this));
        getCommand("logout").setExecutor(new LogoutCommand(this));
      //  getCommand("medic").setExecutor(new DonorReviveCommand(this));
        getCommand("mapkit").setExecutor(new MapKitCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("servertime").setExecutor(new ServerTimeCommand());
        getCommand("sotw").setExecutor(new SotwCommand(this));
        getCommand("timer").setExecutor(new TimerExecutor(this));
        getCommand("bal").setExecutor(new BalCommand(this));
        
        Map<String, Map<String, Object>> map = getDescription().getCommands();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            PluginCommand command = getCommand(entry.getKey());
            command.setPermission("hcf.command." + entry.getKey());
            command.setPermissionMessage(ChatColor.RED + "ERROR: No Permissions.");
        }
    }

    private void saveData() {
    	this.combatLogListener.removeCombatLoggers();
        this.economyManager.saveEconomyData();
        this.factionManager.saveFactionData();
        this.timerManager.saveTimerData();
        this.userManager.saveUserData();
    }  
    
    private void registerCooldowns() {
        Cooldowns.createCooldown("Juggernaut_cooldown");
    }

    @Override
    public void onDisable() {
    	 this.combatLogListener.removeCombatLoggers();
        this.pvpClassManager.onDisable();
        this.scoreboardHandler.clearBoards();
        this.factionManager.saveFactionData();
        this.economyManager.saveEconomyData();
        this.factionManager.saveFactionData();
        this.timerManager.saveTimerData();
        this.userManager.saveUserData();
        this.saveData();

        HCF.plugin = null; 
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
        ConfigurationSerialization.registerClass(RoadFaction.class);
        ConfigurationSerialization.registerClass(SpawnFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
    }

    private void registerListeners() {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new AutoSmeltOreListener(), this);
        manager.registerEvents(new BlockHitFixListener(), this);
        manager.registerEvents(new BoatGlitchFixListener(), this);
        manager.registerEvents(new BorderListener(), this);
        manager.registerEvents(new BottledExpListener(), this);
        manager.registerEvents(new ChatListener(this), this);
        manager.registerEvents(new ClaimWandListener(this), this);
        manager.registerEvents(combatLogListener = new CombatLogListener(this), this);
        manager.registerEvents(new CoreListener(this), this);
        manager.registerEvents(new CrowbarListener(this), this);
        manager.registerEvents(new DeathListener(this), this);
        manager.registerEvents(new DeathMessageListener(this), this);
        manager.registerEvents(new DeathSignListener(this), this);
        manager.registerEvents(new EnchantLimitListener(), this);
        manager.registerEvents(new EnderChestRemovalListener(), this);
        manager.registerEvents(new EntityLimitListener(), this);
        manager.registerEvents(new EndListener(), this);
        manager.registerEvents(new EotwListener(this), this);
        manager.registerEvents(new EventSignListener(), this);
        manager.registerEvents(new ExpMultiplierListener(), this);
        manager.registerEvents(new FactionListener(this), this);
        manager.registerEvents(this.foundDiamondsListener = new FoundDiamondsListener(this), this);
        manager.registerEvents(new InfinityArrowFixListener(), this);
        manager.registerEvents(new KitMapListener(this), this);
        manager.registerEvents(new PearlGlitchListener(this), this);
        manager.registerEvents(new PortalListener(this), this);
        manager.registerEvents(new PotionLimitListener(), this);
        manager.registerEvents(new ProtectionListener(this), this);
        manager.registerEvents(new SignSubclaimListener(this), this);
        manager.registerEvents(new ShopSignListener(this), this);
        manager.registerEvents(new SkullListener(), this);
        manager.registerEvents(new ItemStatTrackingListener(), this);
        manager.registerEvents(new SotwListener(this), this);
        manager.registerEvents(new BeaconStrengthFixListener(), this);
        manager.registerEvents(new UnRepairableListener(), this);
        manager.registerEvents(new WallBorderListener(this), this);
        manager.registerEvents(new WorldListener(this), this);
        //manager.registerEvents(new ToxicityListener(), this);
        manager.registerEvents(new ElevatorListener(), this);
        manager.registerEvents(new FactionsCoreListener(this), this);
    }

    private void registerManagers() {
        this.claimHandler = new ClaimHandler(this);
        this.economyManager = new FlatFileEconomyManager(this);
        this.eotwHandler = new EotwHandler(this);
        this.eventScheduler = new EventScheduler(this);
        this.factionManager = new FlatFileFactionManager(this);
        this.pvpClassManager = new PvpClassManager(this);
        this.sotwTimer = new SotwTimer();
        this.timerManager = new TimerManager(this); // needs to be registered before ScoreboardHandler
        this.scoreboardHandler = new ScoreboardHandler(this);
        this.userManager = new UserManager(this);
        this.visualiseHandler = new VisualiseHandler();
    }
}
