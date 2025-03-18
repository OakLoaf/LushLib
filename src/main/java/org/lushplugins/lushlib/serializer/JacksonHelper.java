package org.lushplugins.lushlib.serializer;

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

public class JacksonHelper {

    public static ObjectMapper addCustomSerializers(ObjectMapper mapper) {
        return mapper.registerModule(new SimpleModule()
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
