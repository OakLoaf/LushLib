package me.dave.lushlib.language;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class LanguageManager {
    private static final HashMap<String, String> messages = new HashMap<>();

    static {
        setMessage("insufficient-permissions", "&#ff6969You don't have the sufficient permissions for this command");
    }

    @Nullable
    public static String getMessage(String name) {
        return messages.get(name);
    }

    public static void setMessage(@NotNull String name, @NotNull String message) {
        messages.put(name, message);
    }
}
