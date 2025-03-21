package org.lushplugins.lushlib.utils;

import org.bukkit.*;

import java.util.Collection;
import java.util.List;

/**
 * @see org.lushplugins.lushlib.registry.RegistryUtils
 */
@Deprecated(forRemoval = true)
public class RegistryUtils {

    public static <T extends Keyed> List<T> fromStringList(Registry<T> registry, Collection<String> stringList) {
        return stringList.stream().map(string -> RegistryUtils.fromString(registry, string)).toList();
    }

    public static <T extends Keyed> T fromString(Registry<T> registry, String string) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(string.toLowerCase());
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
