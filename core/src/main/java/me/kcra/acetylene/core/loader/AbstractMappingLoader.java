package me.kcra.acetylene.core.loader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.kcra.acetylene.core.MappingFile;

import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMappingLoader<T extends LoaderContext> {
    private final File file;

    protected abstract T context();

    public MappingFile load() {
        return context()
                .loadFile(file)
                .build();
    }
}
