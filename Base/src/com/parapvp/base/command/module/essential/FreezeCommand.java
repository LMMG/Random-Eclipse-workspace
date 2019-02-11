package com.parapvp.base.command.module.essential;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.event.PlayerFreezeEvent;
import com.parapvp.util.BukkitUtils;
import com.parapvp.util.chat.ClickAction;
import com.parapvp.util.chat.Text;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class FreezeCommand
extends BaseCommand
implements Listener {
    private static final String FREEZE_BYPASS = "base.freeze.bypass";
    private final TObjectLongMap<UUID> frozenPlayers = new TObjectLongHashMap();
    private long defaultFreezeDuration;
    private long serverFrozenMillis;
    private HashSet<String> frozen = new HashSet();

    public FreezeCommand(BasePlugin plugin) {
        super("freeze", "Freezes a player from moving");
        this.setUsage("/(command) <all|playerName>");
        this.setAliases(new String[]{"ss"});
        this.defaultFreezeDuration = TimeUnit.MINUTES.toMillis(60);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        String reason = null;
        Long freezeTicks = this.defaultFreezeDuration;
        long millis = System.currentTimeMillis();
        if (args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")) {
            long oldTicks = this.getRemainingServerFrozenMillis();
            if (oldTicks > 0) {
                freezeTicks = (long) 0;
            }
            this.serverFrozenMillis = millis + this.defaultFreezeDuration;
            Bukkit.getServer().broadcastMessage((Object)ChatColor.YELLOW + "The server is " + (freezeTicks > 0 ? new StringBuilder().append("now frozen for ").append(DurationFormatUtils.formatDurationWords((long)freezeTicks, (boolean)true, (boolean)true)).toString() : "no longer frozen") + (reason == null ? "" : new StringBuilder().append(" with reason ").append(reason).toString()) + '.');
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage((Object)ChatColor.GOLD + "Player '" + (Object)ChatColor.WHITE + args[0] + (Object)ChatColor.GOLD + "' not found.");
            return true;
        }
        if (target.equals((Object)sender)) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot freeze yourself.");
            return true;
        }
        UUID targetUUID = target.getUniqueId();
        boolean shouldFreeze = this.getRemainingPlayerFrozenMillis(targetUUID) > 0;
        PlayerFreezeEvent playerFreezeEvent = new PlayerFreezeEvent(target, shouldFreeze);
        Bukkit.getServer().getPluginManager().callEvent((Event)playerFreezeEvent);
        if (playerFreezeEvent.isCancelled()) {
            sender.sendMessage((Object)ChatColor.RED + "Unable to freeze " + target.getName() + '.');
            return false;
        }
        if (shouldFreeze) {
            this.frozen.remove(target.getName());
            this.frozenPlayers.remove((Object)targetUUID);
            target.sendMessage((Object)ChatColor.GREEN + "You have been un-frozen.");
            Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + target.getName() + " is no longer frozen."));
        } else {
            this.frozen.add(target.getName());
            this.frozenPlayers.put(targetUUID, millis + freezeTicks);
            String timeString = DurationFormatUtils.formatDurationWords((long)freezeTicks, (boolean)true, (boolean)true);
            this.Message(target.getName());
            Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + target.getName() + " is now frozen for " + timeString + '.'));
        }
        return true;
    }

    private void Message(final String name) {
        HashMap timer = new HashMap();
        final Player p = Bukkit.getPlayer((String)name);
        BukkitTask task = new BukkitRunnable(){

            public void run() {
                if (FreezeCommand.this.frozen.contains(name)) {
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + "\u2588\u2588\u2588\u2588");
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.GOLD + (Object)ChatColor.BLACK + "\u2588" + (Object)ChatColor.GOLD + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + "\u2588\u2588\u2588");
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.GOLD + "\u2588" + (Object)ChatColor.BLACK + "\u2588" + (Object)ChatColor.GOLD + "\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + "\u2588\u2588");
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.GOLD + "\u2588" + (Object)ChatColor.BLACK + "\u2588" + (Object)ChatColor.GOLD + "\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + "\u2588\u2588 " + (Object)ChatColor.YELLOW + "You have been frozen by a staff member.");
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.GOLD + "\u2588" + (Object)ChatColor.BLACK + "\u2588" + (Object)ChatColor.GOLD + "\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + "\u2588\u2588 " + (Object)ChatColor.YELLOW + "If you disconnect you will be " + (Object)ChatColor.DARK_RED + (Object)ChatColor.BOLD + "BANNED" + (Object)ChatColor.YELLOW + '.');
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.GOLD + "\u2588\u2588\u2588" + (Object)ChatColor.BLACK + (Object)ChatColor.GOLD + "\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + "\u2588 " + (Object)ChatColor.YELLOW + "Please connect to our Teamspeak" + (Object)ChatColor.YELLOW + '.');
                    new Text((Object)ChatColor.RED + "\u2588" + (Object)ChatColor.GOLD + "\u2588\u2588\u2588" + (Object)ChatColor.BLACK + "\u2588" + (Object)ChatColor.GOLD + "\u2588\u2588\u2588" + (Object)ChatColor.RED + "\u2588" + (Object)ChatColor.WHITE + (Object)ChatColor.GRAY + " (teamspeakherecunt) " + (Object)ChatColor.ITALIC + "Click me to download" + (Object)ChatColor.GRAY + '.').setClick(ClickAction.OPEN_URL, "http://www.teamspeak.com/downloads").send((CommandSender)p);
                    p.sendMessage((Object)ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
                    p.sendMessage((Object)ChatColor.WHITE + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously((Plugin)BasePlugin.getPlugin(), 0, 200);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacker = BukkitUtils.getFinalAttacker((EntityDamageEvent)event, false);
            if (attacker == null) {
                return;
            }
            Player player = (Player)entity;
            if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission("base.freeze.bypass"))) {
                attacker.sendMessage((Object)ChatColor.RED + player.getName() + " is currently frozen, you may not attack.");
                event.setCancelled(true);
                return;
            }
            if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(attacker.getUniqueId()) <= 0 || attacker.hasPermission("base.freeze.bypass"))) {
                event.setCancelled(true);
                attacker.sendMessage((Object)ChatColor.RED + "You may not attack players whilst frozen.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPreCommandProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission("base.freeze.bypass"))) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You may not use commands whilst frozen.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        Player player = event.getPlayer();
        if (!(this.getRemainingServerFrozenMillis() <= 0 && this.getRemainingPlayerFrozenMillis(player.getUniqueId()) <= 0 || player.hasPermission("base.freeze.bypass"))) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (this.frozen.contains(e.getPlayer().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("base.command.freeze")) continue;
                new Text((Object)ChatColor.YELLOW + "" + (Object)ChatColor.BOLD + e.getPlayer().getName() + " has " + (Object)ChatColor.DARK_RED + "QUIT" + (Object)ChatColor.YELLOW + (Object)ChatColor.BOLD + " while frozen." + (Object)ChatColor.GRAY + (Object)ChatColor.ITALIC + "(Click here to ban)").setHoverText((Object)ChatColor.YELLOW + "BAN" + (Object)ChatColor.GRAY + e.getPlayer().getName()).setClick(ClickAction.RUN_COMMAND, "/ban " + e.getPlayer().getName() + " Logged out whilst frozen");
                return;
            }
        }
    }

    public long getRemainingServerFrozenMillis() {
        return this.serverFrozenMillis - System.currentTimeMillis();
    }

    public long getRemainingPlayerFrozenMillis(UUID uuid) {
        long remaining = this.frozenPlayers.get((Object)uuid);
        if (remaining == this.frozenPlayers.getNoEntryValue()) {
            return 0;
        }
        return remaining - System.currentTimeMillis();
    }

}

