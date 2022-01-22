package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public record ClassAncestorTree(List<TypedClassMapping> classes) {
    public static ClassAncestorTree of(String refClass, TypedMappingFile... files) {
        return of(refClass, Arrays.asList(files));
    }

    public static ClassAncestorTree of(String refClassS, List<TypedMappingFile> files) {
        final TypedClassMapping refClass = Objects.requireNonNull(files.get(0).mappedClass(refClassS));
        final List<TypedClassMapping> mappings = new ArrayList<>();
        mappings.add(refClass);
        final AtomicReference<List<String>> currentMappings = new AtomicReference<>(refClass.mappings());
        for (TypedMappingFile file : files.stream().skip(1).toList()) {
            final TypedClassMapping result = file.classes().stream()
                    .filter(e -> !Collections.disjoint(e.mappings(), currentMappings.get()))
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new ClassAncestorTree(Collections.unmodifiableList(mappings));
            }
            currentMappings.set(result.mappings());
            mappings.add(result);
        }
        return new ClassAncestorTree(Collections.unmodifiableList(mappings));
    }

    public @Nullable TypedClassMapping mapping(int index) {
        if (index >= size()) {
            return null;
        }
        return classes.get(index);
    }

    public DescriptableAncestorTree fieldAncestors(String refField) {
        return DescriptableAncestorTree.of(classes.get(0).mappedField(refField), true, classes.stream().skip(1).map(tcm -> tcm.fields().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public DescriptableAncestorTree methodAncestors(String refMethod, String refDescriptor) {
        return DescriptableAncestorTree.of(classes.get(0).mappedMethod(refMethod, refDescriptor), false, classes.stream().skip(1).map(tcm -> tcm.methods().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public int size() {
        return classes.size();
    }
}
