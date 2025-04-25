package org.lushplugins.lushlib.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.LushLogger;
import org.lushplugins.lushlib.config.ConfigSection;
import org.lushplugins.lushlib.utils.converter.YamlConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

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
        if (config.isList(path)) {
            return config.getMapList(path).stream()
                .map(map -> fromMap(config, path, map))
                .toList();
        } else {
            ConfigurationSection pathSection = config.getConfigurationSection(path);
            if (pathSection == null) {
                return Collections.emptyList();
            }

            return pathSection.getValues(false).values().stream()
                .map(rawSection -> rawSection instanceof ConfigurationSection configSection ? configSection : null)
                .filter(Objects::nonNull)
                .toList();
        }
    }

    /**
     * Creates a configuration section from a map, mainly for use in map lists. Note that
     * the current path will be set to "list"
     *
     * @param parent the parent section
     * @param path the path
     * @param map the map of data
     * @return a compiled configuration section
     */
    private static ConfigurationSection fromMap(ConfigurationSection parent, String path, Map<?, ?> map) {
        return new ConfigSection(parent, path).createSection("list", map);
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

    /**
     * Load all yaml files in a directory
     * @param directory directory to read from
     */
    private List<Pair<String, YamlConfiguration>> readConfigsInDirectory(File directory) {
        List<Pair<String, YamlConfiguration>> configFiles = new ArrayList<>();

        try (
            DirectoryStream<Path> fileStream = Files.newDirectoryStream(directory.toPath(), "*.yml")
        ) {
            for (Path filePath : fileStream) {
                File file = filePath.toFile();
                configFiles.add(new Pair<>(
                    file.getName(),
                    YamlConfiguration.loadConfiguration(file)
                ));
            }
        } catch (IOException e) {
            LushLogger.getLogger().log(Level.WARNING, "Caught error whilst loading config files: ", e);
        }

        return configFiles;
    }
}
