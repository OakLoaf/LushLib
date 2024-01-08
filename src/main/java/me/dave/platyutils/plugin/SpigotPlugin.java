package me.dave.platyutils.plugin;

import me.dave.platyutils.PlatyUtils;
import me.dave.platyutils.hook.Hook;
import me.dave.platyutils.module.Module;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

    public void warn(@NotNull String message, @NotNull Throwable... throwable) {
        log(Level.WARNING, message, throwable);
    }

    public void severe(@NotNull String message, @NotNull Throwable... throwable) {
        log(Level.SEVERE, message, throwable);
    }

    public void log(@NotNull Level level, @NotNull String message, @NotNull Throwable... throwable) {
        if (throwable.length > 0) {
            getLogger().log(level, message, throwable);
        } else {
            getLogger().log(level, message);
        }
    }

    public Optional<Module> getModule(String moduleId) {
        return Optional.ofNullable(modules.get(moduleId));
    }

    public void registerModule(Module module) {
        modules.put(module.getId(), module);
    }

    public void unregisterModule(String moduleId) {
        modules.get(moduleId).disable();
        modules.remove(moduleId);
    }

    public void unregisterAllModules() {
        modules.values().forEach(Module::disable);
        modules.clear();
    }

    public Optional<Hook> getHook(String hookId) {
        return Optional.ofNullable(hooks.get(hookId));
    }

    public void registerHook(Hook hook) {
        hooks.put(hook.getId(), hook);
    }

    public void unregisterHook(String hookId) {
        hooks.get(hookId).disable();
        hooks.remove(hookId);
    }

    public void unregisterAllHooks() {
        hooks.values().forEach(Hook::disable);
        hooks.clear();
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
}
