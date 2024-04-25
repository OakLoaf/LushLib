package org.lushplugins.lushlib.manager;

public abstract class Manager {
    private boolean enabled = false;

    public void onEnable() {}
    public void onDisable() {}

    public final boolean isEnabled() {
        return enabled;
    }

    public final void enable() {
        if (!isEnabled()) {
            this.enabled = true;
            onEnable();
        } else {
            throw new IllegalStateException("This Manager is already enabled");
        }
    }

    public final void disable() {
        if (isEnabled()) {
            this.enabled = false;
            onDisable();
        }
    }
}
