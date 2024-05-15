package org.lushplugins.lushlib.utils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EntityTypeUtils {

    @NotNull
    public static List<EntityType> from(List<String> stringList) {
        List<EntityType> entityTypes = new ArrayList<>();

        for (String string : stringList) {
            entityTypes.addAll(from(string));
        }

        return entityTypes;
    }

    @NotNull
    public static Collection<EntityType> from(String string) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(string);
        if (namespacedKey == null) {
            return Collections.emptyList();
        }

        if (string.charAt(0) == '#') {
            Tag<EntityType> tag = Bukkit.getTag(Tag.REGISTRY_ENTITY_TYPES, namespacedKey, EntityType.class);
            return tag != null ? tag.getValues() : Collections.emptyList();
        } else {
            EntityType entityType = Registry.ENTITY_TYPE.get(namespacedKey);
            return entityType != null ? List.of(entityType) : Collections.emptyList();
        }
    }
}
