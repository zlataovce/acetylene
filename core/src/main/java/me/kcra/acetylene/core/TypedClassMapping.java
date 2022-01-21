package me.kcra.acetylene.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TypedClassMapping(String original, List<String> mappings,
                                List<TypedDescriptableMapping> fields,
                                List<TypedDescriptableMapping> methods) implements Mappable {
    @Override
    public @NotNull String mapped() {
        return String.join(",", mappings);
    }

    public @Nullable String mapped(int index) {
        return mappings.get(index);
    }

    public @Nullable TypedDescriptableMapping field(int index) {
        return fields.get(index);
    }

    public @Nullable TypedDescriptableMapping method(int index) {
        return methods.get(index);
    }
}
