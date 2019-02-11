package org.ipvp.hcf.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.ipvp.hcf.ConfigurationService;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.eventgame.faction.KothFaction;
import org.ipvp.hcf.faction.event.CaptureZoneEnterEvent;
import org.ipvp.hcf.faction.event.CaptureZoneLeaveEvent;
import org.ipvp.hcf.faction.event.FactionCreateEvent;
import org.ipvp.hcf.faction.event.FactionRemoveEvent;
import org.ipvp.hcf.faction.event.FactionRenameEvent;
import org.ipvp.hcf.faction.event.PlayerClaimEnterEvent;
import org.ipvp.hcf.faction.event.PlayerJoinFactionEvent;
import org.ipvp.hcf.faction.event.PlayerLeaveFactionEvent;
import org.ipvp.hcf.faction.event.PlayerLeftFactionEvent;
import org.ipvp.hcf.faction.struct.Raidable;
import org.ipvp.hcf.faction.struct.RegenStatus;
import org.ipvp.hcf.faction.struct.Relation;
import org.ipvp.hcf.faction.type.ClaimableFaction;
import org.ipvp.hcf.faction.type.Faction;
import org.ipvp.hcf.faction.type.PlayerFaction;

import com.doctordark.util.BukkitUtils;
import com.google.common.base.Optional;

public class FactionListener implements Listener
{
    private static final long FACTION_JOIN_WAIT_MILLIS;
    private static final String FACTION_JOIN_WAIT_WORDS;
    private static final String LAND_CHANGED_META_KEY = "landChangedMessage";
    private static final long LAND_CHANGE_MSG_THRESHOLD = 225L;
    private final HCF plugin;
    
    static {
        FACTION_JOIN_WAIT_MILLIS = TimeUnit.SECONDS.toMillis(30L);
        FACTION_JOIN_WAIT_WORDS = DurationFormatUtils.formatDurationWords(FactionListener.FACTION_JOIN_WAIT_MILLIS, true, true);
    }
    
    public FactionListener(final HCF plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRenameMonitor(final FactionRenameEvent event) {
        final Faction faction = event.getFaction();
        if (faction instanceof KothFaction) {
            ((KothFaction)faction).getCaptureZone().setName(event.getNewName());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionCreate(final FactionCreateEvent event) {
        final Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            final CommandSender sender = event.getSender();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final String msg = ChatColor.YELLOW + "The faction " + ChatColor.WHITE + ((player == null) ? faction.getName() : faction.getDisplayName((CommandSender)player)) + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "created" + ChatColor.YELLOW + " by " + ChatColor.WHITE + ((sender instanceof Player) ? ((Player)sender).getDisplayName() : sender.getName()) + ChatColor.YELLOW + '.';
                player.sendMessage(msg);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRemove(final FactionRemoveEvent event) {
        final Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            final CommandSender sender = event.getSender();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final String msg = ChatColor.YELLOW + "The faction " + ChatColor.WHITE + ((player == null) ? faction.getName() : faction.getDisplayName((CommandSender)player)) + ChatColor.YELLOW + " has been " + ChatColor.RED + "disbanded" + ChatColor.YELLOW + " by " + ChatColor.WHITE + ((sender instanceof Player) ? ((Player)sender).getDisplayName() : sender.getName()) + ChatColor.YELLOW + '.';
                player.sendMessage(msg);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRename(final FactionRenameEvent event) {
        final Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final Relation relation = faction.getRelation((CommandSender)player);
                final String msg = ChatColor.YELLOW + "The faction " + relation.toChatColour() + event.getOriginalName() + ChatColor.YELLOW + " has been " + ChatColor.AQUA + "renamed" + ChatColor.YELLOW + " to " + relation.toChatColour() + event.getNewName() + ChatColor.YELLOW + '.';
                player.sendMessage(msg);
            }
        }
    }
    
    private long getLastLandChangedMeta(final Player player) {
        final List<MetadataValue> value = (List<MetadataValue>)player.getMetadata("landChangedMessage");
        final long millis = System.currentTimeMillis();
        final long remaining = (value == null || value.isEmpty()) ? 0L : (value.get(0).asLong() - millis);
        if (remaining <= 0L) {
            player.setMetadata("landChangedMessage", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)(millis + 225L)));
        }
        return remaining;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCaptureZoneEnter(final CaptureZoneEnterEvent event) {
        final Player player = event.getPlayer();
        if (this.getLastLandChangedMeta(player) > 0L) {
            return;
        }
        if (this.plugin.getUserManager().getUser(player.getUniqueId()).isCapzoneEntryAlerts()) {
            player.sendMessage(ChatColor.YELLOW + "Now entering cap zone: " + event.getCaptureZone().getDisplayName() + ChatColor.YELLOW + '(' + event.getFaction().getName() + ChatColor.YELLOW + ')');
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCaptureZoneLeave(final CaptureZoneLeaveEvent event) {
        final Player player = event.getPlayer();
        if (this.getLastLandChangedMeta(player) > 0L) {
            return;
        }
        if (this.plugin.getUserManager().getUser(player.getUniqueId()).isCapzoneEntryAlerts()) {
            player.sendMessage(ChatColor.YELLOW + "Now leaving cap zone: " + event.getCaptureZone().getDisplayName() + ChatColor.YELLOW + '(' + event.getFaction().getName() + ChatColor.YELLOW + ')');
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onPlayerClaimEnter(final PlayerClaimEnterEvent event) {
        final Faction toFaction = event.getToFaction();
        if (toFaction.isSafezone()) {
            final Player player = event.getPlayer();
            player.setHealth(((CraftPlayer)player).getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setSaturation(4.0f);
        }
        final Player player = event.getPlayer();
        if (this.getLastLandChangedMeta(player) > 0L) {
            return;
        }
        final Faction fromFaction = event.getFromFaction();
        player.sendMessage(ChatColor.YELLOW + "Now leaving: " + fromFaction.getDisplayName((CommandSender)player) + " " + ChatColor.YELLOW + '(' + (fromFaction.isDeathban() ? (ChatColor.RED + "Deathban") : (ChatColor.GREEN + "Non-Deathban")) + ChatColor.YELLOW + ')');
        player.sendMessage(ChatColor.YELLOW + "Now entering: " + toFaction.getDisplayName((CommandSender)player) + " " + ChatColor.YELLOW + '(' + (toFaction.isDeathban() ? (ChatColor.RED + "Deathban") : (ChatColor.GREEN + "Non-Deathban")) + ChatColor.YELLOW + ')');
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeftFaction(final PlayerLeftFactionEvent event) {
        final Optional<Player> optionalPlayer = event.getPlayer();
        if (optionalPlayer.isPresent()) {
            this.plugin.getUserManager().getUser(((Player)optionalPlayer.get()).getUniqueId()).setLastFactionLeaveMillis(System.currentTimeMillis());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerPreFactionJoin(final PlayerJoinFactionEvent event) {
        final Faction faction = event.getFaction();
        final Optional<Player> optionalPlayer = event.getPlayer();
        if (faction instanceof PlayerFaction && optionalPlayer.isPresent()) {
            final Player player = (Player)optionalPlayer.get();
            final PlayerFaction playerFaction = (PlayerFaction)faction;
            if (!ConfigurationService.KIT_MAP && !this.plugin.getEotwHandler().isEndOfTheWorld() && playerFaction.getRegenStatus() == RegenStatus.PAUSED) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot join factions that are not regenerating DTR.");
                return;
            }
            final long difference = this.plugin.getUserManager().getUser(player.getUniqueId()).getLastFactionLeaveMillis() - System.currentTimeMillis() + FactionListener.FACTION_JOIN_WAIT_MILLIS;
            if (difference > 0L && !player.hasPermission("hcf.faction.argument.staff.forcejoin")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot join factions after just leaving within " + FactionListener.FACTION_JOIN_WAIT_WORDS + ". " + "You gotta wait another " + DurationFormatUtils.formatDurationWords(difference, true, true) + '.');
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionLeave(final PlayerLeaveFactionEvent event) {
        if (event.isForce() || event.isKick()) {
            return;
        }
        final Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            final Optional<Player> optional = event.getPlayer();
            if (optional.isPresent()) {
                final Player player = (Player)optional.get();
                if (this.plugin.getFactionManager().getFactionAt(player.getLocation()) == faction) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot leave your faction whilst you remain in its' territory.");
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.broadcast(ChatColor.DARK_GREEN + "Member Online: " + ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.GOLD + '.', player.getUniqueId());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.broadcast(ChatColor.RED + "Member Offline: " + ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.GOLD + '.');
        }
    }
}
