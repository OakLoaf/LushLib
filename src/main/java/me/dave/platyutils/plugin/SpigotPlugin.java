package me.dave.platyutils.plugin;

import me.dave.platyutils.PlatyUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class SpigotPlugin extends JavaPlugin {

    /**
     * @param path Path within resources folder
     */
    public void saveDefaultResource(String path) {
        if (!new File(PlatyUtils.getPlugin().getDataFolder(), path).exists()) {
            saveResource(path, false);
            PlatyUtils.getPlugin().getLogger().info("File Created: " + path);
        }
    }
}
