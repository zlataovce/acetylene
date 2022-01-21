package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.MappingFile;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Preconditions;

import java.io.File;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMappingLoader<T extends LoaderContext, R extends TypedLoaderContext> {
    private final List<File> files;

    protected AbstractMappingLoader(List<File> files) {
        this.files = files.stream().filter(Objects::nonNull).toList();
        Preconditions.checkArgument(this.files.size() > 0, "No files");
    }

    protected abstract T context();

    protected abstract R typedContext();

    public MappingFile load() {
        return context()
                .loadFile(files.get(0))
                .build();
    }

    public TypedMappingFile loadTyped() {
        return typedContext()
                .loadFiles(files)
                .build();
    }
}
