package org.ipvp.hcf.combatlog;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class LoggerDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final LoggerEntity loggerEntity;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
