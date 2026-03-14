package org.lushplugins.lushlib.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.lushplugins.lushlib.jackson.serializer.IntRangeDeserializer;
import org.lushplugins.lushlib.jackson.serializer.IntRangeSerializer;
import org.lushplugins.lushlib.jackson.serializer.RegistryDeserializer;
import org.lushplugins.lushlib.jackson.serializer.RegistrySerializer;
import org.lushplugins.lushlib.utils.IntRange;

public class JacksonHelper {

    public static ObjectMapper addCustomSerializers(ObjectMapper mapper) {
        return mapper.registerModule(new SimpleModule()
            .addSerializer(IntRange.class, new IntRangeSerializer())
            .addDeserializer(IntRange.class, new IntRangeDeserializer())
            .addSerializer(Keyed.class, new RegistrySerializer())
            .addDeserializer(Attribute.class, new RegistryDeserializer<>(Registry.ATTRIBUTE))
            .addDeserializer(Enchantment.class, new RegistryDeserializer<>(Registry.ENCHANTMENT))
            .addDeserializer(EntityType.class, new RegistryDeserializer<>(Registry.ENTITY_TYPE))
            .addDeserializer(Material.class, new RegistryDeserializer<>(Registry.MATERIAL))
            .addDeserializer(Sound.class, new RegistryDeserializer<>(Registry.SOUNDS))
            .addDeserializer(TrimMaterial.class, new RegistryDeserializer<>(Registry.TRIM_MATERIAL))
            .addDeserializer(TrimPattern.class, new RegistryDeserializer<>(Registry.TRIM_PATTERN)));
    }
}
