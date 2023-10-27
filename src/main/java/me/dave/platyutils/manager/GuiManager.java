package me.dave.platyutils.manager;

import me.dave.platyutils.gui.inventory.ChestGui;

import java.util.HashMap;
import java.util.UUID;

public class GuiManager extends Manager {
    private HashMap<UUID, ChestGui> playerGuiMap = null;

    @Override
    public void onEnable() {
        playerGuiMap = new HashMap<>();
    }

    @Override
    public void onDisable() {
        if (playerGuiMap != null) {
            playerGuiMap.values().forEach(ChestGui::close);
            playerGuiMap.clear();
            playerGuiMap = null;
        }
    }

    public ChestGui getGui(UUID uuid) {
        return playerGuiMap.get(uuid);
    }

    public void addGui(UUID uuid, ChestGui gui) {
        playerGuiMap.put(uuid, gui);
    }

    public void removeGui(UUID uuid) {
        playerGuiMap.remove(uuid);
    }
}
