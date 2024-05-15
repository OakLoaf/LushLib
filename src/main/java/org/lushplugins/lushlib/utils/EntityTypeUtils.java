package org.lushplugins.lushlib.utils;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class EntityTypeUtils {

    @NotNull
    public static List<EntityType> from(List<String> stringList) {
        return TagUtils.parseStringList(stringList, TagUtils.TagType.ENTITY_TYPES);
    }

    @NotNull
    public static Collection<EntityType> from(String string) {
        return TagUtils.parseString(string, TagUtils.TagType.ENTITY_TYPES);
    }
}
