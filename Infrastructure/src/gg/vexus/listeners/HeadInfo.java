package gg.vexus.listeners;

import org.bukkit.ChatColor;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HeadInfo
  implements Listener
{
  @EventHandler
  public void onInteract(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
    {
      BlockState state = e.getClickedBlock().getState();
      if ((state instanceof Skull))
      {
        Skull s = (Skull)state;
        if (s.getSkullType() == SkullType.PLAYER) {
          p.sendMessage(ChatColor.YELLOW + "Head of " + ChatColor.WHITE + s.getOwner() + ChatColor.YELLOW + ".");
        }
      }
    }
  }
}
