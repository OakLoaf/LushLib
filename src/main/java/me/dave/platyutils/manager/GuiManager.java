package me.dave.platyutils.manager;

import me.dave.platyutils.gui.inventory.ChestGui;

import java.util.HashMap;
import java.util.UUID;

public class GuiManager extends Manager {
    private HashMap<UUID, ChestGui> playerInventoryMap = null;

    @Override
    public void onEnable() {
        playerInventoryMap = new HashMap<>();
    }

    @Override
    public void onDisable() {

    }

    public ChestGui getGui(UUID uuid) {
        return playerInventoryMap.get(uuid);
    }

    public void addInventory(UUID uuid, ChestGui gui) {
        playerInventoryMap.put(uuid, gui);
    }

    public void removeInventory(UUID uuid) {
        playerInventoryMap.remove(uuid);
    }
}
