package org.lushplugins.lushlib.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class ItemButton extends Button {

    public ItemButton() {
        super();
    }

    public ItemButton(Consumer<InventoryClickEvent> onClick) {
        super(onClick);
    }

    public abstract ItemStack getItemStack(@Nullable Player player);
}
