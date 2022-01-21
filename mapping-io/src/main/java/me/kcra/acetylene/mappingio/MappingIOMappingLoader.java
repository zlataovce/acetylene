package me.kcra.acetylene.mappingio;

import me.kcra.acetylene.core.loader.AbstractMappingLoader;
import me.kcra.acetylene.core.loader.LoaderContext;
import me.kcra.acetylene.core.loader.TypedLoaderContext;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MappingIOMappingLoader extends AbstractMappingLoader<MappingIOLoaderContext, MappingIOTypedLoaderContext> {
    protected MappingIOMappingLoader(List<File> files) {
        super(files);
    }

    public static AbstractMappingLoader<? extends LoaderContext, ? extends TypedLoaderContext> of(File... files) {
        return of(Arrays.asList(files));
    }

    public static AbstractMappingLoader<? extends LoaderContext, ? extends TypedLoaderContext> of(List<File> files) {
        return new MappingIOMappingLoader(files);
    }

    @Override
    protected MappingIOLoaderContext context() {
        return new MappingIOLoaderContext();
    }

    @Override
    protected MappingIOTypedLoaderContext typedContext() {
        return new MappingIOTypedLoaderContext();
    }
}
