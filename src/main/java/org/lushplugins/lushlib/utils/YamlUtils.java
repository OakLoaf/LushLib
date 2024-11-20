package org.lushplugins.lushlib.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.utils.converter.YamlConverter;

import java.util.*;
import java.util.stream.Collectors;

public class YamlUtils {

    /**
     * Gets the requested list of String by path.
     * @param config config section
     * @param path path of the list to get
     * @return requested list of String by path.
     */
    public static @NotNull List<String> getStringList(ConfigurationSection config, String path) {
        if (!config.isList(path)) {
            String key = config.getString(path);
            return key != null ? List.of(key) : Collections.emptyList();
        } else {
            return config.getStringList(path);
        }
    }

    /**
     * Get a list of configuration sections by path
     * @param config config section
     * @param path path of the list to get
     * @return requested list of configuration sections by path
     */
    public static List<ConfigurationSection> getConfigurationSections(ConfigurationSection config, String path) {
        Collection<Object> rawSections;
        if (config.isList(path)) {
            rawSections = config.getMapList(path).stream()
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toList());
        } else {
            ConfigurationSection pathSection = config.getConfigurationSection(path);
            if (pathSection == null) {
                return Collections.emptyList();
            }

            rawSections = pathSection.getValues(false).values();
        }

        return rawSections.stream()
            .map(rawSection -> rawSection instanceof ConfigurationSection configSection ? configSection : null)
            .filter(Objects::nonNull)
            .toList();
    }

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
