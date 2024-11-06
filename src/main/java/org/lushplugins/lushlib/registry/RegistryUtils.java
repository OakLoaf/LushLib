package org.lushplugins.lushlib.registry;

import org.bukkit.*;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.utils.LushLogger;

import java.util.*;

public class RegistryUtils {

    /**
     * Get registry value from a string
     * @param string string to parse
     * @param registry registry to get values from
     * @return parsed value or null if none was found
     */
    public static <T extends Keyed> @Nullable T parseString(String string, Registry<T> registry) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(string.toLowerCase());
        if (namespacedKey == null) {
            LushLogger.getLogger().warning("'" + string + "' contains invalid characters");
            return null;
        }

        return registry.get(namespacedKey);
    }

    /**
     * Get registry and tag values from a string
     * @param string string to parse
     * @param registry registry to get values from
     * @return collection of values
     */
    public static <T extends Keyed> Collection<T> fromString(String string, Registry<T> registry) {
        boolean isTag = string.charAt(0) == '#';
        NamespacedKey namespacedKey = NamespacedKey.fromString(isTag ? string.substring(1) : string.toLowerCase());
        if (namespacedKey == null) {
            LushLogger.getLogger().warning("'" + string + "' contains invalid characters");
            return Collections.emptyList();
        }

        if (isTag) {
            TagType<T> tagType = TagType.get(registry);
            if (tagType == null) {
                return Collections.emptyList();
            }

            Tag<T> tag = TagType.getTag(namespacedKey, registry);
            if (tag != null) {
                return tag.getValues();
            }
        } else {
            T t = registry.get(namespacedKey);
            if (t != null) {
                return List.of(t);
            }
        }

        LushLogger.getLogger().warning("Could not find value in registry for '" + string + "'");
        return Collections.emptyList();
    }

    /**
     * Get registry and tag values from a list of strings
     * @param stringList list of strings to parse, this can include registry values and tags
     * @param registry registry to get values from
     * @return collection of values
     */
    public static <T extends Keyed> List<T> fromStringList(List<String> stringList, Registry<T> registry) {
        return stringList.stream()
            .flatMap(string -> fromString(string, registry).stream())
            .toList();
    }
}
