package org.lushplugins.lushlib.serializer;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.lushplugins.lushlib.registry.RegistryUtils;

public class RegistryDeserializer<T extends Keyed> extends TextDeserializer<T> {
    private final Registry<T> registry;

    public RegistryDeserializer(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public T deserialize(String text) {
        return RegistryUtils.parseString(text, registry);
    }
}
