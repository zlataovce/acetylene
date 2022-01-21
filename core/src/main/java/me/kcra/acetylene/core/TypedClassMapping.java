package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record TypedClassMapping(String original, Map<Identifier, String> mappings,
                                Map<Identifier, DescriptableMapping> fields,
                                Map<Identifier, DescriptableMapping> methods) implements Mappable {
    @Override
    public @NotNull String mapped() {
        return String.join(",", mappings.values());
    }

    public @Nullable String mapped(Identifier type) {
        return mappings.get(type);
    }

    public @Nullable DescriptableMapping field(Identifier type) {
        return fields.get(type);
    }

    public @Nullable DescriptableMapping method(Identifier type) {
        return methods.get(type);
    }
}
