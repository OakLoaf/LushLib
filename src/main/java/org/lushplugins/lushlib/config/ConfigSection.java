package org.lushplugins.lushlib.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

public class ConfigSection extends MemorySection {

    public ConfigSection(ConfigurationSection parent, String path) {
        super(parent, path);
    }
}
