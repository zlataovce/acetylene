package me.kcra.acetylene.core;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TypedMappingFile(List<TypedClassMapping> classes) {
    public @Nullable TypedClassMapping clazz(String original) {
        return classes.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable TypedClassMapping mappedClazz(String mapped) {
        return classes.stream().filter(e -> e.mappings().containsValue(mapped)).findFirst().orElse(null);
    }
}
