package me.dave.platyutils.language;

import java.util.HashMap;

public class LanguageManager {
    private static final HashMap<String, String> messages = new HashMap<>();

    static {
        setMessage("insufficient-permissions", "&#ff6969You don't have the sufficient permissions for this command");
    }

    public static String getMessage(String name) {
        return messages.getOrDefault(name, "Message '" + name + "' is undefined");
    }

    public static void setMessage(String name, String message) {
        messages.put(name, message);
    }
}
