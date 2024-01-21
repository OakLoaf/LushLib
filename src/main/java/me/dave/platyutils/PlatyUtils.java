package me.dave.platyutils;

import me.dave.platyutils.listener.InventoryListener;
import me.dave.platyutils.listener.PlayerListener;
import me.dave.platyutils.manager.GuiManager;
import me.dave.platyutils.manager.Manager;
import me.dave.platyutils.plugin.SpigotPlugin;
import org.jetbrains.annotations.NotNull;
import space.arim.morepaperlib.MorePaperLib;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class PlatyUtils {
    private static boolean enabled = false;

    private static SpigotPlugin plugin = null;
    private static Logger logger = null;
    private static MorePaperLib morePaperLib = null;

    private static ConcurrentHashMap<Class<? extends Manager>, Manager> managers;

    public static void enable(@NotNull SpigotPlugin plugin) {
        enabled = true;

        PlatyUtils.plugin = plugin;
        logger = plugin.getLogger();

        registerManager(new GuiManager());

        new InventoryListener().registerListeners();
        new PlayerListener().registerListeners();

        logger.info("Successfully enabled PlatyUtils");
    }

    public static void disable() {
        if (managers != null) {
            managers.keySet().forEach(PlatyUtils::unregisterManager);
            managers.clear();
            managers = null;
        }

        if (morePaperLib != null) {
            morePaperLib.scheduling().cancelGlobalTasks();
            morePaperLib = null;
        }

        logger.info("Successfully disabled PlatyUtils");
        logger = null;
        plugin = null;

        enabled = false;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static SpigotPlugin getPlugin() {
        return plugin;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static MorePaperLib getMorePaperLib() {
        if (isEnabled()) {
            if (morePaperLib == null) {
                morePaperLib = new MorePaperLib(plugin);
            }

            return morePaperLib;
        } else {
            throw new IllegalStateException("PlatyUtils is not enabled");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Manager> Optional<T> getManager(Class<T> clazz) {
        return managers.containsKey(clazz) ? Optional.of((T) managers.get(clazz)) : Optional.empty();
    }

    public static void registerManager(@NotNull Manager... managers) {
        if (PlatyUtils.managers == null) {
            PlatyUtils.managers = new ConcurrentHashMap<>();
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
