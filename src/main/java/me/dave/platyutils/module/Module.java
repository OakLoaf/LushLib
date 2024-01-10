package me.dave.platyutils.module;

import me.dave.platyutils.listener.EventListener;

public abstract class Module {
    private final String id;
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
        this.onEnable();
        if (this instanceof EventListener listener) {
            listener.registerListeners();
        }
        this.enabled = true;
    }

    protected void onDisable() {}

    public final void disable() {
        this.onDisable();
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