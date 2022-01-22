package me.kcra.acetylene.core.loader;

import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.MappingFile;
import me.kcra.acetylene.core.utils.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class LoaderContext {
    private final List<ClassMapping> mappings = new ArrayList<>();

    protected abstract void loadFile0(Object file);

    protected void addClass(@NotNull ClassMapping classMapping) {
        mappings.add(classMapping);
    }

    public LoaderContext loadFile(@NotNull Object file) {
        loadFile0(Preconditions.checkNotNull(file, "File cannot be null"));
        return this;
    }

    public MappingFile build() {
        return new MappingFile(List.copyOf(mappings));
    }
}
