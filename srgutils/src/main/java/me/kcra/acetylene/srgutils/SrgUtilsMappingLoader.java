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

import me.kcra.acetylene.core.loader.AbstractMappingLoader;
import me.kcra.acetylene.core.loader.LoaderContext;
import me.kcra.acetylene.core.loader.TypedLoaderContext;

import java.util.Arrays;
import java.util.List;

public class SrgUtilsMappingLoader extends AbstractMappingLoader<SrgUtilsLoaderContext, SrgUtilsTypedLoaderContext> {
    protected SrgUtilsMappingLoader(List<Object> files) {
        super(files);
    }

    public static AbstractMappingLoader<? extends LoaderContext, ? extends TypedLoaderContext> of(Object... files) {
        return of(Arrays.asList(files));
    }

    public static AbstractMappingLoader<? extends LoaderContext, ? extends TypedLoaderContext> of(List<Object> files) {
        return new SrgUtilsMappingLoader(files);
    }

    @Override
    protected SrgUtilsLoaderContext context() {
        return new SrgUtilsLoaderContext();
    }

    @Override
    protected SrgUtilsTypedLoaderContext typedContext() {
        return new SrgUtilsTypedLoaderContext();
    }
}
