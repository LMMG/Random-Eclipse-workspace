package com.hcempire.horus.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class EventZone {

    @Getter @Setter private Location firstLocation, secondLocation;

    public EventZone(Location firstLocation, Location secondLocation) {
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
    }

    public int getFirstX() {
        return Math.min(firstLocation.getBlockX(), secondLocation.getBlockX());
    }

    public int getSecondX() {
        return Math.max(firstLocation.getBlockX(), secondLocation.getBlockX());
    }

    public int getFirstZ() {
        return Math.min(firstLocation.getBlockZ(), secondLocation.getBlockZ());
    }

    public int getSecondZ() {
        return Math.max(firstLocation.getBlockZ(), secondLocation.getBlockZ());
    }

    public int getFirstY() {
        return Math.min(firstLocation.getBlockY(), secondLocation.getBlockY());
    }

    public int getSecondY() {
        return Math.max(firstLocation.getBlockY(), secondLocation.getBlockY());
    }

    public boolean isInside(Location location) {
        return (location.getWorld().getName().equalsIgnoreCase(firstLocation.getWorld().getName()) && (location.getBlockX() >= getFirstX() && location.getBlockX() <= getSecondX()) && (location.getBlockZ() >= getFirstZ() && location.getBlockZ() <= getSecondZ()) && (location.getBlockY() >= getFirstY() && location.getBlockY() <= getSecondY()));
    }

}
