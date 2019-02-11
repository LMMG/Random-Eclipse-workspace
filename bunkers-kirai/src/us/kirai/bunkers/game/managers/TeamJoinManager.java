package us.kirai.bunkers.game.managers;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Wool;

import us.kirai.bunkers.Bunkers;
import us.kirai.bunkers.game.GameState;
import us.kirai.bunkers.game.Team;

/**
 * Created by Spirit  on 09/08/2017.
 */
public class TeamJoinManager implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(Bunkers.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (e.getPlayer().getItemInHand().getType() == Material.WOOL) {
                    Wool wool = (Wool) e.getPlayer().getItemInHand().getData();
                    // blue green red yellow 
                    if(Bunkers.getInstance().getGameHandler().players.containsKey(e.getPlayer().getName()))
                        Bunkers.getInstance().getGameHandler().players.remove(e.getPlayer().getName());
                   // e.setCancelled(true);
                    if(wool.getColor() == DyeColor.BLUE) {
                        Bunkers.getInstance().getGameHandler().players.put(e.getPlayer().getName(), Team.BLUE);
                        e.getPlayer().sendMessage(ChatColor.YELLOW + "You are now on the " + ChatColor.BLUE + "Blue" + ChatColor.YELLOW + " team.");
                    }
                    if(wool.getColor() == DyeColor.GREEN) {
                        Bunkers.getInstance().getGameHandler().players.put(e.getPlayer().getName(), Team.GREEN);
                        e.getPlayer().sendMessage(ChatColor.YELLOW + "You are now on the " + ChatColor.GREEN + "Green" + ChatColor.YELLOW + " team.");
                    }
                    if(wool.getColor() == DyeColor.RED) {
                        Bunkers.getInstance().getGameHandler().players.put(e.getPlayer().getName(), Team.RED);
                        e.getPlayer().sendMessage(ChatColor.YELLOW + "You are now on the " + ChatColor.RED + "Red" + ChatColor.YELLOW + " team.");
                    }
                    if(wool.getColor() == DyeColor.YELLOW) {
                        Bunkers.getInstance().getGameHandler().players.put(e.getPlayer().getName(), Team.YELLOW);
                        e.getPlayer().sendMessage(ChatColor.YELLOW + "You are now on the " + ChatColor.GOLD + "Yellow" + ChatColor.YELLOW + " team.");
                    }
                    if(wool.getColor() == DyeColor.WHITE) {
                        Bunkers.getInstance().getGameHandler().players.remove(e.getPlayer().getName());
                        e.getPlayer().kickPlayer("§eYou have kicked yourself from the Bunkers game!");
                        
                    }
            }
        }
    }
}
}