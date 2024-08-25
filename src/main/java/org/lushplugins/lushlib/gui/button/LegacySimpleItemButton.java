package org.lushplugins.lushlib.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.lushplugins.lushlib.utils.SimpleItemStack;

import java.util.function.Consumer;

@Deprecated
public class LegacySimpleItemButton extends ItemButton {
    private SimpleItemStack item;

    public LegacySimpleItemButton(SimpleItemStack item, Consumer<InventoryClickEvent> onClick) {
        super(onClick);
        this.item = item;
    }

    public SimpleItemStack getItem() {
        return item;
    }

    public void setItem(SimpleItemStack item) {
        this.item = item;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        return item.asItemStack();
    }
}
