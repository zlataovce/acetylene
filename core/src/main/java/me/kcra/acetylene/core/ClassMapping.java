package me.kcra.acetylene.core;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * A record representing a class mapping.
 */
public record ClassMapping(String original, String mapped, @Unmodifiable List<DescriptableMapping> fields,
                           @Unmodifiable List<DescriptableMapping> methods) implements Mappable {
    /**
     * Gets a field mapping based on its original (obfuscated) mapping.
     *
     * @param original the original mapping
     * @return the field mapping, null if not found
     */
    public @Nullable DescriptableMapping field(String original) {
        return fields.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    /**
     * Gets a field mapping based on its mapping (non-obfuscated).
     *
     * @param mapped the mapping
     * @return the field mapping, null if not found
     */
    public @Nullable DescriptableMapping mappedField(String mapped) {
        return fields.stream().filter(e -> e.mapped().equals(mapped)).findFirst().orElse(null);
    }

    /**
     * Gets a method mapping based on its original (obfuscated) mapping.
     *
     * @param original the original mapping
     * @param descriptor the original descriptor
     * @return the method mapping, null if not found
     */
    public @Nullable DescriptableMapping method(String original, String descriptor) {
        return methods.stream().filter(e -> e.original().equals(original) && e.descriptor().equals(descriptor)).findFirst().orElse(null);
    }

    /**
     * Gets a method mapping based on its mapping (non-obfuscated).
     *
     * @param mapped the mapping
     * @param descriptor the mapped descriptor
     * @return the method mapping, null if not found
     */
    public @Nullable DescriptableMapping mappedMethod(String mapped, String descriptor) {
        return methods.stream().filter(e -> e.mapped().equals(mapped) && e.mappedDescriptor().equals(descriptor)).findFirst().orElse(null);
    }
}
