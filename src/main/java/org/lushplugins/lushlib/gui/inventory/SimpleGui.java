package org.lushplugins.lushlib.gui.inventory;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @see Gui
 */
@Deprecated
public class SimpleGui extends Gui {

    public SimpleGui(@NotNull List<GuiLayer> layers, String title, Player player) {
        super(layers, title, player);
    }

    public SimpleGui(GuiLayer layer, String title, Player player) {
        super(layer, title, player);
    }
}
