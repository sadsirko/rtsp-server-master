package com.rtsp.rtspserver.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.Instant;

public class UptimeManager {
    private Instant startTime;

    public static long getSystemUptime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getUptime() / 60000;  // Convert milliseconds to minutes
    }
}
