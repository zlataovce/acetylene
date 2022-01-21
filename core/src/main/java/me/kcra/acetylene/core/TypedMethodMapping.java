package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;

import java.util.Map;

public record TypedMethodMapping(String original, Map<Identifier, String> mappings) implements Descriptable {
    @Override
    public String mapped() {
        return String.join(",", mappings.values());
    }

    public String mapped(Identifier type) {
        return mappings.get(type);
    }
}
