package me.kcra.acetylene.core.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.kcra.acetylene.core.utils.Identifier;

import java.io.IOException;

public class IdentifierSerializer extends JsonSerializer<Identifier> {
    @Override
    public void serialize(Identifier value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.name());
    }
}
