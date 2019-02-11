package net.nersa.kitmap.faction.event;

import java.util.UUID;

import net.nersa.kitmap.faction.type.PlayerFaction;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FactionFocusChangeEvent
  extends Event
{
  private static final HandlerList handlers = new HandlerList();
  private final PlayerFaction senderFaction;
  private final Player player;
  private final UUID oldFocus;
  
  public FactionFocusChangeEvent(PlayerFaction senderFaction, Player player, UUID oldFocus)
  {
    this.senderFaction = senderFaction;
    this.player = player;
    this.oldFocus = oldFocus;
  }
  
  public UUID getOldFocus()
  {
    return this.oldFocus;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public PlayerFaction getSenderFaction()
  {
    return this.senderFaction;
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
}
