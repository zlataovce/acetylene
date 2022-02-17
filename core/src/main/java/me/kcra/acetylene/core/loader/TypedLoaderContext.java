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

package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A non-reusable typed mapping loader context.
 */
public abstract class TypedLoaderContext {
    /**
     * A null identifier constant.
     */
    protected static final Identifier NULL = null;
    /**
     * The loaded class mappings.
     */
    private final List<TypedClassMapping> mappings = new ArrayList<>();

    /**
     * Checks if the value of the supplied pair is not null.<br>
     * This is for loader implementation use only, meant to be used in Streams.
     *
     * @param pair the pair
     * @param <K> the pair key type
     * @param <V> the pair value type
     * @return is the pair value not null?
     */
    @ApiStatus.Internal
    protected static <K, V> boolean nonNullPairValue(Pair<K, V> pair) {
        return pair.value() != null;
    }

    /**
     * Loads the supplied files into this context.<br>
     * This method is for implementation only.
     *
     * @param files the files to be loaded
     */
    @ApiStatus.Internal
    protected abstract void loadFiles0(List<Object> files);

    /**
     * Adds a class mapping to this context.
     *
     * @param typedClassMapping the class mapping
     */
    @ApiStatus.Internal
    protected void addClass(@NotNull TypedClassMapping typedClassMapping) {
        mappings.add(typedClassMapping);
    }

    /**
     * Loads the supplied files into this context.
     *
     * @param files the files to be loaded
     */
    public TypedLoaderContext loadFiles(List<Object> files) {
        loadFiles0(files);
        return this;
    }

    /**
     * Creates a new mapping file from the classes loaded into this context.
     *
     * @return the mapping file
     */
    public TypedMappingFile build() {
        return new TypedMappingFile(List.copyOf(mappings));
    }
}
