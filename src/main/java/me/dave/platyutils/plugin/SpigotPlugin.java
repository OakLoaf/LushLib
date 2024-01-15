package me.dave.platyutils.plugin;

import me.dave.platyutils.PlatyUtils;
import me.dave.platyutils.hook.Hook;
import me.dave.platyutils.module.Module;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@SuppressWarnings("unused")
public abstract class SpigotPlugin extends JavaPlugin {
    protected ConcurrentHashMap<String, Module> modules = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, Hook> hooks = new ConcurrentHashMap<>();

    public void debug(@NotNull String message, @NotNull Throwable... throwable) {
        log(Level.FINE, message, throwable);
    }

    public void log(@NotNull Level level, @NotNull String message, @NotNull Throwable... throwable) {
        if (throwable.length > 0) {
            getLogger().log(level, message, throwable);
        } else {
            getLogger().log(level, message);
        }
    }

    public Collection<Module> getModules() {
        return modules.values();
    }

    public Optional<Module> getModule(String moduleId) {
        return Optional.ofNullable(modules.get(moduleId));
    }

    public void registerModule(Module module) {
        if (modules.containsKey(module.getId())) {
            log(Level.SEVERE, "Failed to register module with id '" + module.getId() + "', a module with this id is already running");
            return;
        }

        modules.put(module.getId(), module);
    }

    public void unregisterModule(String moduleId) {
        if (modules.containsKey(moduleId)) {
            modules.get(moduleId).disable();
            modules.remove(moduleId);
        }
    }

    public void unregisterAllModules() {
        modules.values().forEach(Module::disable);
        modules.clear();
    }

    public Collection<Hook> getHooks() {
        return hooks.values();
    }

    public Optional<Hook> getHook(String hookId) {
        return Optional.ofNullable(hooks.get(hookId));
    }

    public void registerHook(Hook hook) {
        hooks.put(hook.getId(), hook);
    }

    public void unregisterHook(String hookId) {
        if (hooks.containsKey(hookId)) {
            hooks.get(hookId).disable();
            hooks.remove(hookId);
        }
    }

    public void unregisterAllHooks() {
        hooks.values().forEach(Hook::disable);
        hooks.clear();
    }

    protected void addHook(String pluginName, Runnable runnable) {
        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin(pluginName) instanceof JavaPlugin hookPlugin && hookPlugin.isEnabled()) {
            getLogger().info("Found plugin \"" + pluginName +"\". Enabling " + pluginName + " support.");
            runnable.run();
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

    /**
     * @param path Path within resources folder
     */
    public void saveDefaultResource(String path) {
        if (!new File(PlatyUtils.getPlugin().getDataFolder(), path).exists()) {
            saveResource(path, false);
            PlatyUtils.getPlugin().getLogger().info("File Created: " + path);
        }
    }

    public void backupFile(File file) {
        File parent = file.getParentFile();
        String name = file.getName();

        if (!file.renameTo(new File(parent, FilenameUtils.removeExtension(name) + "-old-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy--HH-mm-ss")) + ".yml"))) {
            this.getLogger().severe("Failed to rename file '" + name + "'");
        }
    }
}
