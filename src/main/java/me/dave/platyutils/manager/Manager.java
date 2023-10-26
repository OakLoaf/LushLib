package me.dave.platyutils.manager;

public abstract class Manager {
    private boolean enabled = false;

    public abstract void onEnable();
    public abstract void onDisable();

    public void enable() {
        this.enabled = true;
        onEnable();
    }

    public void disable() {
        this.enabled = false;
        onDisable();
    }
}
