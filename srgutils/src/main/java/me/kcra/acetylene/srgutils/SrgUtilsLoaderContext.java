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
import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.DescriptableMapping;
import me.kcra.acetylene.core.loader.LoaderContext;
import net.minecraftforge.srgutils.IMappingFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SrgUtilsLoaderContext extends LoaderContext {
    @Override
    protected void loadFile0(Object file) {
        IMappingFile mappingFile;
        try {
            if (file instanceof File) {
                mappingFile = IMappingFile.load((File) file);
            } else if (file instanceof Path) {
                mappingFile = IMappingFile.load(((Path) file).toFile());
            } else if (file instanceof IMappingFile) {
                mappingFile = (IMappingFile) file;
            } else {
                throw new IllegalArgumentException("Unsupported file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load file", e);
        }
        mappingFile.getClasses().forEach(iClass -> addClass(new ClassMapping(
                iClass.getOriginal(),
                iClass.getMapped(),
                iClass.getFields().stream()
                        .map(iField -> new DescriptableMapping(
                                iField.getOriginal(),
                                iField.getMapped(),
                                iField.getDescriptor(),
                                iField.getMappedDescriptor()
                        ))
                        .toList(),
                iClass.getMethods().stream()
                        .map(iMethod -> new DescriptableMapping(
                                iMethod.getOriginal(),
                                iMethod.getMapped(),
                                iMethod.getDescriptor(),
                                iMethod.getMappedDescriptor()
                        ))
                        .toList()
        )));
    }
}
