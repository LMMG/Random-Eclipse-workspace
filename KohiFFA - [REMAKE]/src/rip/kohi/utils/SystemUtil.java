package rip.kohi.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * Created by Owner on 02/07/2017.
 */
public class SystemUtil {

    private static final Runtime runtime = Runtime.getRuntime();

    public static long getFreeMemory() {
        return calcMB(runtime.freeMemory());
    }

    public static long getMaximumMemory() {
        return calcMB(runtime.maxMemory());
    }

    public static long getTotalMemory() {
        return calcMB(runtime.totalMemory());
    }

    public static int getTotalProcessorCount() {
        return ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getAvailableProcessors();
    }

    private static long calcMB(long time) {
        return time / 1024 / 1024;
    }
}