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

package me.kcra.acetylene.mappingio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.loader.TypedLoaderContext;
import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static me.kcra.acetylene.mappingio.MappingIOLoaderContext.getFirstNamespace;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MappingIOTypedLoaderContext extends TypedLoaderContext {
    @Override
    protected void loadFiles0(List<Object> files) {
        final List<Pair<Identifier, MemoryMappingTree>> mappingFiles = files.stream()
                .map(file -> {
                    Identifier id = NULL;
                    MemoryMappingTree mappingTree = new MemoryMappingTree();
                    try {
                        if (file instanceof File) {
                            MappingReader.read(((File) file).toPath(), mappingTree);
                        } else if (file instanceof Path) {
                            MappingReader.read((Path) file, mappingTree);
                        } else if (file instanceof MemoryMappingTree) {
                            mappingTree = (MemoryMappingTree) file;
                        } else if (file instanceof final Pair<?, ?> pair) {
                            if (pair.key() instanceof Identifier) {
                                id = (Identifier) pair.key();
                                final Object value = pair.value();
                                if (value == null) {
                                    return null;
                                }
                                if (value instanceof File) {
                                    MappingReader.read(((File) value).toPath(), mappingTree);
                                } else if (value instanceof Path) {
                                    MappingReader.read(((Path) value), mappingTree);
                                } else if (value instanceof MemoryMappingTree) {
                                    mappingTree = (MemoryMappingTree) value;
                                } else {
                                    throw new IllegalArgumentException("Unsupported pair value");
                                }
                            } else {
                                throw new IllegalArgumentException("Unsupported pair key");
                            }
                        } else {
                            throw new IllegalArgumentException("Unsupported file");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Could not load file", e);
                    }
                    return Pair.of(id, mappingTree);
                })
                .filter(Objects::nonNull)
                .toList();
        for (MappingTree.ClassMapping classMapping : mappingFiles.get(0).value().getClasses()) {
            final List<Pair<Identifier, MappingTree.ClassMapping>> otherClasses = mappingFiles.stream()
                    .map(pair -> Pair.of(pair.key(), (MappingTree.ClassMapping) pair.value().getClass(classMapping.getSrcName())))
                    .filter(TypedLoaderContext::nonNullPairValue)
                    .toList();
            addClass(
                    new TypedClassMapping(
                            classMapping.getSrcName(),
                            otherClasses.stream()
                                    .map(pair -> Pair.of(pair.key(), pair.value().getDstName(getFirstNamespace(pair.value()))))
                                    .toList(),
                            classMapping.getFields().stream()
                                    .map(fieldMapping -> {
                                        final List<Pair<Identifier, MappingTree.FieldMapping>> otherFields = otherClasses.stream()
                                                .map(pair -> Pair.of(pair.key(), pair.value().getField(fieldMapping.getSrcName(), fieldMapping.getSrcDesc())))
                                                .filter(TypedLoaderContext::nonNullPairValue)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                fieldMapping.getSrcName(),
                                                fieldMapping.getSrcDesc(),
                                                otherFields.stream()
                                                        .map(pair -> {
                                                            final int firstNamespace = getFirstNamespace(pair.value());
                                                            return Pair.of(pair.key(), Pair.of(pair.value().getDstName(firstNamespace), pair.value().getDstDesc(firstNamespace)));
                                                        })
                                                        .toList()
                                        );
                                    })
                                    .toList(),
                            classMapping.getMethods().stream()
                                    .map(methodMapping -> {
                                        final List<Pair<Identifier, MappingTree.MethodMapping>> otherMethods = otherClasses.stream()
                                                .map(pair -> Pair.of(pair.key(), pair.value().getMethod(methodMapping.getSrcName(), methodMapping.getSrcDesc())))
                                                .filter(TypedLoaderContext::nonNullPairValue)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                methodMapping.getSrcName(),
                                                methodMapping.getSrcDesc(),
                                                otherMethods.stream()
                                                        .map(pair -> {
                                                            final int firstNamespace = getFirstNamespace(pair.value());
                                                            return Pair.of(pair.key(), Pair.of(pair.value().getDstName(firstNamespace), pair.value().getDstDesc(firstNamespace)));
                                                        })
                                                        .toList()
                                        );
                                    })
                                    .toList()
                    )
            );
        }
    }
}
