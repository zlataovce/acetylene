package me.kcra.acetylene.core;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record TypedMappingFile(List<TypedClassMapping> classes) {
    public @Nullable TypedClassMapping mapping(String original) {
        return classes.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable TypedClassMapping mappedClass(String mapped) {
        return classes.stream().filter(e -> e.mappings().contains(mapped)).findFirst().orElse(null);
    }

    public @Nullable TypedClassMapping mappedClass(String... mapped) {
        return mappedClass(Arrays.asList(mapped));
    }

    public @Nullable TypedClassMapping mappedClass(Collection<String> mapped) {
        return classes.stream().filter(e -> !Collections.disjoint(e.mappings(), mapped)).findFirst().orElse(null);
    }

    public int size() {
        return classes.size();
    }
}
