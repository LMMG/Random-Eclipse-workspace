/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 */
package com.bizarrealex.aether;

import com.bizarrealex.aether.AetherOptions;
import com.bizarrealex.aether.event.BoardCreateEvent;
import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.BoardEntry;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Aether
implements Listener {
    private JavaPlugin plugin;
    private AetherOptions options;
    BoardAdapter adapter;

    public Aether(JavaPlugin plugin, BoardAdapter adapter, AetherOptions options) {
        this.options = options;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.setAdapter(adapter);
        this.run();
    }

    public Aether(JavaPlugin plugin, BoardAdapter adapter) {
        this(plugin, adapter, AetherOptions.defaultOptions());
    }

    public Aether(JavaPlugin plugin) {
        this(plugin, null, AetherOptions.defaultOptions());
    }

    private void run() {
        new BukkitRunnable(){

            public void run() {
                if (Aether.this.adapter == null) {
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Board board = Board.getByPlayer(player);
                    if (board == null) continue;
                    List<String> scores = Aether.this.adapter.getScoreboard(player, board, board.getCooldowns());
                    ArrayList<String> translatedScores = new ArrayList<String>();
                    if (scores == null) {
                        if (board.getEntries().isEmpty()) continue;
                        for (BoardEntry boardEntry : board.getEntries()) {
                            boardEntry.remove();
                        }
                        board.getEntries().clear();
                        continue;
                    }
                    for (String line : scores) {
                        translatedScores.add(ChatColor.translateAlternateColorCodes((char)'&', (String)line));
                    }
                    if (!Aether.this.options.scoreDirectionDown()) {
                        Collections.reverse(scores);
                    }
                    Scoreboard scoreboard = board.getScoreboard();
                    Objective objective = board.getObjective();
                    if (!objective.getDisplayName().equals(Aether.this.adapter.getTitle(player))) {
                        objective.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)Aether.this.adapter.getTitle(player)));
                    }
                    block3 : for (int i = 0; i < scores.size(); ++i) {
                        String text = scores.get(i);
                        int position = Aether.this.options.scoreDirectionDown() ? 15 - i : i + 1;
                        for (BoardEntry boardEntry : new ArrayList<BoardEntry>(board.getEntries())) {
                            Score score = objective.getScore(boardEntry.getKey());
                            if (score != null && boardEntry.getText().equals(ChatColor.translateAlternateColorCodes((char)'&', (String)text)) && score.getScore() == position) continue block3;
                        }
                        int positionToSearch = Aether.this.options.scoreDirectionDown() ? 15 - position : position - 1;
                        Iterator<BoardEntry> iterator = board.getEntries().iterator();
                        while (iterator.hasNext()) {
                            BoardEntry boardEntry2 = iterator.next();
                            int entryPosition = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(boardEntry2.getKey()).getScore();
                            if (Aether.this.options.scoreDirectionDown() || entryPosition <= scores.size()) continue;
                            iterator.remove();
                            boardEntry2.remove();
                        }
                        BoardEntry entry = board.getByPosition(positionToSearch);
                        if (entry == null) {
                            new BoardEntry(board, text).send(position);
                        } else {
                            entry.setText(text).setup().send(position);
                        }
                        if (board.getEntries().size() <= scores.size()) continue;
                        iterator = board.getEntries().iterator();
                        while (iterator.hasNext()) {
                            BoardEntry boardEntry3 = iterator.next();
                            if (translatedScores.contains(boardEntry3.getText()) && Collections.frequency(board.getBoardEntriesFormatted(), boardEntry3.getText()) <= 1) continue;
                            iterator.remove();
                            boardEntry3.remove();
                        }
                    }
                    player.setScoreboard(scoreboard);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)this.plugin, 20, 2);
    }

    public void setAdapter(BoardAdapter adapter) {
        this.adapter = adapter;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Board board = Board.getByPlayer(player);
            if (board != null) {
                Board.getBoards().remove(board);
            }
            Bukkit.getPluginManager().callEvent((Event)new BoardCreateEvent(new Board(player, this, this.options), player));
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (Board.getByPlayer(event.getPlayer()) == null) {
            Bukkit.getPluginManager().callEvent((Event)new BoardCreateEvent(new Board(event.getPlayer(), this, this.options), event.getPlayer()));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Board board = Board.getByPlayer(event.getPlayer());
        if (board != null) {
            Board.getBoards().remove(board);
        }
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public AetherOptions getOptions() {
        return this.options;
    }

    public BoardAdapter getAdapter() {
        return this.adapter;
    }

}

