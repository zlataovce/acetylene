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

import me.kcra.acetylene.core.MappingFile;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * A class representing a reusable mapping loader.
 *
 * @param <T> the loader context
 * @param <R> the typed loader context
 */
public abstract class AbstractMappingLoader<T extends LoaderContext, R extends TypedLoaderContext> {
    /**
     * The files to be loaded.
     */
    private final List<Object> files;

    /**
     * Constructs a new mapping loader.
     *
     * @param files the files to be loaded
     */
    protected AbstractMappingLoader(List<Object> files) {
        this.files = files.stream().filter(Objects::nonNull).toList();
        Preconditions.checkArgument(this.files.size() > 0, "No files");
    }

    /**
     * Creates a new {@link LoaderContext} for this loader.
     *
     * @return the context
     */
    protected abstract T context();

    /**
     * Creates a new {@link TypedLoaderContext} for this loader.
     *
     * @return the context
     */
    protected abstract R typedContext();

    /**
     * Loads the first mapping file in this loader.
     *
     * @return the mapping file
     */
    public MappingFile load() {
        return context()
                .loadFile(files.get(0))
                .build();
    }

    /**
     * Loads all mapping files in this loader, joining them together to a typed mapping file.
     *
     * @return the mapping file
     */
    public TypedMappingFile loadTyped() {
        return typedContext()
                .loadFiles(files)
                .build();
    }
}
