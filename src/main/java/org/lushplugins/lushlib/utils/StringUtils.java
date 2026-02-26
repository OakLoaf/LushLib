package org.lushplugins.lushlib.utils;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class StringUtils {

    public static String replace(String string, String target, Callable<String> replacement) {
        if (string.contains(target)) {
            try {
                return string.replace(string, replacement.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return string;
        }
    }

    public static String makeFriendly(String string) {
        StringBuilder output = new StringBuilder();

        String[] words = string.toLowerCase().split(" ");
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                output.append(" ");
            }

            String word = words[i];
            output.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
        }

        return output.toString();
    }

    public static <T extends Enum<T>> Optional<T> getEnum(String content, Class<T> clazz) {
        return getEnum(content, clazz, null);
    }

    public static <T extends Enum<T>> Optional<T> getEnum(String content, Class<T> clazz, @Nullable T def) {
        try {
            return Optional.of(Enum.valueOf(clazz, content.toUpperCase()));
        } catch (Exception e) {
            if (def != null) {
                return Optional.of(def);
            } else {
                return Optional.empty();
            }
        }
    }
}
