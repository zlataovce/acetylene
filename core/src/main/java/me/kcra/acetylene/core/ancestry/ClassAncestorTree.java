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

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A record representing an ancestor tree of a class name, traced using multiple inconsistent mapping types.
 */
public record ClassAncestorTree(@Unmodifiable List<TypedClassMapping> classes, int offset) {
    public static ClassAncestorTree of(String refClass, TypedMappingFile... files) {
        return of(Arrays.asList(files), List.of(refClass));
    }

    public static ClassAncestorTree of(String refClass, List<TypedMappingFile> files) {
        return of(files, List.of(refClass));
    }

    public static ClassAncestorTree of(List<TypedMappingFile> files, List<String> refClassS) {
        int i = 0;
        TypedClassMapping refClass = null;
        for (TypedMappingFile refFile : files) {
            refClass = refFile.mappedClass(refClassS);
            if (refClass != null) {
                break;
            }
            i++;
        }
        if (refClass == null) {
            throw new IllegalArgumentException("Reference class not found");
        }
        final List<TypedClassMapping> mappings = new ArrayList<>();
        mappings.add(refClass);
        final AtomicReference<List<String>> currentMappings = new AtomicReference<>(refClass.mappings().stream().map(Pair::value).toList());
        for (TypedMappingFile file : files.subList(i + 1, files.size())) {
            final TypedClassMapping result = file.classes().stream()
                    .filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), currentMappings.get()))
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new ClassAncestorTree(Collections.unmodifiableList(mappings), i);
            }
            currentMappings.set(result.mappings().stream().map(Pair::value).toList());
            mappings.add(result);
        }
        return new ClassAncestorTree(Collections.unmodifiableList(mappings), i);
    }

    public @Nullable TypedClassMapping mapping(int index) {
        if (!has(index)) {
            return null;
        }
        return classes.get(index - offset);
    }

    public int size() {
        return classes.size();
    }

    public boolean has(int index) {
        return (index - offset) < size() && (index - offset) >= 0;
    }

    public DescriptableAncestorTree fieldAncestors(String... refFieldS) {
        return fieldAncestors(Arrays.asList(refFieldS));
    }

    public DescriptableAncestorTree fieldAncestors(List<String> refFieldS) {
        int i = 0;
        TypedDescriptableMapping refField = null;
        for (TypedClassMapping refClass : classes) {
            refField = refClass.mappedField(refFieldS);
            if (refField != null) {
                break;
            }
            i++;
        }
        if (refField == null) {
            throw new IllegalArgumentException("Reference field not found");
        }
        return DescriptableAncestorTree.of(refField, offset + i, true, classes.stream().skip(i + 1).map(tcm -> tcm.fields().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public DescriptableAncestorTree methodAncestors(String refMethodS, String refDescriptorS) {
        int i = 0;
        TypedDescriptableMapping refMethod = null;
        for (TypedClassMapping refClass : classes) {
            refMethod = refClass.mappedMethod(refMethodS, refDescriptorS);
            if (refMethod != null) {
                break;
            }
            i++;
        }
        if (refMethod == null) {
            throw new IllegalArgumentException("Reference method not found");
        }
        return DescriptableAncestorTree.of(refMethod, offset + i, false, classes.stream().skip(i + 1).map(tcm -> tcm.methods().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public DescriptableAncestorTree methodAncestors(List<Pair<String, String>> refMethodS) {
        int i = 0;
        TypedDescriptableMapping refMethod = null;
        for (TypedClassMapping refClass : classes) {
            refMethod = refClass.mappedMethod(refMethodS);
            if (refMethod != null) {
                break;
            }
            i++;
        }
        if (refMethod == null) {
            throw new IllegalArgumentException("Reference method not found");
        }
        return DescriptableAncestorTree.of(refMethod, offset + i, false, classes.stream().skip(i + 1).map(tcm -> tcm.methods().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public @Unmodifiable List<TypedDescriptableMapping> walkFields() {
        final Set<String> mappingSet = new HashSet<>();
        final List<TypedDescriptableMapping> fields = new ArrayList<>();
        for (TypedClassMapping clazz : classes) {
            fields.addAll(
                    clazz.fields().stream()
                            .filter(f -> {
                                final List<String> mapStr = f.mappings().stream().map(e -> e.value().key()).toList();
                                final boolean disjoint = Collections.disjoint(mapStr, mappingSet);
                                if (disjoint) {
                                    mappingSet.addAll(mapStr);
                                }
                                return disjoint;
                            })
                            .toList()
            );
        }
        return Collections.unmodifiableList(fields);
    }

    public @Unmodifiable List<TypedDescriptableMapping> walkMethods() {
        final Set<String> mappingSet = new HashSet<>();
        final List<TypedDescriptableMapping> methods = new ArrayList<>();
        for (TypedClassMapping clazz : classes) {
            methods.addAll(
                    clazz.methods().stream()
                            .filter(m -> {
                                final List<String> mapStr = m.mappings().stream().map(e -> e.value().key()).toList();
                                final boolean disjoint = Collections.disjoint(mapStr, mappingSet);
                                if (disjoint) {
                                    mappingSet.addAll(mapStr);
                                }
                                return disjoint;
                            })
                            .toList()
            );
        }
        return Collections.unmodifiableList(methods);
    }
}
