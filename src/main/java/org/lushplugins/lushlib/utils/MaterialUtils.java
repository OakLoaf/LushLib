package org.lushplugins.lushlib.utils;

import org.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MaterialUtils {

    @NotNull
    public static List<Material> from(List<String> stringList) {
        List<Material> materials = new ArrayList<>();

        for (String string : stringList) {
            materials.addAll(from(string));
        }

        return materials;
    }

    @NotNull
    public static Collection<Material> from(String string) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(string);
        if (namespacedKey == null) {
            return Collections.emptyList();
        }

        if (string.charAt(0) == '#') {
            String[] registries = {Tag.REGISTRY_BLOCKS, Tag.REGISTRY_ITEMS};

            Tag<Material> tag = null;
            for (String registry : registries) {
                tag = Bukkit.getTag(registry, namespacedKey, Material.class);
                if (tag != null) {
                    break;
                }
            }

            return tag != null ? tag.getValues() : Collections.emptyList();
        } else {
            Material material = Registry.MATERIAL.get(namespacedKey);
            return material != null ? List.of(material) : Collections.emptyList();
        }
    }
}
