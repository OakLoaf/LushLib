package org.lushplugins.lushlib.gui.inventory;

import com.google.common.collect.TreeMultimap;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.utils.DisplayItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GuiFormat {
    private final List<String> rows;
    private final HashMap<Character, DisplayItemStack> items = new HashMap<>();

    public GuiFormat() {
        this.rows = new ArrayList<>();
    }

    public GuiFormat(String... rows) {
        this(Arrays.asList(rows));
    }

    public GuiFormat(List<String> rows) {
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

    public @Nullable DisplayItemStack getItemReference(char character) {
        return items.get(character);
    }

    public void setItemReference(char character, DisplayItemStack item) {
        items.put(character, item);
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
