package me.dave.platyutils.manager;

import me.dave.platyutils.gui.inventory.Gui;

import java.util.HashMap;
import java.util.UUID;

public class GuiManager extends Manager {
    private HashMap<UUID, Gui> playerGuiMap = null;

    @Override
    public void onEnable() {
        playerGuiMap = new HashMap<>();
    }

    @Override
    public void onDisable() {
        if (playerGuiMap != null) {
            playerGuiMap.values().forEach(Gui::close);
            playerGuiMap.clear();
            playerGuiMap = null;
        }
    }

    public Gui getGui(UUID uuid) {
        return playerGuiMap.get(uuid);
    }

    public void addGui(UUID uuid, Gui gui) {
        playerGuiMap.put(uuid, gui);
    }

    public void removeGui(UUID uuid) {
        playerGuiMap.remove(uuid);
    }
}
