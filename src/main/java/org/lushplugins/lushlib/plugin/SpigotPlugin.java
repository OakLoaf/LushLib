package org.lushplugins.lushlib.plugin;

import org.lushplugins.lushlib.LushLib;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.hook.Hook;
import org.lushplugins.lushlib.manager.Manager;
import org.lushplugins.lushlib.module.Module;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private ConcurrentHashMap<Class<? extends Manager>, Manager> managers = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, Hook> hooks = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();

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
            log(Level.SEVERE, "Failed to register module with id '" + module.getId() + "', a module with this id is already registered");
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

    @SuppressWarnings("unchecked")
    public <T extends Manager> Optional<T> getManager(Class<T> clazz) {
        return managers.containsKey(clazz) ? Optional.of((T) managers.get(clazz)) : Optional.empty();
    }

    public void registerManager(@NotNull Manager... managers) {
        if (this.managers == null) {
            this.managers = new ConcurrentHashMap<>();
        }

        for (Manager manager : managers) {
            this.managers.put(manager.getClass(), manager);
            manager.enable();
        }
    }

    public void unregisterManager(Class<? extends Manager> clazz) {
        Manager manager = managers.get(clazz);
        if (manager != null) {
            manager.disable();
            managers.remove(clazz);
        }
    }

    public Collection<Hook> getHooks() {
        return hooks.values();
    }

    public Optional<Hook> getHook(String hookId) {
        return Optional.ofNullable(hooks.get(hookId));
    }

    public void registerHook(Hook hook) {
        if (hooks.containsKey(hook.getId())) {
            log(Level.SEVERE, "Failed to register hook with id '" + hook.getId() + "', a hook with this id is already registered");
            return;
        }

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

    public Command getLushCommand(String name) {
        return commands.get(name);
    }

    public void registerCommand(Command command) {
        registerCommand(command.getName(), command);
    }

    public void registerCommand(String command, CommandExecutor executor) {
        try {
            getCommand(command).setExecutor(executor);
        } catch (NullPointerException e) {
            getLogger().severe("Failed to register command '" + command + "', make sure the command has been defined in the plugin.yml");
            e.printStackTrace();
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

    public FileConfiguration getConfigResource(String path) {
        return YamlConfiguration.loadConfiguration(new File(LushLib.getInstance().getPlugin().getDataFolder(), path));
    }

    /**
     * @param path Path within resources folder
     */
    public void saveDefaultResource(String path) {
        if (!new File(LushLib.getInstance().getPlugin().getDataFolder(), path).exists()) {
            saveResource(path, false);
            LushLib.getInstance().getPlugin().getLogger().info("File Created: " + path);
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
