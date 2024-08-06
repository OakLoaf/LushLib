package org.lushplugins.lushlib.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.lushplugins.lushlib.utils.DisplayItemStack;

import java.util.function.Consumer;

public class SimpleItemButton extends ItemButton {
    private DisplayItemStack item;

    public SimpleItemButton(DisplayItemStack item, Consumer<InventoryClickEvent> onClick) {
        super(onClick);
        this.item = item;
    }

    public DisplayItemStack getItem() {
        return item;
    }

    public void setItem(DisplayItemStack item) {
        this.item = item;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        return item.asItemStack();
    }
}
