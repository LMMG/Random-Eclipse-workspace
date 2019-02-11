package net.nersa.kitmap.eventgame;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.eventgame.argument.*;

import com.doctordark.util.command.ArgumentExecutor;

/**
 * Handles the execution and tab completion of the event command.
 */
public class EventExecutor extends ArgumentExecutor {

	public EventExecutor(HCF plugin) {
		super("event");

		addArgument(new EventCancelArgument(plugin));
		addArgument(new EventCreateArgument(plugin));
		addArgument(new EventDeleteArgument(plugin));
		addArgument(new EventRenameArgument(plugin));
		addArgument(new EventSetAreaArgument(plugin));
		addArgument(new EventSetCapzoneArgument(plugin));
		addArgument(new EventAddLootTableArgument(plugin));
		addArgument(new EventDelLootTableArgument(plugin));
		addArgument(new EventSetLootArgument(plugin));
		addArgument(new EventStartArgument(plugin));
		addArgument(new EventUptimeArgument(plugin));
	}
}