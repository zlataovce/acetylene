package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record ClassAncestorTree(List<TypedClassMapping> classes, List<TypedMappingFile> files) {
    public static ClassAncestorTree of(TypedClassMapping refClass, TypedMappingFile... files) {
        return of(refClass, Arrays.asList(files));
    }

    public static ClassAncestorTree of(TypedClassMapping refClass, List<TypedMappingFile> files) {
        final List<TypedClassMapping> mappings = new ArrayList<>();
        final AtomicReference<List<String>> currentMappings = new AtomicReference<>(refClass.mappings());
        for (TypedMappingFile file : files) {
            final TypedClassMapping result = file.classes().stream()
                    .filter(e -> !Collections.disjoint(e.mappings(), currentMappings.get()))
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new ClassAncestorTree(Collections.unmodifiableList(mappings), files);
            }
            currentMappings.set(result.mappings());
            mappings.add(result);
        }
        return new ClassAncestorTree(Collections.unmodifiableList(mappings), files);
    }

    public @Nullable TypedClassMapping mapping(int index) {
        if (index >= size()) {
            return null;
        }
        return classes.get(index);
    }

    public int size() {
        return classes.size();
    }
}
