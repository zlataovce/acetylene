package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.MappingFile;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * A class representing a reusable mapping loader.
 *
 * @param <T> the loader context
 * @param <R> the typed loader context
 */
public abstract class AbstractMappingLoader<T extends LoaderContext, R extends TypedLoaderContext> {
    /**
     * The files to be loaded.
     */
    private final List<Object> files;

    /**
     * Constructs a new mapping loader.
     *
     * @param files the files to be loaded
     */
    protected AbstractMappingLoader(List<Object> files) {
        this.files = files.stream().filter(Objects::nonNull).toList();
        Preconditions.checkArgument(this.files.size() > 0, "No files");
    }

    /**
     * Creates a new {@link LoaderContext} for this loader.
     *
     * @return the context
     */
    protected abstract T context();

    /**
     * Creates a new {@link TypedLoaderContext} for this loader.
     *
     * @return the context
     */
    protected abstract R typedContext();

    /**
     * Loads the first mapping file in this loader.
     *
     * @return the mapping file
     */
    public MappingFile load() {
        return context()
                .loadFile(files.get(0))
                .build();
    }

    /**
     * Loads all mapping files in this loader, joining them together to a typed mapping file.
     *
     * @return the mapping file
     */
    public TypedMappingFile loadTyped() {
        return typedContext()
                .loadFiles(files)
                .build();
    }
}
