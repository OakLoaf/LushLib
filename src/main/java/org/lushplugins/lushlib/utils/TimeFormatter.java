package org.lushplugins.lushlib.utils;

import java.time.Duration;

public class TimeFormatter {

    public static String formatDuration(Duration duration, FormatType formatType) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours);

            switch (formatType) {
                case LONG_FORM -> result.append(" hours ");
                case SHORT_FORM -> result.append("h ");
            }
        }

        if (minutes > 0) {
            result.append(minutes);

            switch (formatType) {
                case LONG_FORM -> result.append(" minutes ");
                case SHORT_FORM -> result.append("m ");
            }
        }

        if (seconds > 0) {
            result.append(seconds);

            switch (formatType) {
                case LONG_FORM -> result.append(" seconds ");
                case SHORT_FORM -> result.append("s ");
            }
        }

        return result.toString().trim();
    }

    public enum FormatType {
        LONG_FORM,
        SHORT_FORM
    }
}
