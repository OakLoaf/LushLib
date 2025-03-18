package org.lushplugins.lushlib.module;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.lushplugins.lushlib.LushLib;
import org.lushplugins.lushlib.LushLogger;

import java.util.logging.Level;

@SuppressWarnings("unused")
public abstract class Module {
    protected final String id;
    private boolean enabled = false;

    public Module(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void onEnable() {}

    public final void enable() {
        if (!this.enabled) {
            this.enabled = true;

            try {
                this.onEnable();
            } catch (Throwable e) {
                LushLogger.getLogger().log(Level.SEVERE, "Error when enabling module '" + id + "' at:");
                e.printStackTrace();
                LushLogger.getLogger().log(Level.SEVERE, "Disabling module '" + id + "'");
                disable();
                return;
            }

            if (enabled && this instanceof Listener listener) {
                LushLib.getInstance().getPlugin().getServer().getPluginManager().registerEvents(listener, LushLib.getInstance().getPlugin());
            }

            LushLogger.getLogger().log(Level.INFO, "Successfully enabled module '" + id + "'");
        }
    }

    protected void onDisable() {}

    public final void disable() {
        this.enabled = false;

        try {
            this.onDisable();
        } catch (Throwable e) {
            LushLogger.getLogger().log(Level.SEVERE, "Error when disabling module '" + id + "' at:");
            e.printStackTrace();
        }

        if (!enabled && this instanceof Listener listener) {
            HandlerList.unregisterAll(listener);
        }
    }

    public final void reload() {
        if (enabled) {
            disable();
        }

        enable();
    }
}