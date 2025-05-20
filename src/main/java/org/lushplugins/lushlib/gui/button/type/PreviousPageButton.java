package org.lushplugins.lushlib.gui.button.type;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.lushlib.gui.button.GuiButton;
import org.lushplugins.lushlib.gui.inventory.Gui;
import org.lushplugins.lushlib.gui.inventory.PagedGui;
import org.lushplugins.lushlib.utils.DisplayItemStack;

public class PreviousPageButton extends GuiButton {

    public PreviousPageButton(DisplayItemStack item) {
        super(item);
    }

    @Override
    public void onClick(Gui gui, InventoryClickEvent event) {
        if (!(gui instanceof PagedGui pagedGui)) {
            return;
        }

        pagedGui.previousPage();
    }
}
