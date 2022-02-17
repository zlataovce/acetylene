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

import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.MappingFile;
import me.kcra.acetylene.core.utils.Preconditions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A non-reusable mapping loader context.
 */
public abstract class LoaderContext {
    /**
     * The loaded class mappings.
     */
    private final List<ClassMapping> mappings = new ArrayList<>();

    /**
     * Loads the supplied file into this context.<br>
     * This method is for implementation only.
     *
     * @param file the file to be loaded
     */
    @ApiStatus.Internal
    protected abstract void loadFile0(Object file);

    /**
     * Adds a class mapping to this context.
     *
     * @param classMapping the class mapping
     */
    @ApiStatus.Internal
    protected void addClass(@NotNull ClassMapping classMapping) {
        mappings.add(classMapping);
    }

    /**
     * Loads the supplied file into this context.
     *
     * @param file the file to be loaded
     * @return this context
     */
    public LoaderContext loadFile(@NotNull Object file) {
        loadFile0(Preconditions.checkNotNull(file, "File cannot be null"));
        return this;
    }

    /**
     * Creates a new mapping file from the classes loaded into this context.
     *
     * @return the mapping file
     */
    public MappingFile build() {
        return new MappingFile(List.copyOf(mappings));
    }
}
