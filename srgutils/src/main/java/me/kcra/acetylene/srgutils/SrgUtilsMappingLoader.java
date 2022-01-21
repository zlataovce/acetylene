package me.kcra.acetylene.srgutils;

import me.kcra.acetylene.core.loader.AbstractMappingLoader;
import me.kcra.acetylene.core.loader.LoaderContext;

import java.io.File;

public class SrgUtilsMappingLoader extends AbstractMappingLoader<SrgUtilsLoaderContext> {
    protected SrgUtilsMappingLoader(File file) {
        super(file);
    }

    public static AbstractMappingLoader<? extends LoaderContext> of(File file) {
        return new SrgUtilsMappingLoader(file);
    }

    @Override
    protected SrgUtilsLoaderContext context() {
        return new SrgUtilsLoaderContext();
    }
}
