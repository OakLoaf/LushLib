package org.lushplugins.lushlib.jackson.serializer;

import org.lushplugins.lushlib.utils.IntRange;

public class IntRangeSerializer extends TextSerializer<IntRange> {

    @Override
    public String serialize(IntRange intRange) {
        return intRange.toString();
    }
}
