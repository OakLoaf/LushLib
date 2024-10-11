package org.lushplugins.lushlib.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.lushlib.utils.converter.YamlConverter;

public class YamlUtils {

    /**
     * @see YamlConverter#getDisplayItemBuilder(ConfigurationSection)
     */
    @Deprecated(forRemoval = true)
    public static DisplayItemStack.Builder getDisplayItemBuilder(ConfigurationSection section) {
        return YamlConverter.getDisplayItemBuilder(section);
    }

    /**
     * @see YamlConverter#getDisplayItem(ConfigurationSection)
     */
    @Deprecated(forRemoval = true)
    public static DisplayItemStack getDisplayItem(ConfigurationSection section) {
        return YamlConverter.getDisplayItem(section);
    }

    /**
     * @see YamlConverter#setDisplayItem(ConfigurationSection, DisplayItemStack)
     */
    @Deprecated(forRemoval = true)
    public static void setDisplayItem(ConfigurationSection section, DisplayItemStack item) {
        YamlConverter.setDisplayItem(section, item);
    }
}
