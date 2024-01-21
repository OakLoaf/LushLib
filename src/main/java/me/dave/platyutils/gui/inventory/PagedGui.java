package me.dave.platyutils.gui.inventory;

import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public abstract class PagedGui extends Gui {
    protected int page = 1;

    public PagedGui(int size, String title, Player player) {
        super(size, title, player);
    }

    public void setPage(int page) {
        this.page = page;
        recalculateContents();
    }

    public void nextPage() {
        setPage(++page);
    }

    public void previousPage() {
        setPage(--page);
    }
}
