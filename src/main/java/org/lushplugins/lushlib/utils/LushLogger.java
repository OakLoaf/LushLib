package org.lushplugins.lushlib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LushLogger {
    private static Logger logger;

    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("LushLib");
        }

        return logger;
    }

    public static void setLogger(Logger logger) {
        LushLogger.logger = logger;
    }

    public static void logCurrentStackTrace(@NotNull Level level) {
        logCurrentStackTrace(level, null);
    }

    public static void logCurrentStackTrace(@NotNull Level level, @Nullable String message) {
        if (message != null) {
            getLogger().log(level, message);
        }

        for (StackTraceElement stackTrace : Thread.currentThread().getStackTrace()) {
            getLogger().log(level, stackTrace.toString());
        }
    }
}
