package org.lushplugins.lushlib.listener;

import org.lushplugins.lushlib.LushLib;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface EventListener extends Listener {

    default void registerListeners() {
        LushLib.getInstance().getPlugin().getServer().getPluginManager().registerEvents(this, LushLib.getInstance().getPlugin());
    }

    default void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }
}
