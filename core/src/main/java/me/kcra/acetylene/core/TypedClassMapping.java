/*
 * This file is part of acetylene, licensed under the MIT License.
 *
 * Copyright (c) 2022 Matouš Kučera
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 * Class representing a class mapping consisting of several (non-obfuscated) mappings with the same original (obfuscated) mapping.<br>
 * This is useful for creating {@link me.kcra.acetylene.core.ancestry.ClassAncestorTree}s from several inconsistent mapping types.
 * <p>
 * Mappings are stored in the mappings field ({@link #mappings()}) with the following structure:<br>
 * <strong>[version - mapped]</strong>
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

    /**
     * Gets a field mapping by its original (obfuscated) name.
     *
     * @param original the original name
     * @return the field mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping field(String original) {
        return fields.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    /**
     * Gets a field mapping by its mapped (non-obfuscated) name.
     *
     * @param mapped the mapped name
     * @return the field mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping mappedField(String mapped) {
        return fields.stream().filter(e -> e.mappings().stream().anyMatch(p -> p.value().key().equals(mapped))).findFirst().orElse(null);
    }

    /**
     * Gets a field mapping by its mapped (non-obfuscated) names.<br>
     * <strong>At least one name needs to match one of the names in the field mapping for it to be selected.</strong>
     *
     * @param mapped the mapped names
     * @return the field mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping mappedField(String... mapped) {
        return mappedField(Arrays.asList(mapped));
    }

    /**
     * Gets a field mapping by its mapped (non-obfuscated) names.<br>
     * <strong>At least one name needs to match one of the names in the field mapping for it to be selected.</strong>
     *
     * @param mapped the mapped names
     * @return the field mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping mappedField(Collection<String> mapped) {
        return fields.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).map(Pair::key).toList(), mapped)).findFirst().orElse(null);
    }

    /**
     * Gets a method mapping by its original (obfuscated) name and descriptor.
     *
     * @param original the original name
     * @return the method mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping method(String original, String descriptor) {
        return methods.stream().filter(e -> e.original().equals(original) && e.descriptor().equals(descriptor)).findFirst().orElse(null);
    }

    /**
     * Gets a method mapping by its mapped (non-obfuscated) name and descriptor.
     *
     * @param mapped the mapped name
     * @param mappedDescriptor the mapped descriptor
     * @return the method mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping mappedMethod(String mapped, String mappedDescriptor) {
        return mappedMethod(Pair.of(mapped, mappedDescriptor));
    }

    /**
     * Gets a method mapping by its mapped (non-obfuscated) mapping pair.
     *
     * @param mapped the mapping pair
     * @return the method mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping mappedMethod(Pair<String, String> mapped) {
        return methods.stream().filter(e -> e.mappings().stream().map(Pair::value).anyMatch(p -> p.equals(mapped))).findFirst().orElse(null);
    }

    /**
     * Gets a method mapping by its mapped (non-obfuscated) mapping pairs.<br>
     * <strong>At least one name and descriptor needs to match one of the names and descriptors in the method mapping for it to be selected.</strong>
     *
     * @param mapped the mapping pairs
     * @return the method mapping, null if not found
     */
    public @Nullable TypedDescriptableMapping mappedMethod(Collection<Pair<String, String>> mapped) {
        return methods.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), mapped)).findFirst().orElse(null);
    }
}
