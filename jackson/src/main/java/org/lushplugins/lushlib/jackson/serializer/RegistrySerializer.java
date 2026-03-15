package org.lushplugins.lushlib.jackson.serializer;

import org.bukkit.Keyed;

public class RegistrySerializer extends TextSerializer<Keyed> {

    @Override
    public String serialize(Keyed key) {
        return key.getKey().toString();
    }
}