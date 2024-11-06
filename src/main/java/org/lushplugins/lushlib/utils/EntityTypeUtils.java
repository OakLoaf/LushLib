package org.lushplugins.lushlib.utils;

import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.registry.RegistryUtils;

import java.util.Collection;
import java.util.List;

/**
 * @see org.lushplugins.lushlib.registry.RegistryUtils
 */
@Deprecated(forRemoval = true)
public class EntityTypeUtils {

    @NotNull
    public static List<EntityType> from(List<String> stringList) {
        return RegistryUtils.fromStringList(stringList, Registry.ENTITY_TYPE);
    }

    @NotNull
    public static Collection<EntityType> from(String string) {
        return RegistryUtils.fromString(string, Registry.ENTITY_TYPE);
    }
}
