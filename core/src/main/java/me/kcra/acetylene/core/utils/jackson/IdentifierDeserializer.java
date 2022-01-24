package me.kcra.acetylene.core.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import me.kcra.acetylene.core.utils.Identifier;

import java.io.IOException;

public class IdentifierDeserializer extends JsonDeserializer<Identifier> {
    @Override
    public Identifier deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Identifier.of(p.getValueAsString());
    }
}
