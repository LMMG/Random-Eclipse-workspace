package us.kirai.bunkers;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import us.kirai.bunkers.commands.ForceStart;
import us.kirai.bunkers.commands.LobbySpawn;
import us.kirai.bunkers.commands.RemoveTab;
import us.kirai.bunkers.commands.TeamCommand;
import us.kirai.bunkers.config.ConfigurationService;
import us.kirai.bunkers.game.GameHandler;
import us.kirai.bunkers.game.managers.BalanceManager;
import us.kirai.bunkers.game.managers.ChatManager;
import us.kirai.bunkers.game.managers.CooldownManager;
import us.kirai.bunkers.game.managers.DTRManager;
import us.kirai.bunkers.game.managers.FreezeManager;
import us.kirai.bunkers.game.managers.ItemManager;
import us.kirai.bunkers.game.managers.PointManager;
import us.kirai.bunkers.game.managers.ShopManager;
import us.kirai.bunkers.game.managers.TeamJoinManager;
import us.kirai.bunkers.game.managers.TeamManager;
import us.kirai.bunkers.scoreboard.ScoreboardHandler;
import us.kirai.bunkers.tab.TabEvent;
import us.kirai.bunkers.tab.api.TablistManager;
import us.kirai.bunkers.world.ConfigManager;
import us.kirai.bunkers.world.WorldManager;


public class Bunkers extends JavaPlugin
{
    private static Bunkers instance;
    private static Plugin plugin;
    private GameHandler gameHandler;
    private BalanceManager balanceManager;
    private PointManager pointManager;
    private TeamManager teamManager;
    private FreezeManager freezeManager;
    private ScoreboardHandler scoreboardHandler;
    private ItemManager itemManager;
    private DTRManager dtrManager;
    private ChatManager chatManager;
    private CooldownManager cooldownManager;
    private WorldEditPlugin worldEdit;
    public static final Random RANDOM;
    public WorldManager wm;
    public ConfigManager config;
	private TeamJoinManager teamjoinManager;
    
    static {
        RANDOM = new Random();
    }
    
    public static Bunkers getInstance() {
        if (Bunkers.instance == null) {
            Bunkers.instance = new Bunkers();
        }
        return Bunkers.instance;
    }
    
    public FreezeManager getFreezeManager() {
        if (this.freezeManager == null) {
            this.freezeManager = new FreezeManager();
        }
        return this.freezeManager;
    }
    
    public Bunkers() {
        this.wm = new WorldManager();
        this.config = new ConfigManager("WorldRollback", "config");
    }
    
    public BalanceManager getBalanceManager() {
        if (this.balanceManager == null) {
            this.balanceManager = new BalanceManager();
        }
        return this.balanceManager;
    }
    
    public DTRManager getDTRManager() {
        if (this.dtrManager == null) {
            this.dtrManager = new DTRManager();
        }
        return this.dtrManager;
    }
    
    public ChatManager getChatManager() {
        if (this.chatManager == null) {
            this.chatManager = new ChatManager();
        }
        return this.chatManager;
    }
    
    public ItemManager getItemManager() {
        if (this.itemManager == null) {
            this.itemManager = new ItemManager();
        }
        return this.itemManager;
    }
    
    public PointManager getPointManager() {
        if (this.pointManager == null) {
            this.pointManager = new PointManager();
        }
        return this.pointManager;
    }
    
    public ScoreboardHandler getScoreboardHandler() {
        if (this.scoreboardHandler == null) {
            this.scoreboardHandler = new ScoreboardHandler(getInstance());
        }
        return this.scoreboardHandler;
    }
    
    public GameHandler getGameHandler() {
        if (this.gameHandler == null) {
            this.gameHandler = new GameHandler();
        }
        return this.gameHandler;
    }
    
    public TeamManager getTeamManager() {
        if (this.teamManager == null) {
            this.teamManager = new TeamManager();
        }
        return this.teamManager;
    }
    
    public CooldownManager getCooldownManager() {
        if (this.cooldownManager == null) {
            this.cooldownManager = new CooldownManager();
        }
        return this.cooldownManager;
    }
    
    public void onEnable() {
        Bunkers.instance = this;
		new TablistManager(this, new TabEvent(), TimeUnit.SECONDS.toMillis(5l));
        this.gameHandler = new GameHandler();
        this.balanceManager = new BalanceManager();
        this.itemManager = new ItemManager();
        this.pointManager = new PointManager();
        this.chatManager = new ChatManager();
        this.cooldownManager = new CooldownManager();
        this.freezeManager = new FreezeManager();
        this.scoreboardHandler = new ScoreboardHandler(getInstance());
        this.teamManager = new TeamManager();
        this.teamjoinManager = new TeamJoinManager();
        this.dtrManager = new DTRManager();
        final Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) {
            this.worldEdit = (WorldEditPlugin)p;
        }
        ConfigurationService.init();
        this.registerEvents();
        this.registerCommands();
        Bukkit.getScheduler().runTaskTimer((Plugin)getInstance(), (Runnable)this.getGameHandler(), 20L, 20L);
        Bukkit.broadcastMessage("§cThis server is running Bunkers by Kirai");
        Bunkers.plugin = (Plugin)this;
        if (this.config.getFile() == null) {
            this.config.createFile();
            this.config.saveFile();
        }
        if (this.config.getFile().getString("world") == null) {
            this.config.getFile().set("world", (Object)"world");
            this.config.getFile().set("map", (Object)"_map");
            this.config.saveFile();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Bunkers] " + ChatColor.YELLOW + "The world has been refreshed for the next game!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Bunkers] " + ChatColor.YELLOW + "This network is running p-1-29-Version-BETA");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Bunkers] " + ChatColor.YELLOW + "This plugin is only licensed to kirai.us");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Bunkers] " + ChatColor.YELLOW + "This plugin was coded by Memelord, Sexest");
    }
    
    
    public void onDisable() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("§aServer reloading, please try again later.");
            final World delete = Bukkit.getWorld(this.config.getFile().getString("world"));
            final File deleteFolder = delete.getWorldFolder();
            this.wm.deleteWorld(deleteFolder);
            Bukkit.getServer().createWorld(new WorldCreator(this.config.getFile().getString("map")));
            final World source = Bukkit.getWorld(this.config.getFile().getString("map"));
            final File sourceFolder = source.getWorldFolder();
            final World target = Bukkit.getWorld(this.config.getFile().getString("world"));
            final File targetFolder = target.getWorldFolder();
            this.wm.copyWorld(sourceFolder, targetFolder);
        }
    }
    
    public WorldEditPlugin getWorldEdit() {
        return this.worldEdit;
    }
    
    private void registerEvents() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)this.getGameHandler(), (Plugin)getInstance());
        pm.registerEvents((Listener)new ShopManager(), (Plugin)getInstance());
        pm.registerEvents((Listener)this.scoreboardHandler, (Plugin)getInstance());
		new TablistManager(this, new TabEvent(), TimeUnit.SECONDS.toMillis(5l));
        pm.registerEvents((Listener)this.freezeManager, (Plugin)getInstance());
        pm.registerEvents((Listener)this.cooldownManager, (Plugin)getInstance());
        pm.registerEvents((Listener)this.chatManager, (Plugin)getInstance());
        pm.registerEvents((Listener)this.teamjoinManager, (Plugin)getInstance());
    }
    
    private void registerCommands() {
        this.getCommand("setlobbyspawn").setExecutor((CommandExecutor)new LobbySpawn());
        this.getCommand("removetab").setExecutor((CommandExecutor)new RemoveTab());
        this.getCommand("team").setExecutor((CommandExecutor)new TeamCommand());
        this.getCommand("forcestart").setExecutor((CommandExecutor)new ForceStart());
    }
}
