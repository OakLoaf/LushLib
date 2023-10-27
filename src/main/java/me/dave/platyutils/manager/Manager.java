package me.dave.platyutils.manager;

public abstract class Manager {
    private boolean enabled = false;

    public abstract void onEnable();
    public abstract void onDisable();

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        if (!isEnabled()) {
            this.enabled = true;
            onEnable();
        } else {
            throw new IllegalStateException("This Manager is already enabled");
        }
    }

    public void disable() {
        if (isEnabled()) {
            this.enabled = false;
            onDisable();
        }
    }
}
