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

package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record DescriptableAncestorTree(@Unmodifiable List<TypedDescriptableMapping> descriptables, int offset) {
    public static DescriptableAncestorTree of(TypedDescriptableMapping refDescr, int offset, boolean ignoreDescriptors, TypedDescriptableMapping[]... descs) {
        return of(refDescr, offset, ignoreDescriptors, Arrays.asList(descs));
    }

    public static DescriptableAncestorTree of(TypedDescriptableMapping refDescr, int offset, boolean ignoreDescriptors, List<TypedDescriptableMapping[]> descs) {
        final List<TypedDescriptableMapping> mappings = new ArrayList<>();
        mappings.add(refDescr);
        final AtomicReference<List<Pair<String, String>>> currentMappings = new AtomicReference<>(refDescr.mappings().stream().map(Pair::value).toList());
        for (TypedDescriptableMapping[] mappings1 : descs) {
            final TypedDescriptableMapping result = Arrays.stream(mappings1)
                    .filter(e -> {
                        if (ignoreDescriptors) {
                            return !Collections.disjoint(
                                    e.mappings().stream().map(Pair::value).map(Pair::key).toList(),
                                    currentMappings.get().stream().map(Pair::key).toList()
                            );
                        }
                        return !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), currentMappings.get());
                    })
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new DescriptableAncestorTree(Collections.unmodifiableList(mappings), offset);
            }
            currentMappings.set(result.mappings().stream().map(Pair::value).toList());
            mappings.add(result);
        }
        return new DescriptableAncestorTree(Collections.unmodifiableList(mappings), offset);
    }

    public @Nullable TypedDescriptableMapping mapping(int index) {
        if (!has(index)) {
            return null;
        }
        return descriptables.get(index - offset);
    }

    public int size() {
        return descriptables.size();
    }

    public boolean has(int index) {
        return (index - offset) < size() && (index - offset) >= 0;
    }
}
