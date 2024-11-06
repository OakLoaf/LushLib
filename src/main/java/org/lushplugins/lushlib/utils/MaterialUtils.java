package org.lushplugins.lushlib.utils;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.registry.RegistryUtils;

import java.util.Collection;
import java.util.List;

/**
 * @see org.lushplugins.lushlib.registry.RegistryUtils
 */
@Deprecated(forRemoval = true)
public class MaterialUtils {

    @NotNull
    public static List<Material> from(List<String> stringList) {
        return RegistryUtils.fromStringList(stringList, Registry.MATERIAL);
    }

    @NotNull
    public static Collection<Material> from(String string) {
        return RegistryUtils.fromString(string, Registry.MATERIAL);
    }
}
