package net.nersa.kitmap.listener;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.event.FactionFocusChangeEvent;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FocusListener
  implements Listener
{
  @EventHandler
  public void onQuit(PlayerQuitEvent e)
  {
    for (PlayerFaction playerFaction : HCF.getInstance().getFactionManager().getPlayerFactions()) {
      if ((playerFaction.getFocus() != null) && (playerFaction.getFocus() == e.getPlayer().getUniqueId()))
      {
        Bukkit.getPluginManager().callEvent(new FactionFocusChangeEvent(playerFaction, e.getPlayer(), null));
        playerFaction.setFocus(null);
        return;
      }
    }
  }
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e)
  {
	 for (PlayerFaction playerFaction : HCF.getInstance().getFactionManager().getPlayerFactions()) {
      if ((playerFaction.getFocus() != null) && (playerFaction.getFocus() == e.getEntity().getUniqueId()))
      {
        Bukkit.getPluginManager().callEvent(new FactionFocusChangeEvent(playerFaction, e.getEntity(), null));
        playerFaction.setFocus(null);
        return;
      }
    }
  }
}
