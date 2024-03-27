package me.dave.lushlib.manager;

import me.dave.lushlib.gui.inventory.Gui;
import me.dave.lushlib.listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GuiManager extends Manager {
    private ConcurrentHashMap<UUID, Gui> playerGuiMap = null;

    @Override
    public void onEnable() {
        playerGuiMap = new ConcurrentHashMap<>();
        new InventoryListener().registerListeners();
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

    public void closeAll() {
        playerGuiMap.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.closeInventory();
            }
        });
    }
}
