package org.ipvp.hcf.listener.fixes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.ipvp.hcf.HCF;

public class PhaseListener 
  implements Listener
{
	  @EventHandler
	  public void onMove(PlayerInteractEvent e)
	  {
	    if ((e.getPlayer().getLocation().getBlock() != null) && 
	      (e.getPlayer().getLocation().getBlock().getType() == Material.TRAP_DOOR) && 
	      (!HCF.getPlugin().getFactionManager().getFactionAt(e.getPlayer().getLocation()).equals(HCF.getPlugin().getFactionManager().getPlayerFaction(e.getPlayer().getUniqueId())))) {
	      e.getPlayer().teleport(e.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D));
	    }
	  }
	  
	  @EventHandler(priority=EventPriority.HIGHEST)
	  public void onDamage(EntityDamageEvent e)
	  {
	    if ((HCF.getPlugin().getSotwTimer().getRemaining() > 0L) && 
	      ((e.getEntity() instanceof Player)) && (e.getCause() != EntityDamageEvent.DamageCause.VOID)) {
	      e.setCancelled(true);
	    }
	  }
	  
	  @EventHandler(priority=EventPriority.MONITOR)
	  public void onFoodLevelChange(FoodLevelChangeEvent e)
	  {
	    if ((HCF.getPlugin().getSotwTimer().getRemaining() > 0L) && 
	      ((e.getEntity() instanceof Player)))
	    {
	      e.setCancelled(true);
	      e.setFoodLevel(20);
	    }
    }
}
