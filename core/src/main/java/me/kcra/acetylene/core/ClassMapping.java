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
