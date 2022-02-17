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

package me.kcra.acetylene.srgutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.loader.TypedLoaderContext;
import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import net.minecraftforge.srgutils.IMappingFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SrgUtilsTypedLoaderContext extends TypedLoaderContext {
    @Override
    protected void loadFiles0(List<Object> files) {
        final List<Pair<Identifier, IMappingFile>> mappingFiles = files.stream()
                .map(file -> {
                    try {
                        if (file instanceof File) {
                            return Pair.of(NULL, IMappingFile.load((File) file));
                        } else if (file instanceof Path) {
                            return Pair.of(NULL, IMappingFile.load(((Path) file).toFile()));
                        } else if (file instanceof IMappingFile) {
                            return Pair.of(NULL, (IMappingFile) file);
                        } else if (file instanceof final Pair<?, ?> pair) {
                            if (pair.key() instanceof final Identifier key) {
                                final Object value = pair.value();
                                if (value == null) {
                                    return null;
                                }
                                if (value instanceof File) {
                                    return Pair.of(key, IMappingFile.load((File) value));
                                } else if (value instanceof Path) {
                                    return Pair.of(key, IMappingFile.load(((Path) value).toFile()));
                                } else if (value instanceof IMappingFile) {
                                    return Pair.of(key, (IMappingFile) value);
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
                })
                .filter(Objects::nonNull)
                .toList();
        mappingFiles.get(0).value().getClasses().forEach(iClass -> {
            final List<Pair<Identifier, IMappingFile.IClass>> otherClasses = mappingFiles.stream()
                    .map(pair -> Pair.of(pair.key(), pair.value().getClass(iClass.getOriginal())))
                    .filter(TypedLoaderContext::nonNullPairValue)
                    .toList();
            addClass(
                    new TypedClassMapping(
                            iClass.getOriginal(),
                            otherClasses.stream()
                                    .map(pair -> Pair.of(pair.key(), pair.value().getMapped()))
                                    .toList(),
                            iClass.getFields().stream()
                                    .map(iField -> {
                                        final List<Pair<Identifier, IMappingFile.IField>> otherFields = otherClasses.stream()
                                                .map(pair -> Pair.of(pair.key(), pair.value().getField(iField.getOriginal())))
                                                .filter(TypedLoaderContext::nonNullPairValue)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                iField.getOriginal(),
                                                iField.getDescriptor(),
                                                otherFields.stream()
                                                        .map(pair -> Pair.of(pair.key(), Pair.of(pair.value().getMapped(), pair.value().getMappedDescriptor())))
                                                        .toList()
                                        );
                                    })
                                    .toList(),
                            iClass.getMethods().stream()
                                    .map(iMethod -> {
                                        final List<Pair<Identifier, IMappingFile.IMethod>> otherMethods = otherClasses.stream()
                                                .map(pair -> Pair.of(pair.key(), pair.value().getMethod(iMethod.getOriginal(), iMethod.getDescriptor())))
                                                .filter(TypedLoaderContext::nonNullPairValue)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                iMethod.getOriginal(),
                                                iMethod.getDescriptor(),
                                                otherMethods.stream()
                                                        .map(pair -> Pair.of(pair.key(), Pair.of(pair.value().getMapped(), pair.value().getMappedDescriptor())))
                                                        .toList()
                                        );
                                    })
                                    .toList()
                    )
            );
        });
    }
}
