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
import me.kcra.acetylene.core.utils.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing a descriptable (field, method) mapping consisting of several (non-obfuscated) mappings with the same original (obfuscated) mapping.<br>
 * This is useful for creating {@link me.kcra.acetylene.core.ancestry.ClassAncestorTree}s from several inconsistent mapping types.
 * <p>
 * Mappings are stored in the mappings field ({@link #mappings()}) with the following structure:<br>
 * <strong>[version - [mapped, mappedDescriptor]]</strong>
 */
public record TypedDescriptableMapping(String original, String descriptor,
                                       @Unmodifiable List<Pair<Identifier, Pair<String, String>>> mappings) implements Descriptable {
    /**
     * A constant of the Latin alphabet.
     */
    private static final List<Integer> ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".chars().boxed().toList();

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String mapped() {
        return mappings.stream().map(Pair::value).map(Pair::key).collect(Collectors.joining(","));
    }

    /**
     * Gets the non-obfuscated mapping by its type.
     *
     * @param type the mapping type
     * @return the mapping, null if not found
     */
    public @Nullable String mapped(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).map(Pair::key).orElse(null);
    }

    /**
     * Determines if this {@link TypedDescriptableMapping} has the supplied mapping type.
     *
     * @param type the mapping type
     * @return does this class have the supplied mapping type?
     */
    public boolean has(Identifier type) {
        return mappings.stream().anyMatch(e -> e.key().equals(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String mappedDescriptor() {
        return mappings.stream().map(Pair::value).map(Pair::value).collect(Collectors.joining(","));
    }

    /**
     * Gets the mapped (non-obfuscated) descriptor by its type.
     *
     * @param type the mapping type
     * @return the mapped descriptor, null if not found
     */
    public @Nullable String mappedDescriptor(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).map(Pair::value).orElse(null);
    }

    /**
     * Gets the mapping pair by its type.
     *
     * @param type the mapping type
     * @return the mapping, null if not found
     */
    public @Nullable Pair<String, String> mapping(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).orElse(null);
    }

    /**
     * Tries to determine whether the mapping follows the <a href="https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html">constant naming convention</a>.
     *
     * @param type the mapping type
     * @return is the mapping a constant?
     */
    public boolean isConstant(Identifier type) {
        Preconditions.checkArgument(has(type), "Invalid type");
        for (char c : Objects.requireNonNull(mapped(type)).toCharArray()) {
            if (ALPHABET.contains((int) c) && Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }
}
