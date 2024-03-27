package me.dave.lushlib.utils;

import java.util.Random;

@SuppressWarnings("unused")
public class IntRange {
    private static final Random RANDOM = new Random();
    private final int min;
    private final int max;

    /**
     * @param min Minimum value (inclusive)
     * @param max Maximum value in range (inclusive)
     */
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @param value Minimum and maximum value (inclusive)
     */
    public IntRange(int value) {
        this.min = value;
        this.max = value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean inRange(int number) {
        return number >= min && number <= max;
    }

    public int next() {
        if (min == max) {
            return min;
        }

        return RANDOM.nextInt(min, max + 1);
    }

    public static IntRange parseIntRange(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("Cannot parse null string");
        }

        String[] values = s.split("-");
        switch (values.length) {
            case 1 -> {
                int value = Integer.parseInt(values[0]);
                return new IntRange(value, value);
            }
            case 2 -> {
                return new IntRange(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }
            default -> throw new NumberFormatException("Cannot parse invalid range format");
        }
    }

    public static IntRange valueOf(Object object) throws NumberFormatException {
        String s;
        try {
            s = (String) object;
        } catch (ClassCastException e) {
            return new IntRange((int) object);
        }

        if (s == null) {
            throw new NumberFormatException("Cannot parse null string");
        }

        String[] values = s.split("-");
        switch (values.length) {
            case 1 -> {
                int value = Integer.parseInt(values[0]);
                return new IntRange(value, value);
            }
            case 2 -> {
                return new IntRange(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }
            default -> throw new NumberFormatException("Cannot parse invalid range format");
        }
    }

    @Override
    public String toString() {
        return min + "-" + max;
    }
}
