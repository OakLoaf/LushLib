package org.lushplugins.lushlib.gui.inventory;

import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.gui.button.Button;
import org.lushplugins.lushlib.gui.button.SimpleItemButton;
import org.lushplugins.lushlib.utils.DisplayItemStack;

import java.util.List;

/**
 * @see GuiLayer
 */
@Deprecated
public class GuiFormat extends GuiLayer {

    public GuiFormat() {
        super();
    }

    public GuiFormat(String... rows) {
        super(rows);
    }

    public GuiFormat(List<String> rows) {
        super(rows);
    }

    /**
     * @see GuiLayer#getButton(char)
     */
    @Deprecated
    public @Nullable DisplayItemStack getItemReference(char character) {
        if (this.getButtonMap().get(character) instanceof SimpleItemButton button) {
            return button.getItem();
        } else {
            return null;
        }
    }

    /**
     * @see GuiLayer#setButton(char, Button)
     */
    @Deprecated
    public void setItemReference(char character, DisplayItemStack item) {
        this.getButtonMap().put(character, new SimpleItemButton(item, (ignored) -> {}));
    }
}
