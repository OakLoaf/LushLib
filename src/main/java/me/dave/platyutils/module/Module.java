package me.dave.platyutils.module;

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
        this.enabled = true;
    }

    protected void onDisable() {}

    public final void disable() {
        this.onDisable();
        this.enabled = false;
    }

    public final void reload() {
        if (enabled) {
            disable();
        }

        enable();
    }
}