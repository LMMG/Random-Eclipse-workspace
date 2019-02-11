package com.bizarrealex.aether.scoreboard;

import com.bizarrealex.aether.Aether;
import com.bizarrealex.aether.AetherOptions;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.BoardEntry;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Board {
    private static Set<Board> boards = new HashSet<Board>();
    private Scoreboard scoreboard;
    private Player player;
    private Objective objective;
    private Set<String> keys;
    private List<BoardEntry> entries;
    private Set<BoardCooldown> cooldowns;
    private final Aether aether;
    private final AetherOptions options;

    public Board(Player player, Aether aether, AetherOptions options) {
        this.player = player;
        this.aether = aether;
        this.options = options;
        this.keys = new HashSet<String>();
        this.cooldowns = new HashSet<BoardCooldown>();
        this.entries = new ArrayList<BoardEntry>();
        this.setup();
    }

    private void setup() {
        this.scoreboard = this.options.hook() && !this.player.getScoreboard().equals((Object)Bukkit.getScoreboardManager().getMainScoreboard()) ? this.player.getScoreboard() : Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("glaedr_is_shit", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        if (this.aether.getAdapter() != null) {
            this.objective.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)this.aether.getAdapter().getTitle(this.player)));
        } else {
            this.objective.setDisplayName("Default Title");
        }
        boards.add(this);
    }

    public String getNewKey(BoardEntry entry) {
        for (ChatColor color : ChatColor.values()) {
            String colorText = (Object)color + "" + (Object)ChatColor.WHITE;
            if (entry.getText().length() > 16) {
                String sub = entry.getText().substring(0, 16);
                colorText = colorText + ChatColor.getLastColors((String)sub);
            }
            if (this.keys.contains(colorText)) continue;
            this.keys.add(colorText);
            return colorText;
        }
        throw new IndexOutOfBoundsException("No more keys available!");
    }

    public BoardCooldown getCooldown(String id) {
        for (BoardCooldown cooldown : this.getCooldowns()) {
            if (!cooldown.getId().equals(id)) continue;
            return cooldown;
        }
        return null;
    }

    public Set<BoardCooldown> getCooldowns() {
        Iterator<BoardCooldown> iterator = this.cooldowns.iterator();
        while (iterator.hasNext()) {
            BoardCooldown cooldown = iterator.next();
            if (System.currentTimeMillis() < cooldown.getEnd()) continue;
            iterator.remove();
        }
        return this.cooldowns;
    }

    public static Board getByPlayer(Player player) {
        for (Board board : boards) {
            if (!board.getPlayer().getName().equals(player.getName())) continue;
            return board;
        }
        return null;
    }

    public static Set<Board> getBoards() {
        return boards;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public Set<String> getKeys() {
        return this.keys;
    }

    public List<BoardEntry> getEntries() {
        return this.entries;
    }
}