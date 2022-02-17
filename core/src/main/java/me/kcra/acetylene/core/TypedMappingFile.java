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
