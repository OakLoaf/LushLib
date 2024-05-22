package org.lushplugins.lushlib.utils;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.Collection;
import java.util.List;

public class RegistryUtils {

    public static <T extends Keyed> List<T> fromStringList(Registry<T> registry, Collection<String> stringList) {
        return stringList.stream().map(string -> RegistryUtils.fromString(registry, string)).toList();
    }

    public static <T extends Keyed> T fromString(Registry<T> registry, String string) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(string);
        if (namespacedKey == null) {
            LushLogger.getLogger().warning("'" + string + "' contains invalid characters");
            return null;
        }

        T t = registry.get(namespacedKey);
        if (t != null) {
            return t;
        } else {
            LushLogger.getLogger().warning("'" + string + "' is not registered");
            return null;
        }
    }
}
