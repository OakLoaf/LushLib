package org.lushplugins.lushlib.serializer;

import org.lushplugins.lushlib.utils.IntRange;

public class IntRangeDeserializer extends TextDeserializer<IntRange> {

    @Override
    public IntRange deserialize(String text) {
        return IntRange.parseIntRange(text);
    }
}
