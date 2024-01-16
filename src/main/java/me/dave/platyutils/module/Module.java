package me.dave.platyutils.module;

import me.dave.platyutils.PlatyUtils;
import me.dave.platyutils.listener.EventListener;

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
        try {
            this.onEnable();
        } catch (Throwable e) {
            PlatyUtils.getPlugin().log(Level.SEVERE, "Error when enabling module '" + id + "' at:");
            e.printStackTrace();
            PlatyUtils.getPlugin().log(Level.SEVERE, "Disabling module '" + id + "'");
            disable();
            return;
        }

        if (this instanceof EventListener listener) {
            listener.registerListeners();
        }

        this.enabled = true;
        PlatyUtils.getPlugin().log(Level.INFO, "Successfully enabled module '" + id + "'");
    }

    protected void onDisable() {}

    public final void disable() {
        try {
            this.onDisable();
        } catch (Throwable e) {
            PlatyUtils.getPlugin().log(Level.SEVERE, "Error when disabling module '" + id + "' at:");
            e.printStackTrace();
        }

        if (this instanceof EventListener listener) {
            listener.unregisterListeners();
        }

        this.enabled = false;
    }

    public final void reload() {
        if (enabled) {
            disable();
        }

        enable();
    }
}