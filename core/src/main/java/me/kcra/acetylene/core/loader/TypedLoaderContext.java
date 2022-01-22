package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class TypedLoaderContext {
    protected static final Identifier NULL = null;
    private final List<TypedClassMapping> mappings = new ArrayList<>();

    protected static <K, V> boolean nonNullPairValue(Pair<K, V> pair) {
        return pair.value() != null;
    }

    protected abstract void loadFiles0(List<Object> files);

    protected void addClass(@NotNull TypedClassMapping typedClassMapping) {
        mappings.add(typedClassMapping);
    }

    public TypedLoaderContext loadFiles(List<Object> files) {
        loadFiles0(files);
        return this;
    }

    public TypedMappingFile build() {
        return new TypedMappingFile(List.copyOf(mappings));
    }
}
