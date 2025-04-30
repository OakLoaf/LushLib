package org.lushplugins.lushlib.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class DynamicItemButton extends ItemButton {
    private final Callable<ItemStack> callableItem;

    public DynamicItemButton(Callable<ItemStack> callableItem, Consumer<InventoryClickEvent> onClick) {
        super(onClick);
        this.callableItem = callableItem;
    }

    @Override
    public ItemStack getItemStack(@Nullable Player player) {
        try {
            return callableItem.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
