package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ClassAncestorTree(List<TypedClassMapping> classes, List<TypedMappingFile> files) {
    public static ClassAncestorTree of(TypedClassMapping refClass, TypedMappingFile... files) {
        return of(refClass, Arrays.asList(files));
    }

    public static ClassAncestorTree of(TypedClassMapping refClass, List<TypedMappingFile> files) {
        final List<TypedClassMapping> mappings = new ArrayList<>();
        for (TypedMappingFile file : files) {
            final TypedClassMapping result = file.mappedClass(refClass.mappings());
            if (result == null) {
                return new ClassAncestorTree(Collections.unmodifiableList(mappings), files);
            }
            mappings.add(result);
        }
        return new ClassAncestorTree(Collections.unmodifiableList(mappings), files);
    }

    public @Nullable TypedClassMapping mapping(int index) {
        return classes.get(index);
    }

    public int size() {
        return classes.size();
    }
}
