package org.lushplugins.lushlib;

import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.utils.LushLogger;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class LushLib {
    private static LushLib instance;

    private boolean enabled = false;
    private SpigotPlugin plugin = null;

    public void enable(@NotNull SpigotPlugin plugin) {
        enabled = true;

        this.plugin = plugin;
        Logger logger = plugin.getLogger();
        LushLogger.setLogger(logger);

        logger.info("Successfully enabled LushLib");
    }

    public void disable() {
        LushLogger.getLogger().info("Successfully disabled LushLib");
        plugin = null;

        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SpigotPlugin getPlugin() {
        return plugin;
    }

    public Logger getLogger() {
        return LushLogger.getLogger();
    }

    public static LushLib getInstance() {
        if (instance == null) {
            instance = new LushLib();
        }

        return instance;
    }
}
