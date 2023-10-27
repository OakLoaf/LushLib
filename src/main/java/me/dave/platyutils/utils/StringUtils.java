package me.dave.platyutils.utils;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unused")
public class StringUtils {

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
