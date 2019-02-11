package net.nersa.kitmap.eventgame;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.tracker.ConquestTracker;
import net.nersa.kitmap.eventgame.tracker.EventTracker;
import net.nersa.kitmap.eventgame.tracker.KothTracker;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

public enum EventType {

	CONQUEST("Conquest", new ConquestTracker(HCF.getInstance())), KOTH("KOTH", new KothTracker(HCF.getInstance()));

	private static final ImmutableMap<String, EventType> byDisplayName;

	static {
		ImmutableMap.Builder<String, EventType> builder = new ImmutableBiMap.Builder<>();
		for (EventType eventType : values()) {
			builder.put(eventType.displayName.toLowerCase(), eventType);
		}

		byDisplayName = builder.build();
	}

	private final EventTracker eventTracker;
	private final String displayName;

	EventType(String displayName, EventTracker eventTracker) {
		this.displayName = displayName;
		this.eventTracker = eventTracker;
	}

	@Deprecated
	public static EventType getByDisplayName(String name) {
		return byDisplayName.get(name.toLowerCase());
	}

	public EventTracker getEventTracker() {
		return eventTracker;
	}

	public String getDisplayName() {
		return displayName;
	}
}
