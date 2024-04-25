package org.beaconstudios.lushlib;

import org.beaconstudios.lushlib.plugin.SpigotPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class LushLib {
    private static LushLib instance;

    private boolean enabled = false;
    private SpigotPlugin plugin = null;
    private Logger logger = null;

    public void enable(@NotNull SpigotPlugin plugin) {
        enabled = true;

        this.plugin = plugin;
        logger = plugin.getLogger();

        logger.info("Successfully enabled LushLib");
    }

    public void disable() {
        logger.info("Successfully disabled LushLib");
        logger = null;
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
        return logger;
    }

    public static LushLib getInstance() {
        if (instance == null) {
            instance = new LushLib();
        }

        return instance;
    }
}
