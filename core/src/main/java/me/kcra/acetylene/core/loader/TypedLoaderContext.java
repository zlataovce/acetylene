package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A non-reusable typed mapping loader context.
 */
public abstract class TypedLoaderContext {
    /**
     * A null identifier constant.
     */
    protected static final Identifier NULL = null;
    /**
     * The loaded class mappings.
     */
    private final List<TypedClassMapping> mappings = new ArrayList<>();

    /**
     * Checks if the value of the supplied pair is not null.<br>
     * This is for loader implementation use only, meant to be used in Streams.
     *
     * @param pair the pair
     * @param <K> the pair key type
     * @param <V> the pair value type
     * @return is the pair value not null?
     */
    @ApiStatus.Internal
    protected static <K, V> boolean nonNullPairValue(Pair<K, V> pair) {
        return pair.value() != null;
    }

    /**
     * Loads the supplied files into this context.<br>
     * This method is for implementation only.
     *
     * @param files the files to be loaded
     */
    @ApiStatus.Internal
    protected abstract void loadFiles0(List<Object> files);

    /**
     * Adds a class mapping to this context.
     *
     * @param typedClassMapping the class mapping
     */
    @ApiStatus.Internal
    protected void addClass(@NotNull TypedClassMapping typedClassMapping) {
        mappings.add(typedClassMapping);
    }

    /**
     * Loads the supplied files into this context.
     *
     * @param files the files to be loaded
     */
    public TypedLoaderContext loadFiles(List<Object> files) {
        loadFiles0(files);
        return this;
    }

    /**
     * Creates a new mapping file from the classes loaded into this context.
     *
     * @return the mapping file
     */
    public TypedMappingFile build() {
        return new TypedMappingFile(List.copyOf(mappings));
    }
}
