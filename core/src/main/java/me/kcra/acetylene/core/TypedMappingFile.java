package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class representing several obfuscation mapping files with the same original (obfuscated) mapping.
 * <p>
 * This is useful for creating {@link me.kcra.acetylene.core.ancestry.ClassAncestorTree}s from several inconsistent mapping types.
 */
public record TypedMappingFile(@Unmodifiable List<TypedClassMapping> classes) {
    /**
     * Looks up a {@link TypedClassMapping} belonging to this {@link TypedMappingFile} based on its original (obfuscated) mapping.
     *
     * @param original the original mapping
     * @return the typed mapping, null if not found
     */
    public @Nullable TypedClassMapping mapping(String original) {
        return classes.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    /**
     * Looks up a {@link TypedClassMapping} belonging to this {@link TypedMappingFile} based on one of its non-obfuscated mappings.
     *
     * @param mapped the mapping
     * @return the typed mapping, null if not found
     */
    public @Nullable TypedClassMapping mappedClass(String mapped) {
        return classes.stream().filter(e -> e.mappings().stream().anyMatch(p -> p.value().equals(mapped))).findFirst().orElse(null);
    }

    /**
     * Looks up a {@link TypedClassMapping} belonging to this {@link TypedMappingFile} based on one of its non-obfuscated mappings.
     * <p>
     * At least <strong>one</strong> mapping from the supplied array has to match at least <strong>one</strong> of the class's non-obfuscated mappings.
     *
     * @param mapped the mappings
     * @return the typed mapping, null if not found
     */
    public @Nullable TypedClassMapping mappedClass(String... mapped) {
        return mappedClass(Arrays.asList(mapped));
    }

    /**
     * Looks up a {@link TypedClassMapping} belonging to this {@link TypedMappingFile} based on one of its non-obfuscated mappings.
     * <p>
     * At least <strong>one</strong> mapping from the supplied {@link Collection} has to match at least <strong>one</strong> of the class's non-obfuscated mappings.
     *
     * @param mapped the mappings
     * @return the typed mapping, null if not found
     */
    public @Nullable TypedClassMapping mappedClass(Collection<String> mapped) {
        return classes.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), mapped)).findFirst().orElse(null);
    }

    /**
     * Counts the unique classes contained in this {@link TypedMappingFile}.
     *
     * @return the amount of classes
     */
    public int size() {
        return classes.size();
    }
}
