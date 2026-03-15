package org.lushplugins.lushlib.utils.plugin;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.common.logger.LushLogger;
import org.lushplugins.lushlib.utils.FilenameUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

@SuppressWarnings("unused")
public abstract class SpigotPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        LushLogger.setLogger(this);
    }

    public void ifPluginPresent(String pluginName, Runnable runnable) {
        if (getServer().getPluginManager().getPlugin(pluginName) != null) {
            getLogger().info("Found plugin \"%s\". Enabling %s support."
                .formatted(pluginName, pluginName));
            runnable.run();
        }
    }

    public void ifPluginEnabled(String pluginName, Runnable runnable) {
        if (getServer().getPluginManager().getPlugin(pluginName) instanceof JavaPlugin plugin && plugin.isEnabled()) {
            getLogger().info("Found plugin \"%s\". Enabling %s support."
                .formatted(pluginName, pluginName));
            runnable.run();
        }
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    public boolean callEvent(Event event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event instanceof Cancellable cancellable) {
            return !cancellable.isCancelled();
        } else {
            return true;
        }
    }

    public NamespacedKey namespacedKey(String key) {
        return new NamespacedKey(this, key);
    }

    public FileConfiguration getConfigResource(String path) {
        return YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), path));
    }

    /**
     * @param path Path within resources folder
     */
    public void saveDefaultResource(String path) {
        if (!new File(this.getDataFolder(), path).exists()) {
            saveResource(path, false);
            this.getLogger().info("File Created: " + path);
        }
    }

    public void backupFile(File file) {
        File parent = file.getParentFile();
        String name = file.getName();

        if (!file.renameTo(new File(parent, FilenameUtils.removeExtension(name) + "-old-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy--HH-mm-ss")) + ".yml"))) {
            this.getLogger().severe("Failed to rename file '%s'".formatted(name));
        }
    }

    public void debug(@NotNull String message) {
        log(Level.FINE, message);
    }

    public void debug(@NotNull String message, @NotNull Throwable throwable) {
        log(Level.FINE, message, throwable);
    }

    public void log(@NotNull Level level, @NotNull String message) {
        getLogger().log(level, message);
    }

    public void log(@NotNull Level level, @NotNull String message, @NotNull Throwable throwable) {
        getLogger().log(level, message, throwable);
    }
}
