package org.lushplugins.lushlib.registry;

import org.bukkit.*;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public record TagType<T extends Keyed>(Registry<T> registry, String[] registries, Class<T> dataType) {
    private static final HashMap<Registry<?>, TagType<?>> TAG_TYPES = new HashMap<>();

    public static final TagType<EntityType> ENTITY_TYPES = new TagType<>(Registry.ENTITY_TYPE, new String[]{Tag.REGISTRY_ENTITY_TYPES}, EntityType.class);
    public static final TagType<Fluid> FLUIDS = new TagType<>(Registry.FLUID, new String[]{Tag.REGISTRY_FLUIDS}, Fluid.class);
    public static final TagType<Material> MATERIALS = new TagType<>(Registry.MATERIAL, new String[]{Tag.REGISTRY_BLOCKS, Tag.REGISTRY_ITEMS}, Material.class);

    static {
        TAG_TYPES.put(Registry.ENTITY_TYPE, TagType.ENTITY_TYPES);
        TAG_TYPES.put(Registry.FLUID, TagType.FLUIDS);
        TAG_TYPES.put(Registry.MATERIAL, TagType.MATERIALS);
    }

    public static <T extends Keyed> TagType<T> get(Registry<T> registry) {
        TagType<?> tagType = TAG_TYPES.get(registry);
        if (tagType != null) {
            @SuppressWarnings("unchecked")
            TagType<T> typedTagType = (TagType<T>) tagType;
            return typedTagType;
        } else {
            return null;
        }
    }

    public static <T extends Keyed> Tag<T> getTag(NamespacedKey namespacedKey, Registry<T> registry) {
        TagType<T> tagType = get(registry);
        return tagType != null ? tagType.getTag(namespacedKey) : null;
    }

    public Tag<T> getTag(NamespacedKey namespacedKey) {
        for (String tagRegistry : registries()) {
            Tag<T> tag = Bukkit.getTag(tagRegistry, namespacedKey, dataType());
            if (tag != null) {
                return tag;
            }
        }

        return null;
    }
}
