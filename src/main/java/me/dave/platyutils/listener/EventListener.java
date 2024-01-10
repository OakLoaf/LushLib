package me.dave.platyutils.listener;

import me.dave.platyutils.PlatyUtils;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface EventListener extends Listener {

    default void registerListeners() {
        PlatyUtils.getPlugin().getServer().getPluginManager().registerEvents(this, PlatyUtils.getPlugin());
    }

    default void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }
}
