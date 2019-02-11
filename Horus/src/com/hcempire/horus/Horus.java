package com.hcempire.horus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.alexandeh.ekko.files.ConfigFile;
import com.bizarrealex.aether.Aether;
import com.bizarrealex.aether.AetherOptions;
import com.hcempire.horus.blockoperation.BlockOperationModifier;
import com.hcempire.horus.blockoperation.BlockOperationModifierListeners;
import com.hcempire.horus.claimwall.ClaimWallListeners;
import com.hcempire.horus.combatlogger.CombatLogger;
import com.hcempire.horus.combatlogger.CombatLoggerListeners;
import com.hcempire.horus.combatlogger.commands.CombatLoggerCommand;
import com.hcempire.horus.crate.Crate;
import com.hcempire.horus.crate.CrateListeners;
import com.hcempire.horus.crate.command.CrateCommand;
import com.hcempire.horus.crowbar.CrowbarListeners;
import com.hcempire.horus.deathlookup.DeathLookupCommand;
import com.hcempire.horus.deathlookup.DeathLookupListeners;
import com.hcempire.horus.deathsign.DeathSignListeners;
import com.hcempire.horus.economysign.EconomySignListeners;
import com.hcempire.horus.elevator.ElevatorListeners;
import com.hcempire.horus.enchantmentlimiter.EnchantmentLimiterListeners;
import com.hcempire.horus.event.Event;
import com.hcempire.horus.event.EventManager;
import com.hcempire.horus.event.koth.KothEvent;
import com.hcempire.horus.event.koth.KothEventListeners;
import com.hcempire.horus.event.koth.command.KothStartCommand;
import com.hcempire.horus.event.koth.command.KothStopCommand;
import com.hcempire.horus.event.koth.procedure.KothCreateProcedureListeners;
import com.hcempire.horus.event.koth.procedure.command.KothCreateProcedureCommand;
import com.hcempire.horus.inventory.command.CloneInventoryCommand;
import com.hcempire.horus.inventory.command.GiveInventoryCommand;
import com.hcempire.horus.inventory.command.LastInventoryCommand;
import com.hcempire.horus.itemdye.ItemDye;
import com.hcempire.horus.itemdye.ItemDyeListeners;
import com.hcempire.horus.listerners.eotw.EotwCommand;
import com.hcempire.horus.listerners.ffa.FreeForAllCommand;
import com.hcempire.horus.listerners.focus.FocusCommand;
import com.hcempire.horus.listerners.reload.ReloadCommand;
import com.hcempire.horus.listerners.sotw.SotwCommand;
import com.hcempire.horus.listerners.sotw.SotwHandler;
import com.hcempire.horus.listerners.sotw.SotwListener;
import com.hcempire.horus.listerners.staffmode.StaffModeListener;
import com.hcempire.horus.misc.commands.HelpCommand;
import com.hcempire.horus.misc.commands.MapKitCommand;
import com.hcempire.horus.misc.commands.SpawnCommand;
import com.hcempire.horus.misc.listeners.GlitchListeners;
import com.hcempire.horus.mobstack.MobStackListeners;
import com.hcempire.horus.potionlimiter.PotionLimiterListeners;
import com.hcempire.horus.profile.Profile;
import com.hcempire.horus.profile.ProfileListeners;
import com.hcempire.horus.profile.cooldown.ProfileCooldownListeners;
import com.hcempire.horus.profile.deathban.ProfileDeathbanListeners;
import com.hcempire.horus.profile.kit.command.ProfileKitCommand;
import com.hcempire.horus.profile.options.command.ProfileOptionsCommand;
import com.hcempire.horus.profile.ore.ProfileOreCommand;
import com.hcempire.horus.profile.protection.ProfileProtection;
import com.hcempire.horus.profile.protection.command.ProfileProtectionCommand;
import com.hcempire.horus.statracker.StatTrackerListeners;
import com.hcempire.horus.subclaim.SubclaimListeners;
import com.hcempire.horus.util.HorusBoardAdapter;
import com.hcempire.horus.util.database.HorusDatabase;

import lombok.Getter;

public class Horus extends JavaPlugin {

    private static Horus instance;
    @Getter
    private HorusDatabase horusDatabase;
    @Getter
    private ConfigFile configFile, scoreboardFile, langFile;
    private SotwHandler sotwTimer;
    @Getter private StaffModeListener staffModeListener;

    public void onEnable() {
        instance = this;
        configFile = new ConfigFile(this, "config");
        langFile = new ConfigFile(this, "lang");
        scoreboardFile = new ConfigFile(this, "scoreboard");
        horusDatabase = new HorusDatabase(this);

        new Aether(this, new HorusBoardAdapter(this), new AetherOptions().hook(true));

        for (Player player : Bukkit.getOnlinePlayers()) {
            new Profile(player.getUniqueId());
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == CombatLogger.ENTITY_TYPE) {
                    if (entity instanceof LivingEntity) {
                        if (((LivingEntity) entity).getCustomName() != null) {
                            entity.remove();
                        }
                    }
                }
            }
        }

        // MobStack.stack();
        KothEvent.load();
        Crate.load();
        BlockOperationModifier.run();
        ProfileProtection.run(this);
        registerRecipes();
        registerListeners();
        registerCommands();

       //if (configFile.getBoolean("ENCHANTS.RUNNABLE.ENABLED") == true) {
      //    new BukkitRunnable() {
     //          @Override
       //        public void run() {
        // for (Player player : Bukkit.getOnlinePlayers()) {
                   //    if (configFile.getBoolean("ENCHANTS.SPEED") == true) {
                     //      if (player.getInventory().getBoots() != null) {
                 //              if (player.getInventory().getBoots().getItemMeta().getLore().contains(Color.translate("&cSpeed II"))) {
             //                      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999 * 20, 1));
               //                }
           //                } else {
      ///                         player.removePotionEffect(PotionEffectType.SPEED);
         //                  }
    //                   }
    //                   if (configFile.getBoolean("ENCHANTS.FIRE") == true) {
   //                        if (player.getInventory().getChestplate() != null) {
   //                            if (player.getInventory().getChestplate().getItemMeta().getLore().contains(Color.translate("&cFire Resistance"))) {
   //                                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999 * 20, 1));
   //                            }
   //                        } else {
   //                            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
   //                        }
   //                    }
   //                }
   //            }
       //    }.runTaskTimerAsynchronously(this, 20L, 2L);
   //    }
    }

    public void onDisable() {
        for (Profile profile : Profile.getProfiles()) {
            profile.save();
        }

        for (CombatLogger logger : CombatLogger.getLoggers()) {
            logger.getEntity().remove();
        }

        for (Event event : EventManager.getInstance().getEvents()) {
            if (event instanceof KothEvent) {
                ((KothEvent) event).save();
            }
        }

        for (Crate crate : Crate.getCrates()) {
            crate.save();
        }

      //  MobStack.unStack();

        horusDatabase.getClient().close();
    }

    private void registerRecipes() {
        for (Material material : Material.values()) {
            if (material.name().contains("CHESTPLATE") || material.name().contains("SWORD") || material.name().contains("LEGGINGS") || material.name().contains("BOOTS") || material.name().contains("HELMET") || material.name().contains("AXE") || material.name().contains("SPADE")) {
                for (ItemDye dye : ItemDye.values()) {
                    Bukkit.addRecipe(ItemDye.getRecipe(material, dye));
                }
            }
        }

        Bukkit.addRecipe(new ShapelessRecipe(new ItemStack(Material.EXP_BOTTLE)).addIngredient(1, Material.GLASS_BOTTLE));
    }

    private void registerCommands() {
        new ProfileProtectionCommand();
        new ProfileOreCommand();
        new CloneInventoryCommand();
        new LastInventoryCommand();
        new GiveInventoryCommand();
        new DeathLookupCommand();
        new ProfileKitCommand();
        new CombatLoggerCommand();
        new KothCreateProcedureCommand();
        new KothStartCommand();
        new KothStopCommand();
        new HelpCommand();
        new SpawnCommand();
        new ProfileOptionsCommand();
        new CrateCommand();
        new MapKitCommand();

        Bukkit.getPluginCommand("horus").setExecutor(new ReloadCommand());
        Bukkit.getPluginCommand("eotw").setExecutor(new EotwCommand());
        Bukkit.getPluginCommand("sotw").setExecutor(new SotwCommand(this));
        Bukkit.getPluginCommand("ffa").setExecutor(new FreeForAllCommand());
        Bukkit.getPluginCommand("focus").setExecutor(new FocusCommand());
      //  Bukkit.getPluginCommand("staffmode").setExecutor(new StaffModeCommand());
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ProfileListeners(this), this);
        pluginManager.registerEvents(new MobStackListeners(), this);
        pluginManager.registerEvents(new CrowbarListeners(), this);
        pluginManager.registerEvents(new EconomySignListeners(), this);
        pluginManager.registerEvents(new DeathSignListeners(), this);
        pluginManager.registerEvents(new StatTrackerListeners(), this);
        pluginManager.registerEvents(new ProfileCooldownListeners(), this);
        pluginManager.registerEvents(new ClaimWallListeners(this), this);
        pluginManager.registerEvents(new EnchantmentLimiterListeners(), this);
        pluginManager.registerEvents(new PotionLimiterListeners(), this);
        pluginManager.registerEvents(new DeathLookupListeners(), this);
        pluginManager.registerEvents(new CombatLoggerListeners(this), this);
        pluginManager.registerEvents(new BlockOperationModifierListeners(), this);
        pluginManager.registerEvents(new KothCreateProcedureListeners(), this);
        pluginManager.registerEvents(new KothEventListeners(), this);
        pluginManager.registerEvents(new ElevatorListeners(), this);
        pluginManager.registerEvents(new SubclaimListeners(), this);
        pluginManager.registerEvents(new CrateListeners(), this);
        pluginManager.registerEvents(new ItemDyeListeners(), this);
        pluginManager.registerEvents(new GlitchListeners(), this);
        pluginManager.registerEvents(new ProfileDeathbanListeners(), this);

        pluginManager.registerEvents(new EotwCommand(), this);
        pluginManager.registerEvents(new SotwListener(this), this);
        this.sotwTimer = new SotwHandler();
        staffModeListener = new StaffModeListener();
        Bukkit.getServer().getPluginManager().registerEvents(staffModeListener, this);
    }
    
    public static Horus getInstance() {
        return instance;
    }

    public SotwHandler getSotwTimer() { return this.sotwTimer; }
}
