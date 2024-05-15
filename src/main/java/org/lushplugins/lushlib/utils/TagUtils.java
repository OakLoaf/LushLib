package org.lushplugins.lushlib.utils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagUtils {

    public static <T extends Keyed> List<T> parseStringList(List<String> stringList, TagType<T> tagType) {
        List<T> results = new ArrayList<>();

        for (String string : stringList) {
            results.addAll(parseString(string, tagType));
        }

        return results;
    }

    public static <T extends Keyed> Collection<T> parseString(String string, TagType<T> tagType) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(string);
        if (namespacedKey == null) {
            return Collections.emptyList();
        }

        if (string.charAt(0) == '#') {
            Tag<T> tag = null;
            for (String registry : tagType.registries()) {
                tag = Bukkit.getTag(registry, namespacedKey, tagType.dataType());
                if (tag != null) {
                    break;
                }
            }

            return tag != null ? tag.getValues() : Collections.emptyList();
        } else {
            T t = tagType.registry().get(namespacedKey);
            return t != null ? List.of(t) : Collections.emptyList();
        }
    }

    public record TagType<T extends Keyed>(Registry<T> registry, String[] registries, Class<T> dataType) {
        public static final TagType<EntityType> ENTITY_TYPES = new TagType<>(Registry.ENTITY_TYPE, new String[]{Tag.REGISTRY_ENTITY_TYPES}, EntityType.class);
        public static final TagType<Fluid> FLUIDS = new TagType<>(Registry.FLUID, new String[]{Tag.REGISTRY_FLUIDS}, Fluid.class);
        public static final TagType<Material> MATERIALS = new TagType<>(Registry.MATERIAL, new String[]{Tag.REGISTRY_BLOCKS, Tag.REGISTRY_ITEMS}, Material.class);
    }
}
