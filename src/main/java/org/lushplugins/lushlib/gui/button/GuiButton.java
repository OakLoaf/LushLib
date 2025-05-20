package org.lushplugins.lushlib.gui.button;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.lushlib.LushLib;
import org.lushplugins.lushlib.gui.inventory.Gui;
import org.lushplugins.lushlib.manager.GuiManager;
import org.lushplugins.lushlib.utils.DisplayItemStack;

public abstract class GuiButton extends SimpleItemButton {

    public GuiButton(DisplayItemStack item) {
        super(item);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> {
            Gui gui = guiManager.getGui(event.getWhoClicked().getUniqueId());
            if (gui != null) {
                this.onClick(gui, event);
            }
        });
    }

    public abstract void onClick(Gui gui, InventoryClickEvent event);
}
