package org.lushplugins.lushlib.listener;

import org.lushplugins.lushlib.LushLib;
import org.lushplugins.lushlib.gui.inventory.Gui;
import org.lushplugins.lushlib.manager.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class InventoryListener implements EventListener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> {
            Gui gui = guiManager.getGui(player.getUniqueId());
            if (gui == null || !event.getInventory().equals(gui.getInventory())) {
                return;
            }

            gui.onOpen(event);
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> {
            Gui gui = guiManager.getGui(player.getUniqueId());
            if (gui == null || !event.getInventory().equals(gui.getInventory())) {
                return;
            }

            gui.onClose(event);
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> {
            Gui gui = guiManager.getGui(player.getUniqueId());
            if (gui == null) {
                return;
            }

            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory == null || !player.getOpenInventory().getTopInventory().equals(gui.getInventory())) {
                return;
            }

            gui.onClick(event);
        });
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> {
            Gui gui = guiManager.getGui(playerUUID);
            if (gui == null || !player.getOpenInventory().getTopInventory().equals(gui.getInventory())) {
                return;
            }

            gui.onDrag(event);
        });
    }
}
