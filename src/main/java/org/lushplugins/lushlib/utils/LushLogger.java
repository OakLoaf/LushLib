package org.lushplugins.lushlib.utils;

import java.util.logging.Logger;

public class LushLogger {
    private static Logger logger;

    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("LushLub");
        }

        return logger;
    }

    public static void setLogger(Logger logger) {
        LushLogger.logger = logger;
    }
}
