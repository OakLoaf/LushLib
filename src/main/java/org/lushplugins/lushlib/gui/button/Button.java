package org.lushplugins.lushlib.gui.button;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class Button {
    private Consumer<InventoryClickEvent> onClick;

    public Button(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    public void click(InventoryClickEvent event) {
        onClick.accept(event);
    }

    public void onClick(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }
}
