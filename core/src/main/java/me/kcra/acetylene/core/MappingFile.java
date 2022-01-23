package me.kcra.acetylene.core;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public record MappingFile(@Unmodifiable List<ClassMapping> classes) {
    public @Nullable ClassMapping clazz(String original) {
        return classes.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable ClassMapping mappedClazz(String mapped) {
        return classes.stream().filter(e -> e.mapped().equals(mapped)).findFirst().orElse(null);
    }
}
