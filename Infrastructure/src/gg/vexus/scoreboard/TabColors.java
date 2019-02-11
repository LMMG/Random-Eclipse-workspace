package gg.vexus.scoreboard;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import gg.vexus.Core;
import server.wenjapvp.hcf.HCF;
import server.wenjapvp.hcf.faction.event.FactionCreateEvent;
import server.wenjapvp.hcf.faction.event.FactionRelationCreateEvent;
import server.wenjapvp.hcf.faction.event.FactionRelationRemoveEvent;
import server.wenjapvp.hcf.faction.event.FactionRemoveEvent;
import server.wenjapvp.hcf.faction.event.PlayerJoinFactionEvent;
import server.wenjapvp.hcf.faction.event.PlayerLeaveFactionEvent;
import server.wenjapvp.hcf.faction.struct.Relation;
import server.wenjapvp.hcf.faction.type.Faction;
import server.wenjapvp.hcf.faction.type.PlayerFaction;

public class TabColors implements Listener
{
    public static void init() {
        new BukkitRunnable() {
            public void run() {
                Player[] onlinePlayers;
                for (int length = (onlinePlayers = Bukkit.getServer().getOnlinePlayers()).length, i = 0; i < length; ++i) {
                    final Player all = onlinePlayers[i];
                    TabColors.resendTab(all);
                }
            }
        }.runTaskLater((Plugin)Core.getCore(), 40L);
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                TabColors.resendTab(p);
            }
        }.runTaskLater((Plugin)Core.getCore(), 40L);
        broadcastAndUpdate(p);
    }
    
    @EventHandler
    public void onFactionCreate(final FactionCreateEvent e) {
        if (e.getSender() != null) {
            broadcastAndUpdate((Player)e.getSender());
        }
    }
    
    @EventHandler
    public void onFactionDisband(final FactionRemoveEvent e) {
        if (e.getSender() != null) {
            broadcastAndUpdate((Player)e.getSender());
        }
        for (final Player all : ((PlayerFaction)e.getFaction()).getOnlinePlayers()) {
            broadcastAndUpdate(all);
        }
    }
    
    @EventHandler
    public void onFactionLeave(final PlayerLeaveFactionEvent e) {
        new BukkitRunnable() {
            public void run() {
                if (e.getPlayer() != null && e.getPlayer().isPresent()) {
                    TabColors.resendTab((Player)e.getPlayer().get());
                }
                for (final Player all : e.getFaction().getOnlinePlayers()) {
                    TabColors.resendTab(all);
                }
            }
        }.runTaskLater((Plugin)Core.getCore(), 3L);
    }
    
    @EventHandler
    public void onFactionPlayerJoin(final PlayerJoinFactionEvent e) {
        new BukkitRunnable() {
            public void run() {
                if (e.getPlayer() != null && e.getPlayer().isPresent()) {
                    TabColors.resendTab((Player)e.getPlayer().get());
                }
                for (final Player all : e.getFaction().getOnlinePlayers()) {
                    TabColors.resendTab(all);
                }
            }
        }.runTaskLater((Plugin)Core.getCore(), 3L);
    }
    
    @EventHandler
    public void onRelationChange(final FactionRelationCreateEvent e) {
        new BukkitRunnable() {
            public void run() {
                for (final Player all : e.getSenderFaction().getOnlinePlayers()) {
                    TabColors.broadcastAndUpdate(all);
                }
                for (final Player all : e.getTargetFaction().getOnlinePlayers()) {
                    TabColors.broadcastAndUpdate(all);
                }
            }
        }.runTaskLater((Plugin)Core.getCore(), 3L);
    }
    
    @EventHandler
    public void onRelationChange(final FactionRelationRemoveEvent paramFactionRelationEvent) {
        new BukkitRunnable() {
            public void run() {
                for (final Player player : paramFactionRelationEvent.getSenderFaction().getOnlinePlayers()) {
                    TabColors.broadcastAndUpdate(player);
                }
                for (final Player player : paramFactionRelationEvent.getTargetFaction().getOnlinePlayers()) {
                    TabColors.broadcastAndUpdate(player);
                }
            }
        }.runTaskLater((Plugin)Core.getCore(), 3L);
    }
    
    public static void broadcastAndUpdate(final Player p) {
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getServer().getOnlinePlayers()).length, i = 0; i < length; ++i) {
            final Player all = onlinePlayers[i];
            final Scoreboard sb = all.getScoreboard();
            if (sb != Bukkit.getScoreboardManager().getMainScoreboard() && all != p) {
                final Team enemy = getTeam(sb, "factions-enemy", ChatColor.RED);
                final Team ally = getTeam(sb, "factions-ally", ChatColor.BLUE);
                final Team member = getTeam(sb, "factions-member", ChatColor.DARK_GREEN);
                final PlayerFaction inFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(all.getUniqueId());
                if (inFaction == null) {
                    enemy.addEntry(all.getName());
                    return;
                }
                final PlayerFaction f = HCF.getPlugin().getFactionManager().getPlayerFaction(p.getUniqueId());
                if (f == null) {
                    enemy.addEntry(p.getName());
                    return;
                }
                final Relation r = f.getFactionRelation((Faction)inFaction);
                switch (r) {
                    case ENEMY: {
                        enemy.addEntry(p.getName());
                        break;
                    }
                    case MEMBER: {
                        member.addEntry(p.getName());
                        break;
                    }
                    case ALLY: {
                        ally.addEntry(p.getName());
                        break;
                    }
                }
            }
        }
    }
    
    public static void resendTab(final Player paramPlayer) {
        final Scoreboard scoreboard = paramPlayer.getScoreboard();
        final Team enemy = getTeam(scoreboard, "factions-enemy", ChatColor.RED);
        final Team ally = getTeam(scoreboard, "factions-ally", ChatColor.BLUE);
        final Team member = getTeam(scoreboard, "factions-member", ChatColor.DARK_GREEN);
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
            final Player others = onlinePlayers[i];
            final PlayerFaction inFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(paramPlayer.getUniqueId());
            if (inFaction == null) {
                enemy.addEntry(paramPlayer.getName());
                return;
            }
            final PlayerFaction faction = HCF.getPlugin().getFactionManager().getPlayerFaction(others.getUniqueId());
            if (faction == null) {
                enemy.addEntry(others.getName());
                return;
            }
            final Relation relation = faction.getFactionRelation((Faction)inFaction);
            for (final Player inFactionPlayer : inFaction.getOnlinePlayers()) {
                switch (relation) {
                    default: {
                        continue;
                    }
                    case ENEMY: {
                        enemy.addPlayer((OfflinePlayer)inFactionPlayer);
                        continue;
                    }
                    case MEMBER: {
                        member.addPlayer((OfflinePlayer)inFactionPlayer);
                        continue;
                    }
                    case ALLY: {
                        ally.addPlayer((OfflinePlayer)inFactionPlayer);
                        continue;
                    }
                }
            }
        }
    }
    
    private static Team getTeam(final Scoreboard paramScoreboard, final String paramTeam, final ChatColor paramChatColor) {
        Team enemy = paramScoreboard.getTeam(paramTeam);
        if (enemy == null) {
            enemy = paramScoreboard.registerNewTeam(paramTeam);
            enemy.setPrefix(paramChatColor.toString());
        }
        return enemy;
    }
}
