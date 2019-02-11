package support.evocative.scoreboard;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.bizarrealex.aether.scoreboard.cooldown.BoardFormat;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Owner on 01/09/2017.
 */
public class Provider implements BoardAdapter, Listener {

    public static int players = Bukkit.getServer().getOnlinePlayers().size();

    public String getTitle(Player player) {
        return ChatColor.translateAlternateColorCodes('&', "&6&lCombo &4[Beta]");
    }

    public List<String> getScoreboard(Player var1, Board var2, Set<BoardCooldown> cooldowns) {
        List<String> args = new ArrayList<String>();
        args.add(ChatColor.translateAlternateColorCodes('&', "&c&lPlayers: &f" + players));
        for(BoardCooldown cooldown : cooldowns) {
            if(!cooldown.getId().equalsIgnoreCase("enderpearl")) continue;
            args.add("&e&lEnderpearl&7:&6 " + cooldown.getFormattedString(BoardFormat.SECONDS));
        }
        args.add(ChatColor.translateAlternateColorCodes('&', "&7&m-------------"));
        args.add(ChatColor.translateAlternateColorCodes('&', "&4Kills: &6" + var1.getStatistic(Statistic.PLAYER_KILLS)));
        args.add(ChatColor.translateAlternateColorCodes('&', "&4Deaths: &6" + var1.getStatistic(Statistic.DEATHS)));
        args.add(ChatColor.translateAlternateColorCodes('&', "&4Score: &6"));
        return args;
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
                player.sendMessage((Object)ChatColor.RED + "You must wait " + cooldown.getFormattedString(BoardFormat.SECONDS) + " seconds before enderpearling again!");
                return;
            }
            new BoardCooldown(board, "enderpearl", 16.0);
        }
    }
}
