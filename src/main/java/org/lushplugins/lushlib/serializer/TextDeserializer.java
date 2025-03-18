package org.lushplugins.lushlib.serializer;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.lushplugins.lushlib.LushLogger;

import java.io.IOException;

public abstract class TextDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        T deserialized = deserialize(text);

        if (deserialized == null) {
            JsonLocation location = jsonParser.currentLocation();
            LushLogger.getLogger().severe("%s cannot deserialize %s at: %s:%s".formatted(
                getClass().getSimpleName(),
                text,
                location.getLineNr(),
                location.getColumnNr()
            ));
        }

        return deserialized;
    }

    public abstract T deserialize(String text);
}
