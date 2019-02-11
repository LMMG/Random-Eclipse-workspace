package com.hcempire.horus.event;

import com.hcempire.horus.Horus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static EventManager instance = new EventManager(Horus.getInstance());
    @Getter private final Horus main;
    @Getter private final List<Event> events;

    public EventManager(Horus main) {
        this.main = main;
        this.events = new ArrayList<>();
    }

    public Event getByName(String name) {
        for (Event event : events) {
            if (event.getName().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }

    public static EventManager getInstance() {
        return instance;
    }
}
