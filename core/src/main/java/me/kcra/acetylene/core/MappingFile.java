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
