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

package me.kcra.acetylene.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A generic immutable identifier.
 */
public interface Identifier {
    /**
     * A cache for {@link Identifier} instances based on their names.
     */
    Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    /**
     * Gets the identifying name of this {@link Identifier}.
     *
     * @return the identifying name
     */
    @NotNull
    String name();

    /**
     * Retrieves an {@link Identifier} from the cache, creating it, if missing.
     *
     * @param name the identifying name
     * @return the identifier
     */
    static Identifier of(@NotNull String name) {
        Preconditions.checkNotNull(name, "Name must not be null");
        return CACHE.computeIfAbsent(name, key -> new Identifier() {
            @Override
            public @NotNull String name() {
                return name;
            }

            @Override
            public String toString() {
                return name;
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof final Identifier id)) {
                    return false;
                }
                return name.equals(id.name());
            }
        });
    }
}
