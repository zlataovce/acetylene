package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.MappingFile;
import me.kcra.acetylene.core.utils.Preconditions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A non-reusable mapping loader context.
 */
public abstract class LoaderContext {
    /**
     * The loaded class mappings.
     */
    private final List<ClassMapping> mappings = new ArrayList<>();

    /**
     * Loads the supplied file into this context.<br>
     * This method is for implementation only.
     *
     * @param file the file to be loaded
     */
    @ApiStatus.Internal
    protected abstract void loadFile0(Object file);

    /**
     * Adds a class mapping to this context.
     *
     * @param classMapping the class mapping
     */
    @ApiStatus.Internal
    protected void addClass(@NotNull ClassMapping classMapping) {
        mappings.add(classMapping);
    }

    /**
     * Loads the supplied file into this context.
     *
     * @param file the file to be loaded
     * @return this context
     */
    public LoaderContext loadFile(@NotNull Object file) {
        loadFile0(Preconditions.checkNotNull(file, "File cannot be null"));
        return this;
    }

    /**
     * Creates a new mapping file from the classes loaded into this context.
     *
     * @return the mapping file
     */
    public MappingFile build() {
        return new MappingFile(List.copyOf(mappings));
    }
}
