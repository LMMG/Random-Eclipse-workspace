package rip.kohi.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.bizarrealex.aether.scoreboard.cooldown.BoardFormat;

import net.md_5.bungee.api.ChatColor;
import rip.kohi.KohiFFA;
import rip.kohi.events.VanishEvent;
import rip.kohi.utils.C;

public class KohiFFAProvider implements BoardAdapter, Listener {


    @Override
    public String getTitle(Player player) {
        return ChatColor.translateAlternateColorCodes('&', "&6&lKohiFFA - [REMAKE]");
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("&7&m-------------------");
        strings.add(ChatColor.translateAlternateColorCodes('&', KohiFFA.config.getString("SCOREBOARD.KILLS") + player.getStatistic(Statistic.PLAYER_KILLS)));
        strings.add(ChatColor.translateAlternateColorCodes('&', KohiFFA.config.getString("SCOREBOARD.DEATHS") + player.getStatistic(Statistic.DEATHS)));
        if (player.isOp()) {
            strings.add(C.c("&e&lStaff Mode:"));
            strings.add(ChatColor.YELLOW + " » " + "Vanish: " + (VanishEvent.isVanished(player) ? C.c("&a") + "On" : C.c("&c") + "Off"));
            strings.add(ChatColor.YELLOW + " » " + "Gamemode: " + StringUtils.capitalize(player.getGameMode().name().toLowerCase()));
        }
        for(BoardCooldown cooldown : cooldowns) {
            if(!cooldown.getId().equals("profile")) continue;
            strings.add("&aProfile&7:&c " + cooldown.getFormattedString(BoardFormat.SECONDS));
        }
        for (BoardCooldown cooldown : cooldowns) {
            if (!cooldown.getId().equals("enderpearl")) continue;
            strings.add("&e&lEnderpearl&7:&c " + cooldown.getFormattedString(BoardFormat.SECONDS));
        }
        strings.add("&7&m-------------------&r");
        if (strings.size() == 2) {
            return null;
        }
        return strings;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Board board = Board.getByPlayer(player);

        BoardCooldown cooldown = board.getCooldown("profile");
        new BoardCooldown(board, "profile", 10.0);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Board board = Board.getByPlayer(player);
        if (event.getAction().name().contains("RIGHT")) {
            if (event.getItem() == null) {
                return;
            }
            if (event.getItem().getType() != Material.ENDER_PEARL) {
                return;
            }
            if (board == null) {
                return;
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                return;
            }
            BoardCooldown cooldown = board.getCooldown("enderpearl");
            if (cooldown != null) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(org.bukkit.ChatColor.RED + "You must wait " + cooldown.getFormattedString(BoardFormat.SECONDS) + " seconds before enderpearling again!");
                return;
            }
            new BoardCooldown(board, "enderpearl", 16.0);
        }
    }
}