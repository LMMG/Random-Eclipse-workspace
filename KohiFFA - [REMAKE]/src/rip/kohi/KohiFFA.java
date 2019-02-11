package rip.kohi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bizarrealex.aether.Aether;

import rip.kohi.commands.StaffModeCommand;
import rip.kohi.commands.StatsCommand;
import rip.kohi.commands.VanishCommand;
import rip.kohi.events.StaffModeEvent;
import rip.kohi.events.VanishEvent;
import rip.kohi.handler.ServerHandler;
import rip.kohi.listener.BeaconListener;
import rip.kohi.listener.KitPotionsListener;
import rip.kohi.profile.Profile;
import rip.kohi.profile.listener.ProfileListener;
import rip.kohi.scoreboard.KohiFFAProvider;
import rip.kohi.utils.IconMenuUtil;

public class KohiFFA extends JavaPlugin implements Listener {

    public static FileConfiguration config;
    public static File conf;
    public static FileConfiguration kitsSave;
    public static File kitSaveCOnf;
    public static final String PREFIX;
    static boolean canWork;
    private Aether aether;
    public static KohiFFA instance;

    static {
        canWork = true;
        PREFIX = ChatColor.translateAlternateColorCodes('&', "&6&lKohiFFA >");
    }

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "===================================================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Name: KohiFFA - [REMAKE]");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Version: 0.1-STABLE");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Author: Evocative");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "===================================================");

        this.kitSaveCOnf = new File("anvil.yml");
        this.kitsSave = YamlConfiguration.loadConfiguration(this.kitSaveCOnf);
        Bukkit.getPluginManager().registerEvents(this, this);

        config = this.getConfig();
        config.options().copyDefaults(true);
        conf = new File(this.getDataFolder(), "config.yml");
        this.saveConfig();
        this.saveDefaultConfig();

        aether = new Aether(this, new KohiFFAProvider());

        //Commands
        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("serverhandler").setExecutor(new ServerHandler());
        // Listeners
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new BeaconListener(), this);
        manager.registerEvents(new ProfileListener(), this);
        manager.registerEvents(new Profile(), this);
        manager.registerEvents(new KitPotionsListener(), this);
        manager.registerEvents(new StaffModeEvent(), this);
        manager.registerEvents(new VanishEvent(), this);
        manager.registerEvents(new KohiFFAProvider(), this);
        manager.registerEvents(new ServerHandler(), this);
    }

    @Override
    public void onDisable() {
        instance = null; // for the skids
        save();
    }

    public void save() {
        try {
            this.config.save(this.kitSaveCOnf);
        } catch (IOException e) {
            System.out.println("[KohiFFA] Error whileist saving kit");
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            final Block b = e.getClickedBlock();
            final Player p = e.getPlayer();
            if (b.getType().equals(Material.ANVIL)) {
                e.setCancelled(true);
                this.openKitMenu(p);
            }
        }
    }

    public static KohiFFA getInstance() {
        return instance;
    }

    public boolean isSword(final ItemStack item) {
        return item.getType().equals((Object) Material.WOOD_SWORD) || item.getType().equals((Object) Material.STONE_SWORD) || item.getType().equals((Object) Material.IRON_SWORD) || item.getType().equals((Object) Material.DIAMOND_SWORD) || item.getType().equals((Object) Material.GOLD_SWORD);
    }

    public void openKitMenu(final Player p1) {
        final IconMenuUtil kitMenu = new IconMenuUtil(ChatColor.GREEN + p1.getName() + ChatColor.RESET + "'s Kits", 45, new IconMenuUtil.OptionClickEventHandler() {
            @Override
            public void onOptionClick(final IconMenuUtil.OptionClickEvent event) {
                if (!KohiFFA.work()) {
                    return;
                }
                final Player p = event.getPlayer();
                final String name = ChatColor.stripColor(event.getName());
                if (name.contains("Save")) {
                    final int number = KohiFFA.this.getNumber(name);
                    KohiFFA.this.saveInventory(number, p);
                    p.sendMessage(ChatColor.GREEN + "Kit " + ChatColor.GOLD + number + ChatColor.GREEN + " saved!");
                    return;
                }
                if (name.contains("Delete")) {
                    final int number = KohiFFA.this.getNumber(name);
                    KohiFFA.this.delete(p, number);
                    p.sendMessage(ChatColor.RED + "Kit " + ChatColor.GOLD + number + ChatColor.RED + " deleted!");
                    return;
                }
                if (name.contains("Load")) {
                    final int number = KohiFFA.this.getNumber(name);
                    if (KohiFFA.this.hasKit(p, number)) {
                        p.getInventory().setContents(KohiFFA.this.fix(KohiFFA.this.getContents(number, p)));
                        p.getInventory().setArmorContents(KohiFFA.this.fix(KohiFFA.this.getArmor(number, p)));
                        p.updateInventory();
                        p.sendMessage(ChatColor.BLUE + "Kit " + ChatColor.GOLD + number + ChatColor.BLUE + " loaded!");
                    } else {
                        System.out.println("UN-LOADED");
                        p.sendMessage(ChatColor.RED + "Kit " + ChatColor.GOLD + number + ChatColor.RED + " not found!");
                    }
                    return;
                }
                p.playSound(p.getLocation(), Sound.CLICK, 15.0f, 15.0f);
            }
        }, (Plugin) this).setOption(0, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 1, new String[0]).setOption(1, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 2, new String[0]).setOption(2, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 3, new String[0]).setOption(3, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 4, new String[0]).setOption(4, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 5, new String[0]).setOption(5, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 6, new String[0]).setOption(6, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 7, new String[0]).setOption(7, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 8, new String[0]).setOption(8, this.getSaveButton(1), ChatColor.GREEN + "Save inventory " + ChatColor.GOLD + 9, new String[0]).setOption(9, this.getChestplate(1, p1)).setOption(10, this.getChestplate(2, p1)).setOption(11, this.getChestplate(3, p1)).setOption(12, this.getChestplate(4, p1)).setOption(13, this.getChestplate(5, p1)).setOption(14, this.getChestplate(6, p1)).setOption(15, this.getChestplate(7, p1)).setOption(16, this.getChestplate(8, p1)).setOption(17, this.getChestplate(9, p1)).setOption(18, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 1, new String[0]).setOption(19, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 2, new String[0]).setOption(20, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 3, new String[0]).setOption(21, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 4, new String[0]).setOption(22, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 5, new String[0]).setOption(23, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 6, new String[0]).setOption(24, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 7, new String[0]).setOption(25, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 8, new String[0]).setOption(26, this.getLoadButton(1), ChatColor.BLUE + "Load inventory " + ChatColor.GOLD + 9, new String[0]).setOption(27, this.getSword(1, p1)).setOption(28, this.getSword(2, p1)).setOption(29, this.getSword(3, p1)).setOption(30, this.getSword(4, p1)).setOption(31, this.getSword(5, p1)).setOption(32, this.getSword(6, p1)).setOption(33, this.getSword(7, p1)).setOption(34, this.getSword(8, p1)).setOption(35, this.getSword(9, p1)).setOption(36, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 1, new String[0]).setOption(37, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 2, new String[0]).setOption(38, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 3, new String[0]).setOption(39, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 4, new String[0]).setOption(40, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 5, new String[0]).setOption(41, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 6, new String[0]).setOption(42, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 7, new String[0]).setOption(43, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 8, new String[0]).setOption(44, this.getRedButton(1), ChatColor.RED + "Delete inventory " + ChatColor.GOLD + 9, new String[0]);
        kitMenu.open(p1);
    }

    public ItemStack getSaveButton(final int value) {
        final ItemStack item = new ItemStack(Material.getMaterial(160), 1, (short) 5);
        return item;
    }

    public ItemStack getChestplate(final int value, final Player p) {
        ItemStack item = null;
        if (this.hasKit(p, value)) {
            item = new ItemStack(Material.DIAMOND_CHESTPLATE);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Diamond Class");
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.getMaterial(102), 1);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Empty Kit");
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack getSword(final int value, final Player p) {
        ItemStack item = null;
        if (this.hasKit(p, value)) {
            item = new ItemStack(Material.DIAMOND_SWORD);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Diamond Class");
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.getMaterial(102), 1);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Empty Kit");
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack getLoadButton(final int value) {
        final ItemStack item = new ItemStack(Material.getMaterial(160), 1, (short) 11);
        return item;
    }

    public boolean hasKit(final Player p, final int value) {
        return this.config.getList(String.valueOf(p.getName()) + ".class." + value + ".inventory") != null;
    }

    public ItemStack getRedButton(final int value) {
        final ItemStack item = new ItemStack(Material.getMaterial(160), 1, (short) 14);
        return item;
    }

    public Integer getNumber(final String s) {
        return Integer.parseInt(s.split(" ")[2]);
    }

    public void saveInventory(final int section, final Player p) {
        final ItemStack[] contents = p.getInventory().getContents();
        final ItemStack[] armor = p.getInventory().getArmorContents();
        this.config.set(String.valueOf(p.getName()) + ".class." + section + ".inventory", (Object) this.toList(contents));
        this.config.set(String.valueOf(p.getName()) + ".class." + section + ".armor", (Object) this.toList(armor));
        this.save();
    }

    @SuppressWarnings("unchecked")
    public ItemStack[] getArmor(final int section, final Player p) {
        return this.toArray((List<ItemStack>) this.config.getList(String.valueOf(p.getName()) + ".class." + section + ".armor"));
    }

    @SuppressWarnings("unchecked")
    public ItemStack[] getContents(final int section, final Player p) {
        return this.toArray((List<ItemStack>) this.config.getList(String.valueOf(p.getName()) + ".class." + section + ".inventory"));
    }

    public void delete(final Player p, final int number) {
        this.config.set(String.valueOf(p.getName()) + ".class." + number + ".inventory", (Object) null);
        this.config.set(String.valueOf(p.getName()) + ".class." + number + ".armor", (Object) null);
        this.saveConfig();
    }

    public List<ItemStack> toList(final ItemStack[] stack) {
        final List<ItemStack> list = new ArrayList<ItemStack>();
        for (final ItemStack item : stack) {
            list.add(item);
        }
        return list;
    }

    public ItemStack[] toArray(final List<ItemStack> stack) {
        final ItemStack[] list = new ItemStack[stack.toArray().length];
        int i = 0;
        for (final ItemStack item : stack) {
            list[i] = item;
            ++i;
        }
        return list;
    }

    public ItemStack[] fix(final ItemStack[] stack) {
        final ItemStack[] list = new ItemStack[stack.length];
        int i = 0;
        for (final ItemStack item : stack) {
            if (item != null && item.getAmount() == 0) {
                item.setAmount(1);
            }
            list[i] = item;
            ++i;
        }
        return list;
    }

    public static boolean work() {
        if (KohiFFA.canWork) {
            KohiFFA.canWork = false;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("KohiFFA"), (Runnable) new Runnable() {
                @Override
                public void run() {
                    KohiFFA.canWork = true;
                }
            }, 5L);
            return true;
        }
        return false;
    }
}