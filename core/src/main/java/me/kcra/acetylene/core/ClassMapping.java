package me.kcra.acetylene.core;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record ClassMapping(String original, String mapped, @Unmodifiable List<DescriptableMapping> fields,
                           @Unmodifiable List<DescriptableMapping> methods) implements Mappable {
    public @Nullable DescriptableMapping field(String original) {
        return fields.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable DescriptableMapping mappedField(String mapped) {
        return fields.stream().filter(e -> e.mapped().equals(mapped)).findFirst().orElse(null);
    }

    public @Nullable DescriptableMapping method(String original) {
        return methods.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable DescriptableMapping mappedMethod(String mapped) {
        return methods.stream().filter(e -> e.mapped().equals(mapped)).findFirst().orElse(null);
    }
}
