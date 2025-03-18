package org.lushplugins.lushlib.serializer;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.lushplugins.lushlib.utils.IntRange;
import org.lushplugins.lushlib.utils.LushLogger;

import java.io.IOException;

public class IntRangeDeserializer extends JsonDeserializer<IntRange> {

    @Override
    public IntRange deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Object obj = jsonParser.currentValue();

        IntRange deserialized;
        try {
            deserialized = IntRange.valueOf(obj);
        } catch (NumberFormatException e) {
            deserialized = null;

            JsonLocation location = jsonParser.currentLocation();
            LushLogger.getLogger().severe("%s cannot deserialize %s at: %s:%s".formatted(
                getClass().getSimpleName().toLowerCase(),
                obj,
                location.getLineNr(),
                location.getColumnNr()
            ));
        }

        return deserialized;
    }
}
