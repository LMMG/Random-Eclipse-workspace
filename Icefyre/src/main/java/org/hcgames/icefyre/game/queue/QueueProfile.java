package org.hcgames.icefyre.game.queue;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.profile.Profile;
import lombok.Getter;

import java.text.DecimalFormat;

public class QueueProfile {

    private static Icefyre main = Icefyre.getInstance();
    public static final int STARTING_RANGE = main.getConfigFile().getInt("QUEUE.STARTING_RANGE");

    @Getter private final Profile profile;
    @Getter private final Queue queue;
    @Getter private final long start;
    @Getter private final int ranking;
    @Getter private final int[] range;

    public QueueProfile(Profile profile, Queue queue, int ranking) {
        this.profile = profile;
        this.queue = queue;
        this.start = System.currentTimeMillis();
        this.ranking = ranking;
        this.range = new int[1];
    }

    public String getFormattedTimeInQueue() {
        return DurationFormatUtils.formatDuration(System.currentTimeMillis() - start, "mm:ss");
    }

}
