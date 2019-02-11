package gg.mist.hcf.timer;

import com.doctordark.util.command.ArgumentExecutor;
import gg.mist.hcf.HCF;
import gg.mist.hcf.timer.argument.TimerCheckArgument;
import gg.mist.hcf.timer.argument.TimerSetArgument;

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