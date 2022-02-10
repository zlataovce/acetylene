package me.kcra.acetylene.core;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * A simple mapping file containing class mappings.
 */
public record MappingFile(@Unmodifiable List<ClassMapping> classes) {
    /**
     * Gets a class mapping based on its original (obfuscated) mapping.
     *
     * @param original the original mapping
     * @return the class mapping, null if not found
     */
    public @Nullable ClassMapping mapping(String original) {
        return classes.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    /**
     * Gets a class mapping based on its mapping (non-obfuscated).
     *
     * @param mapped the mapping
     * @return the class mapping, null if not found
     */
    public @Nullable ClassMapping mappedClass(String mapped) {
        return classes.stream().filter(e -> e.mapped().equals(mapped)).findFirst().orElse(null);
    }
}
