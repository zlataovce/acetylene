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
