package org.lushplugins.lushlib;

import org.lushplugins.lushlib.plugin.SpigotPlugin;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.LushLogger;

@SuppressWarnings("unused")
public final class LushLib {
    private static LushLib instance;

    private boolean enabled = false;
    private SpigotPlugin plugin = null;

    public void enable(@NotNull SpigotPlugin plugin) {
        enabled = true;

        this.plugin = plugin;
        LushLogger.setLogger(plugin.getLogger());
        LushLogger.getLogger().info("Successfully enabled LushLib");
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

    public static LushLib getInstance() {
        if (instance == null) {
            instance = new LushLib();
        }

        return instance;
    }
}
