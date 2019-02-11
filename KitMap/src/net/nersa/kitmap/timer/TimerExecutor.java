package net.nersa.kitmap.timer;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.timer.argument.TimerCheckArgument;
import net.nersa.kitmap.timer.argument.TimerSetArgument;

import com.doctordark.util.command.ArgumentExecutor;

/**
 * Handles the execution and tab completion of the timer command.
 */
public class TimerExecutor extends ArgumentExecutor {

	public TimerExecutor(HCF plugin) {
		super("timer");

		addArgument(new TimerCheckArgument(plugin));
		addArgument(new TimerSetArgument(plugin));
	}
}