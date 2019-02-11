/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils
 */
package com.hcempire.horus.util;

import com.hcempire.horus.util.DateTimeFormats;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

public class DurationFormatter {
    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long HOUR = TimeUnit.HOURS.toMillis(1);

    public static String getRemaining(long millis, boolean milliseconds) {
        return DurationFormatter.getRemaining(millis, milliseconds, true);
    }

    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < MINUTE) {
            return (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format((double)duration * 0.001) + 's';
        }
        return DurationFormatUtils.formatDuration((long)duration, (String)((duration >= HOUR ? "HH:" : "") + "mm:ss"));
    }
}

