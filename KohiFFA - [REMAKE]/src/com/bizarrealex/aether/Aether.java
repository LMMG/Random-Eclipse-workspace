package com.bizarrealex.aether;

import com.bizarrealex.aether.AetherOptions;
import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.BoardEntry;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
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
                    if (scores == null) {
                        if (board.getEntries().isEmpty()) continue;
                        for (BoardEntry boardEntry : board.getEntries()) {
                            boardEntry.remove();
                        }
                        board.getEntries().clear();
                        continue;
                    }
                    if (!Aether.this.options.scoreDirectionDown()) {
                        Collections.reverse(scores);
                    }
                    Scoreboard scoreboard = board.getScoreboard();
                    Objective objective = board.getObjective();
                    if (!objective.getDisplayName().equals(Aether.this.adapter.getTitle(player))) {
                        objective.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)Aether.this.adapter.getTitle(player)));
                    }
                    block2 : for (int i = 0; i < scores.size(); ++i) {
                        String text = scores.get(i);
                        int position = Aether.this.options.scoreDirectionDown() ? 15 - i : i + 1;
                        Iterator<BoardEntry> iterator = board.getEntries().iterator();
                        while (iterator.hasNext()) {
                            BoardEntry boardEntry = iterator.next();
                            Score score = objective.getScore(boardEntry.getKey());
                            if (!scores.contains(boardEntry.getOriginalText())) {
                                iterator.remove();
                                boardEntry.remove();
                                continue;
                            }
                            if (score == null || !boardEntry.getOriginalText().equals(text)) continue;
                            if (score.getScore() == position) continue block2;
                            iterator.remove();
                            boardEntry.remove();
                        }
                        new BoardEntry(board, text).send(position);
                    }
                    player.setScoreboard(scoreboard);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)this.plugin, 2, 2);
    }

    public void setAdapter(BoardAdapter adapter) {
        this.adapter = adapter;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Board board = Board.getByPlayer(player);
            if (board != null) {
                Board.getBoards().remove(board);
            }
            new Board(player, this, this.options);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (Board.getByPlayer(event.getPlayer()) == null) {
            new Board(event.getPlayer(), this, this.options);
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