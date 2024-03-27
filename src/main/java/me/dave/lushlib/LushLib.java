package me.dave.lushlib;

import me.dave.lushlib.manager.Manager;
import me.dave.lushlib.plugin.SpigotPlugin;
import org.jetbrains.annotations.NotNull;
import space.arim.morepaperlib.MorePaperLib;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class LushLib {
    private static LushLib instance;

    private boolean enabled = false;
    private SpigotPlugin plugin = null;
    private Logger logger = null;
    private MorePaperLib morePaperLib = null;

    public void enable(@NotNull SpigotPlugin plugin) {
        enabled = true;

        this.plugin = plugin;
        logger = plugin.getLogger();

        logger.info("Successfully enabled LushLib");
    }

    public void disable() {
        if (morePaperLib != null) {
            morePaperLib.scheduling().cancelGlobalTasks();
            morePaperLib = null;
        }

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

    public MorePaperLib getMorePaperLib() {
        if (isEnabled()) {
            if (morePaperLib == null) {
                morePaperLib = new MorePaperLib(plugin);
            }

            return morePaperLib;
        } else {
            throw new IllegalStateException("LushLib is not enabled");
        }
    }

    public static LushLib getInstance() {
        if (instance == null) {
            instance = new LushLib();
        }

        return instance;
    }
}
