package me.dave.platyutils;

import me.dave.platyutils.manager.GuiManager;
import me.dave.platyutils.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

public final class PlatyUtils {
    private static boolean enabled = false;
    private static Logger logger = null;
    private static HashMap<Class<? extends Manager>, Manager> managers;

    public static void enable(@NotNull JavaPlugin plugin) {
        enabled = true;
        logger = plugin.getLogger();

        registerManager(new GuiManager());
    }

    public static void disable() {
        if (managers != null) {
            managers.keySet().forEach(PlatyUtils::unregisterManager);
            managers.clear();
            managers = null;
        }

        enabled = false;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static Logger getLogger() {
        return logger;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Manager> Optional<T> getManager(Class<T> clazz) {
        return managers.containsKey(clazz) ? Optional.of((T) managers.get(clazz)) : Optional.empty();
    }

    public static void registerManager(@NotNull Manager... managers) {
        if (PlatyUtils.managers == null) {
            PlatyUtils.managers = new HashMap<>();
        }

        for (Manager manager : managers) {
            PlatyUtils.managers.put(manager.getClass(), manager);
            manager.enable();
        }
    }

    public static void unregisterManager(Class<? extends Manager> clazz) {
        Manager manager = managers.get(clazz);
        if (manager != null) {
            manager.disable();
            managers.remove(clazz);
        }
    }
}
