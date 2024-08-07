package org.lushplugins.lushlib.gui.inventory;

import com.google.common.collect.TreeMultimap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lushplugins.lushlib.utils.DisplayItemStack;

public class SimpleGui extends Gui {
    private final GuiFormat format;

    public SimpleGui(GuiFormat format, String title, Player player) {
        super(format.getSize(), title, player);
        this.format = format;
    }

    @Override
    public void refresh() {
        super.refresh();

        TreeMultimap<Character, Integer> slotMap = format.getSlotMap();
        for (char character : slotMap.keySet()) {
            DisplayItemStack displayItem = format.getItemReference(character);
            if (displayItem == null) {
                continue;
            }

            ItemStack itemStack = displayItem.asItemStack(this.getPlayer());
            slotMap.get(character).forEach(slot -> this.getInventory().setItem(slot, itemStack));
        }
    }
}
