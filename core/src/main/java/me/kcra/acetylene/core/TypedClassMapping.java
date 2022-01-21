package me.kcra.acetylene.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kcra.acetylene.core.utils.Identifier;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TypedClassMapping implements Mappable {
    @Getter
    private final String original;
    private final Map<Identifier, String> mappings;
    private final Map<Identifier, FieldMapping> fields;
    private final Map<Identifier, MethodMapping> methods;

    @Override
    public String mapped() {
        return String.join(",", mappings.values());
    }

    public String mapped(Identifier type) {
        return mappings.get(type);
    }
}
