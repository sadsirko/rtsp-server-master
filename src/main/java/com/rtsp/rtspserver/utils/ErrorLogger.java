package com.rtsp.rtspserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLogger {
    private static final Logger log = LoggerFactory.getLogger(ErrorLogger.class);
    private static int errorCount = 0;

    public static void logError(String message, Throwable throwable) {
        log.error(message, throwable);
        errorCount++;
    }

    public static int getErrorCount() {
        return errorCount;
    }
}
