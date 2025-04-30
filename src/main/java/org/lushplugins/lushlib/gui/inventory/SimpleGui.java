package org.lushplugins.lushlib.gui.inventory;

import com.google.common.collect.TreeMultimap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.lushlib.gui.button.Button;

import java.util.Collections;
import java.util.List;

public class SimpleGui extends Gui {

    public SimpleGui(@NotNull List<GuiFormat> layers, String title, Player player) {
        super(layers.get(0).getSize(), title, player);
        layers.forEach(this::applyLayer);
    }

    public SimpleGui(GuiFormat layer, String title, Player player) {
        this(Collections.singletonList(layer), title, player);
    }

    public void applyLayer(GuiFormat layer) {
        TreeMultimap<Character, Integer> slotMap = layer.getSlotMap();
        for (char character : slotMap.keySet()) {
            Button button = layer.getButton(character);
            if (button == null) {
                continue;
            }

            slotMap.get(character).forEach(slot -> this.addButton(slot, button));
        }
    }
}
