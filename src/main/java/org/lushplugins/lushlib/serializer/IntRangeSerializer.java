package org.lushplugins.lushlib.serializer;

import org.lushplugins.lushlib.utils.IntRange;

public class IntRangeSerializer extends TextSerializer<IntRange> {

    @Override
    public String serialize(IntRange intRange) {
        return intRange.toString();
    }
}
