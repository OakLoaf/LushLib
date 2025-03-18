package org.lushplugins.lushlib.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.lushplugins.lushlib.LushLogger;

import java.io.IOException;

public abstract class TextSerializer<T> extends JsonSerializer<T> {

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String serialized = serialize(t);

        if (serialized == null) {
            LushLogger.getLogger().severe("%s cannot serialize %s".formatted(
                getClass().getSimpleName().toLowerCase(),
                t
            ));
        }

        jsonGenerator.writeString(serialized);
    }

    public abstract String serialize(T t);
}
