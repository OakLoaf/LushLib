package org.lushplugins.lushlib.gui.inventory;

import com.google.common.collect.TreeMultimap;
import org.lushplugins.lushlib.gui.button.Button;

import java.util.*;

public class GuiLayer {
    private final List<String> rows;
    private final HashMap<Character, Button> buttons = new HashMap<>();

    public GuiLayer() {
        this.rows = new ArrayList<>();
    }

    public GuiLayer(String... rows) {
        this(Arrays.asList(rows));
    }

    public GuiLayer(List<String> rows) {
        this.rows = new ArrayList<>();
        for (String row : rows) {
            if (row.length() != 9) {
                throw new IllegalArgumentException("Rows should be 9 characters long.");
            }

            this.rows.add(row);
        }
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRow(int row, String format) {
        if (format.length() != 9) {
            throw new IllegalArgumentException("Rows should be 9 characters long.");
        }

        rows.set(row, format);
    }

    public void addRow(String format) {
        if (format.length() != 9) {
            throw new IllegalArgumentException("All rows should be 9 characters long.");
        }

        rows.add(format);
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getSize() {
        return rows.size() * 9;
    }

    public Map<Character, Button> getButtonMap() {
        return buttons;
    }

    public Button getButton(char character) {
        return buttons.get(character);
    }

    public void setButton(char character, Button button) {
        buttons.put(character, button);
    }

    public TreeMultimap<Character, Integer> getSlotMap() {
        TreeMultimap<Character, Integer> slotMap = TreeMultimap.create();
        for (int slot = 0; slot < getRowCount() * 9; slot++) {
            char character = getCharAt(slot);
            slotMap.put(character, slot);
        }

        return slotMap;
    }

    public char getCharAt(int slot) {
        int currRow = (int) Math.ceil((slot + 1) / 9F) - 1;
        int slotInRow = slot % 9;

        return rows.get(currRow).charAt(slotInRow);
    }

    public int getCharCount(char character) {
        int count = 0;

        for (String row : rows) {
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == character) {
                    count++;
                }
            }
        }

        return count;
    }
}
