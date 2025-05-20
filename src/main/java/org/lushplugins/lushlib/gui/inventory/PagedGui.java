package org.lushplugins.lushlib.gui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public abstract class PagedGui extends Gui {
    protected int page = 1;

    public PagedGui(int size, String title, Player player) {
        super(size, title, player);
    }

    public PagedGui(InventoryType inventoryType, String title, Player player) {
        super(inventoryType, title, player);
    }

    public PagedGui(@NotNull List<GuiLayer> layers, String title, Player player) {
        super(layers, title, player);
    }

    public PagedGui(GuiLayer layer, String title, Player player) {
        super(layer, title, player);
    }

    public void setPage(int page) {
        this.page = page;
        refresh();
    }

    public void nextPage() {
        setPage(++page);
    }

    public void previousPage() {
        setPage(--page);
    }
}
