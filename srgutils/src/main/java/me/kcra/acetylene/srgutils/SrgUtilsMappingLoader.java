package me.kcra.acetylene.srgutils;

import me.kcra.acetylene.core.loader.AbstractMappingLoader;
import me.kcra.acetylene.core.loader.LoaderContext;
import me.kcra.acetylene.core.loader.TypedLoaderContext;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SrgUtilsMappingLoader extends AbstractMappingLoader<SrgUtilsLoaderContext, SrgUtilsTypedLoaderContext> {
    protected SrgUtilsMappingLoader(List<File> files) {
        super(files);
    }

    public static AbstractMappingLoader<? extends LoaderContext, ? extends TypedLoaderContext> of(File... files) {
        return of(Arrays.asList(files));
    }

    public static AbstractMappingLoader<? extends LoaderContext, ? extends TypedLoaderContext> of(List<File> files) {
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
