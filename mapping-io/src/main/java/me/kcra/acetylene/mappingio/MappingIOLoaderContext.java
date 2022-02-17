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
import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.DescriptableMapping;
import me.kcra.acetylene.core.loader.LoaderContext;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MappingTreeView;
import net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MappingIOLoaderContext extends LoaderContext {
    @Override
    protected void loadFile0(Object file) {
        MemoryMappingTree mappingTree = new MemoryMappingTree();
        try {
            if (file instanceof File) {
                MappingReader.read(((File) file).toPath(), mappingTree);
            } else if (file instanceof Path) {
                MappingReader.read((Path) file, mappingTree);
            } else if (file instanceof MemoryMappingTree) {
                mappingTree = (MemoryMappingTree) file;
            } else {
                throw new IllegalArgumentException("Unsupported file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load file", e);
        }
        for (MappingTree.ClassMapping classMapping : mappingTree.getClasses()) {
            addClass(new ClassMapping(
                    classMapping.getSrcName(),
                    classMapping.getName(getFirstNamespace(classMapping)),
                    classMapping.getFields().stream()
                            .map(fieldMapping -> {
                                final int firstNamespace = getFirstNamespace(fieldMapping);
                                return new DescriptableMapping(
                                        fieldMapping.getSrcName(),
                                        fieldMapping.getDstName(firstNamespace),
                                        fieldMapping.getSrcDesc(),
                                        fieldMapping.getDstDesc(firstNamespace)
                                );
                            })
                            .toList(),
                    classMapping.getMethods().stream()
                            .map(methodMapping -> {
                                final int firstNamespace = getFirstNamespace(methodMapping);
                                return new DescriptableMapping(
                                        methodMapping.getSrcName(),
                                        methodMapping.getDstName(firstNamespace),
                                        methodMapping.getSrcDesc(),
                                        methodMapping.getDstDesc(firstNamespace)
                                );
                            })
                            .toList()
            ));
        }
    }

    protected static int getFirstNamespace(MappingTreeView.ElementMappingView mappingView) {
        return mappingView.getTree().getNamespaceId(mappingView.getTree().getDstNamespaces().get(0));
    }
}
