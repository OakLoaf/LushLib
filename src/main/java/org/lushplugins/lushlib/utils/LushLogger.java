package org.lushplugins.lushlib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @see org.lushplugins.lushlib.LushLogger
 */
@Deprecated(forRemoval = true)
public class LushLogger {

    public static Logger getLogger() {
        return org.lushplugins.lushlib.LushLogger.getLogger();
    }

    public static void setLogger(Logger logger) {
        org.lushplugins.lushlib.LushLogger.setLogger(logger);
    }

    public static void logCurrentStackTrace(@NotNull Level level) {
        org.lushplugins.lushlib.LushLogger.logCurrentStackTrace(level);
    }

    public static void logCurrentStackTrace(@NotNull Level level, @Nullable String message) {
        org.lushplugins.lushlib.LushLogger.logCurrentStackTrace(level, message);
    }
}
