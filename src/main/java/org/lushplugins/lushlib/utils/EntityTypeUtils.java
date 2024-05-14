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
        stringList.forEach(entityTypeRaw -> entityTypes.addAll(from(entityTypeRaw)));
        return entityTypes;
    }

    @NotNull
    public static Collection<EntityType> from(String string) {
        if (string.charAt(0) == '#') {
            Tag<EntityType> tag = Bukkit.getTag(Tag.REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft(string.substring(1)), EntityType.class);
            return tag != null ? tag.getValues() : Collections.emptyList();
        } else {
            EntityType entityType = Registry.ENTITY_TYPE.get(NamespacedKey.minecraft(string));
            return entityType != null ? List.of(entityType) : Collections.emptyList();
        }
    }
}
