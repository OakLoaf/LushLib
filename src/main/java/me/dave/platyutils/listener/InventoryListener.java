package me.dave.platyutils.listener;

import me.dave.platyutils.PlatyUtils;
import me.dave.platyutils.gui.inventory.Gui;
import me.dave.platyutils.manager.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        PlatyUtils.getManager(GuiManager.class).ifPresent(guiManager -> {
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

        PlatyUtils.getManager(GuiManager.class).ifPresent(guiManager -> {
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

        PlatyUtils.getManager(GuiManager.class).ifPresent(guiManager -> {
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

        PlatyUtils.getManager(GuiManager.class).ifPresent(guiManager -> {
            Gui gui = guiManager.getGui(playerUUID);
            if (gui == null || !player.getOpenInventory().getTopInventory().equals(gui.getInventory())) {
                return;
            }

            gui.onDrag(event);
        });
    }
}
