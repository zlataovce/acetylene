package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a class mapping consisting of several (non-obfuscated) mappings with the same original (obfuscated) mapping.
 * <p>
 * This is useful for creating {@link me.kcra.acetylene.core.ancestry.ClassAncestorTree}s from several inconsistent mapping types.
 */
public record TypedClassMapping(String original, @Unmodifiable List<Pair<Identifier, String>> mappings,
                                @Unmodifiable List<TypedDescriptableMapping> fields,
                                @Unmodifiable List<TypedDescriptableMapping> methods) implements Mappable {
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String mapped() {
        return String.join(",", mappings.stream().map(Pair::value).toList());
    }

    /**
     * Gets the non-obfuscated mapping by its type.
     *
     * @param type the mapping type
     * @return the mapping, null if not found
     */
    public @Nullable String mapped(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).orElse(null);
    }

    /**
     * Determines if this {@link TypedClassMapping} has the supplied mapping type.
     *
     * @param type the mapping type
     * @return does this class have the supplied mapping type?
     */
    public boolean has(Identifier type) {
        return mappings.stream().anyMatch(e -> e.key().equals(type));
    }

    public @Nullable TypedDescriptableMapping field(String original) {
        return fields.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedField(String mapped) {
        return fields.stream().filter(e -> e.mappings().stream().anyMatch(p -> p.value().key().equals(mapped))).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedField(String... mapped) {
        return mappedField(Arrays.asList(mapped));
    }

    public @Nullable TypedDescriptableMapping mappedField(Collection<String> mapped) {
        return fields.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).map(Pair::key).toList(), mapped)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping method(String original, String descriptor) {
        return methods.stream().filter(e -> e.original().equals(original) && e.descriptor().equals(descriptor)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedMethod(String mapped, String mappedDescriptor) {
        return mappedMethod(Pair.of(mapped, mappedDescriptor));
    }

    public @Nullable TypedDescriptableMapping mappedMethod(Pair<String, String> mapped) {
        return methods.stream().filter(e -> e.mappings().stream().map(Pair::value).anyMatch(p -> p.equals(mapped))).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedMethod(Collection<Pair<String, String>> mapped) {
        return methods.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), mapped)).findFirst().orElse(null);
    }
}
