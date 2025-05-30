package org.lushplugins.lushlib.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.utils.DisplayItemStack;

import java.util.function.Consumer;

public class SimpleItemButton extends ItemButton {
    private DisplayItemStack item;

    public SimpleItemButton(DisplayItemStack item) {
        super();
        this.item = item;
    }

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
    public ItemStack getItemStack(@Nullable Player player) {
        return item.asItemStack(player);
    }
}
