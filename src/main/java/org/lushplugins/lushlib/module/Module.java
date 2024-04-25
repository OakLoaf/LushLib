package org.lushplugins.lushlib.module;

import org.lushplugins.lushlib.LushLib;
import org.lushplugins.lushlib.listener.EventListener;

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
                LushLib.getInstance().getPlugin().log(Level.SEVERE, "Error when enabling module '" + id + "' at:");
                e.printStackTrace();
                LushLib.getInstance().getPlugin().log(Level.SEVERE, "Disabling module '" + id + "'");
                disable();
                return;
            }

            if (enabled && this instanceof EventListener listener) {
                listener.registerListeners();
            }

            LushLib.getInstance().getPlugin().log(Level.INFO, "Successfully enabled module '" + id + "'");
        }
    }

    protected void onDisable() {}

    public final void disable() {
        this.enabled = false;

        try {
            this.onDisable();
        } catch (Throwable e) {
            LushLib.getInstance().getPlugin().log(Level.SEVERE, "Error when disabling module '" + id + "' at:");
            e.printStackTrace();
        }

        if (!enabled && this instanceof EventListener listener) {
            listener.unregisterListeners();
        }
    }

    public final void reload() {
        if (enabled) {
            disable();
        }

        enable();
    }
}