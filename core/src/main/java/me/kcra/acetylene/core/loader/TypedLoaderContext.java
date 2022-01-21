package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class TypedLoaderContext {
    private final List<TypedClassMapping> mappings = new ArrayList<>();

    protected abstract void loadFiles0(List<File> files);

    protected void addClass(@NotNull TypedClassMapping typedClassMapping) {
        mappings.add(typedClassMapping);
    }

    public TypedLoaderContext loadFiles(List<File> files) {
        loadFiles0(files);
        return this;
    }

    public TypedMappingFile build() {
        return new TypedMappingFile(List.copyOf(mappings));
    }
}
