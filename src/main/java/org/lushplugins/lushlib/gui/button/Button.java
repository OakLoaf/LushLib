package org.lushplugins.lushlib.gui.button;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

// TODO: Look into Lamp's Factory implementations for Buttons
public class Button {
    private Consumer<InventoryClickEvent> onClick;

    public Button() {
        this.onClick = null;
    }

    public Button(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    /**
     * @see Button#onClick(InventoryClickEvent)
     */
    @Deprecated(forRemoval = true)
    public void click(InventoryClickEvent event) {
        this.onClick(event);
    }

    @Deprecated
    public void onClick(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    public void onClick(InventoryClickEvent event) {
        if (this.onClick != null) {
            this.onClick.accept(event);
        }
    }
}
