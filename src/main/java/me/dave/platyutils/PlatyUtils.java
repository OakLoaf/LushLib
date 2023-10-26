package me.dave.platyutils;

import me.dave.platyutils.manager.GuiManager;
import me.dave.platyutils.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

public final class PlatyUtils {
    private static PlatyUtils instance = null;
    private static Logger logger = null;
    private HashMap<Class<? extends Manager>, Manager> managers;

    public PlatyUtils(JavaPlugin plugin) {
        instance = this;
        logger = plugin.getLogger();

        registerManager(new GuiManager());
    }

    public void shutdown() {
        if (managers != null) {
            managers.values().forEach(Manager::disable);
            managers.clear();
            managers = null;
        }

        instance = null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Manager> Optional<T> getManager(Class<T> clazz) {
        return managers.containsKey(clazz) ? Optional.of((T) managers.get(clazz)) : Optional.empty();
    }

    public void registerManager(@NotNull Manager... managers) {
        if (this.managers == null) {
            this.managers = new HashMap<>();
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

    public static PlatyUtils getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return logger;
    }
}
