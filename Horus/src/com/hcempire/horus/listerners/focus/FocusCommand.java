package com.hcempire.horus.listerners.focus;

import com.alexandeh.ekko.factions.type.PlayerFaction;

import com.hcempire.horus.Horus;
import com.hcempire.horus.util.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class FocusCommand
implements CommandExecutor,
TabCompleter {
    private final Horus horus = Horus.getInstance();
    public static final HashMap<UUID, UUID> focusedMap = new HashMap();

    public static Player getFocusedPlayer(Player player) {
        return Bukkit.getServer().getPlayer(focusedMap.get(player.getUniqueId()));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can not execute this command on console."));
            return false;
        }
        Player player = (Player)sender;
        if (arguments.length == 0 || arguments.length > 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ("&cUsage: /" + label + " <playerName>")));
            return true;
        }
        if (arguments.length == 1) {
            Player target = Bukkit.getServer().getPlayerExact(arguments[0]);
            if (target.equals(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can not focus yourself."));
                return false;
            }
            if (EkkoUtils.MangoPlayer.isInFaction(player)) {
                PlayerFaction faction = PlayerFaction.getByPlayer((Player)player);
                PlayerFaction playerFaction = faction;
                if (playerFaction.getOfficers().contains(player.getUniqueId()) || playerFaction.getLeader().equals(player.getUniqueId())) {
                    if (playerFaction.getMembers().contains(target.getUniqueId()) || playerFaction.getAllies().contains(target.getUniqueId())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can not target your faction members or your allies."));
                    } else if (focusedMap.containsKey(player.getUniqueId()) && FocusCommand.getFocusedPlayer(player).equals(target)) {
                        for (Player playerMember : playerFaction.getOnlinePlayers()) {
                            focusedMap.remove(playerMember.getUniqueId());

                            Scoreboard scoreboard = player.getScoreboard();
                            if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard()))
                            {
                                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                                player.setScoreboard(scoreboard);
                            }
                            Team team = scoreboard.getTeam("focused");
                            if (team != null) {
                                team.removeEntry(target.getName());
                                player.showPlayer(target.getPlayer());
                            }

                            Team enemy = scoreboard.getTeam("enemy");

                            if (enemy == null) {
                                enemy = scoreboard.registerNewTeam("enemy");
                                enemy.setPrefix(ChatColor.YELLOW + "");
                                enemy.setCanSeeFriendlyInvisibles(true);
                            }
                            enemy.addEntry(target.getName());
                            player.showPlayer(target.getPlayer());

                            faction.sendMessage(Color.translate("&c" + target.getName() + " &ewill no longer be focused, removed by &a" + player.getName() + "&e."));
                        }
                    } else {
                        for (Player playerMember : playerFaction.getOnlinePlayers()) {
                            focusedMap.put(playerMember.getUniqueId(), target.getUniqueId());
                        }
                        faction.sendMessage(Color.translate("&c" + target.getName() + " &eis now be focused."));

                        Scoreboard scoreboard = player.getScoreboard();
                        if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard()))
                        {
                            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                            player.setScoreboard(scoreboard);
                        }
                        Team team = scoreboard.getTeam("focused");
                        if (team == null)
                        {
                            team = scoreboard.registerNewTeam("focused");
                            team.setPrefix(ChatColor.LIGHT_PURPLE + "");
                            team.setCanSeeFriendlyInvisibles(true);
                        }
                        team.addEntry(target.getName());
                        player.showPlayer(target.getPlayer());
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be either an officer or leader of the faction to execute this command."));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not in any faction."));
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length > 1) {
            return Collections.emptyList();
        }
        return null;
    }
}

